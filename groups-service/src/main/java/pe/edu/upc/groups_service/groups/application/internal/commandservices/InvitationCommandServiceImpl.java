package pe.edu.upc.groups_service.groups.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Invitation;
import pe.edu.upc.groups_service.groups.domain.model.commands.AcceptInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.CancelInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.CreateInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.RejectInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.MemberId;
import pe.edu.upc.groups_service.groups.domain.services.InvitationCommandService;
import pe.edu.upc.groups_service.groups.infrastructure.messaging.TasksEventPublisher;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.InvitationRepository;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.LeaderRepository;

import java.util.Optional;

@Service
public class InvitationCommandServiceImpl implements InvitationCommandService {

  private final InvitationRepository invitationRepository;
  private final GroupRepository groupRepository;
  private final LeaderRepository leaderRepository;
  private final TasksEventPublisher tasksEventPublisher;

  public InvitationCommandServiceImpl(
      InvitationRepository invitationRepository,
      GroupRepository groupRepository,
      LeaderRepository leaderRepository,
      TasksEventPublisher tasksEventPublisher) {

    this.invitationRepository = invitationRepository;
    this.groupRepository = groupRepository;
    this.leaderRepository = leaderRepository;
    this.tasksEventPublisher = tasksEventPublisher;
  }

  @Override
  public Optional<Invitation> handle(CreateInvitationCommand command) {
    var group = this.groupRepository.findById(command.groupId());
    if (group.isEmpty()) {
      throw new IllegalArgumentException("Group with id " + command.groupId() + " does not exist");
    }

    var memberId = new MemberId(command.memberId());
    if (this.invitationRepository.existsByMemberId(memberId)) {
      throw new IllegalArgumentException("Member with id " + memberId.value() + " already has an invitation");
    }

    var createdInvitation = new Invitation(memberId, group.get());
    this.invitationRepository.save(createdInvitation);

    return Optional.of(createdInvitation);
  }

  @Override
  public void handle(CancelInvitationCommand command) {
    var invitation = this.invitationRepository.findById(command.invitationId());
    if (invitation.isEmpty()) {
      throw new IllegalArgumentException("Invitation with id " + command.invitationId() + " does not exist");
    }

    if (!invitation.get().getMemberId().value().equals(command.memberId())) {
      throw new IllegalArgumentException("Member with id " + command.memberId() + " is not the owner of the invitation");
    }
    this.invitationRepository.delete(invitation.get());
  }

  private Invitation validateAndGetInvitation(Long invitationId, Long leaderId) {
    var invitation = this.invitationRepository.findById(invitationId)
        .orElseThrow(() -> new IllegalArgumentException("Invitation with id " + invitationId + " does not exist"));

    var leader = this.leaderRepository.findById(leaderId)
        .orElseThrow(() -> new IllegalArgumentException("Leader with id " + leaderId + " does not exist"));

    if (!invitation.getGroup().getLeader().getId().equals(leader.getId())) {
      throw new IllegalArgumentException("Leader with id " + leaderId + " is not the owner of the invitation");
    }

    return invitation;
  }

  @Override
  public void handle(RejectInvitationCommand command) {
    var invitation = validateAndGetInvitation(command.invitationId(), command.leaderId());
    this.invitationRepository.delete(invitation);
  }

  @Override
  public void handle(AcceptInvitationCommand command) {
    var invitation = validateAndGetInvitation(command.invitationId(), command.leaderId());

    var member = invitation.getMemberId();
    assert member != null;
    var group = invitation.getGroup();

    tasksEventPublisher.publishInvitationAccepted(group.getId(), member.value());
    group.increaseMemberCount();

    this.groupRepository.save(group);
    this.invitationRepository.delete(invitation);
  }
}