package pe.edu.upc.groups_service.groups.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Group;
import pe.edu.upc.groups_service.groups.domain.model.commands.*;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.GroupCode;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.LeaderId;
import pe.edu.upc.groups_service.groups.domain.services.GroupCommandService;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.LeaderRepository;

import java.util.Optional;

@Service
public class GroupCommandServiceImpl implements GroupCommandService {

  private final GroupRepository groupRepository;
  private final LeaderRepository leaderRepository;

  public GroupCommandServiceImpl(GroupRepository groupRepository, LeaderRepository leaderRepository) {
    this.groupRepository = groupRepository;
    this.leaderRepository = leaderRepository;
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
    var leaderId = new LeaderId(command.leaderId());
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
    var leaderId = new LeaderId(command.leaderId());
    var groupId = groupRepository.findByLeaderId(leaderId).get().getId();

    var group = groupRepository.findById(groupId);

    if(!this.groupRepository.existsById(groupId)) {
      throw new IllegalArgumentException("Group with id " + groupId + " does not exist");
    }
    try {
      groupRepository.delete(group.get());
    }catch (Exception e) {
      throw new IllegalArgumentException("Error while deleting group: " + e.getMessage());
    }
  }

  @Override
  public void handle(RemoveMemberFromGroupCommand command) {
    var leaderId = new LeaderId(command.leaderId());
    var groupId = groupRepository.findByLeaderId(leaderId)
        .orElseThrow(() -> new IllegalArgumentException("Group not found for leader"))
        .getId();

    var group = groupRepository.findById(groupId)
        .orElseThrow(() -> new IllegalArgumentException("Group with id " + groupId + " does not exist"));

//    var member = memberRepository.findById(command.memberId())
//        .orElseThrow(() -> new IllegalArgumentException("Member with id " + command.memberId() + " does not exist"));
//
//    if (!group.getMembers().contains(member)) {
//      throw new IllegalArgumentException("Member with id " + command.memberId() + " does not exist in group with id " + groupId);
//    }
//
//    try {
//      group.getMembers().remove(member);
//      member.setGroup(null);
//      group.setMemberCount(group.getMembers().size());
//      memberRepository.save(member);
//      groupRepository.save(group);
//    } catch (Exception e) {
//      throw new IllegalArgumentException("Error while removing member from group: " + e.getMessage());
//    }
  }

  @Override
  public void handle(LeaveGroupCommand command) {
    var group = groupRepository.findById(command.groupId())
        .orElseThrow(() -> new IllegalArgumentException("Group not found for member"));

//    var member = memberRepository.findById(command.memberId())
//        .orElseThrow(() -> new IllegalArgumentException("Member with id " + command.memberId() + " does not exist"));
//
//    if (!group.getMembers().contains(member)) {
//      throw new IllegalArgumentException("Member with id " + command.memberId() + " does not exist in group with id " + group.getId());
//    }
//
//    try {
//      group.getMembers().remove(member);
//      member.setGroup(null);
//      group.setMemberCount(group.getMembers().size());
//      memberRepository.save(member);
//      groupRepository.save(group);
//    } catch (Exception e) {
//      throw new IllegalArgumentException("Error while removing member from group: " + e.getMessage());
//    }
  }
}