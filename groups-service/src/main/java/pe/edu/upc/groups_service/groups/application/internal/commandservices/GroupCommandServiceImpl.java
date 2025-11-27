package pe.edu.upc.groups_service.groups.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Group;
import pe.edu.upc.groups_service.groups.domain.model.commands.*;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.GroupCode;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.LeaderId;
import pe.edu.upc.groups_service.groups.domain.services.GroupCommandService;
import pe.edu.upc.groups_service.groups.infrastructure.messaging.TasksEventPublisher;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.LeaderRepository;

import java.util.Optional;

@Service
public class GroupCommandServiceImpl implements GroupCommandService {

  private final GroupRepository groupRepository;
  private final LeaderRepository leaderRepository;
  private final TasksEventPublisher tasksEventPublisher;

  public GroupCommandServiceImpl(GroupRepository groupRepository,
                                 LeaderRepository leaderRepository,
                                 TasksEventPublisher tasksEventPublisher) {
    this.groupRepository = groupRepository;
    this.leaderRepository = leaderRepository;
    this.tasksEventPublisher = tasksEventPublisher;
  }


  @Override
  public Optional<Group> handle(CreateGroupCommand command) {
    var leader = leaderRepository.findById(command.leaderId()).get();

    Group group = new Group(
        command.name(),
        command.description(),
        command.imgUrl(),
        leader,
        GroupCode.random()
    );
    while( groupRepository.existsByCode(group.getCode())) {
      group.setCode(GroupCode.random());
    }

    groupRepository.save(group);

    return Optional.of(group);
  }

  @Override
  public Optional<Group> handle(UpdateGroupCommand command) {
    var leaderId = command.leaderId();
    var group = groupRepository.findByLeaderId(leaderId).get();

    var groupId = group.getId();
    if(!this.groupRepository.existsById(groupId)) {
      throw new IllegalArgumentException("Group with id " + groupId + " does not exist");
    }

    var groupToUpdate = groupRepository.findById(groupId).get();
    groupToUpdate.updateInformation(command);

    try{
      var updatedGroup = groupRepository.save(groupToUpdate);
      return Optional.of(updatedGroup);
    } catch (Exception e) {
      throw new IllegalArgumentException("Error while updating group: " + e.getMessage());
    }
  }

  @Override
  public void handle(DeleteGroupCommand command) {
    var leaderId = command.leaderId();
    var groupId = groupRepository.findByLeaderId(leaderId).get().getId();

    var group = groupRepository.findById(groupId);

    if(!this.groupRepository.existsById(groupId)) {
      throw new IllegalArgumentException("Group with id " + groupId + " does not exist");
    }
    try {
      groupRepository.delete(group.get());
      tasksEventPublisher.publishGroupDeleted(groupId);
    }catch (Exception e) {
      throw new IllegalArgumentException("Error while deleting group: " + e.getMessage());
    }
  }

  @Override
  public void handle(RemoveMemberFromGroupCommand command) {
    var leaderId = command.leaderId();
    var groupId = groupRepository.findByLeaderId(leaderId)
        .orElseThrow(() -> new IllegalArgumentException("Group not found for leader"))
        .getId();

    var group = groupRepository.findById(groupId)
        .orElseThrow(() -> new IllegalArgumentException("Group with id " + groupId + " does not exist"));

    try {
      group.decreaseMemberCount();
      groupRepository.save(group);

      tasksEventPublisher.publishMemberRemoved(groupId, command.memberId());

    } catch (Exception e) {
      throw new IllegalArgumentException("Error while removing member from group: " + e.getMessage());
    }
  }

  @Override
  public void handle(LeaveGroupCommand command) {
    // 1. Obtener grupo
    var group = groupRepository.findById(command.groupId())
        .orElseThrow(() -> new IllegalArgumentException("Group not found for member"));

    try {
      // 2. Actualizar lógica de dominio
      group.decreaseMemberCount();

      // 3. Persistir cambios
      groupRepository.save(group);

    } catch (Exception e) {
      throw new IllegalArgumentException("Error while member leaving group: " + e.getMessage());
    }
  }
}