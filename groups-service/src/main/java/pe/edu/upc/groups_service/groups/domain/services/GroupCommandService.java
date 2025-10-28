package pe.edu.upc.groups_service.groups.domain.services;

import pe.edu.upc.groups_service.groups.domain.model.aggregates.Group;
import pe.edu.upc.groups_service.groups.domain.model.commands.*;

import java.util.Optional;

public interface GroupCommandService {
  Optional<Group> handle(CreateGroupCommand command);
  Optional<Group> handle(UpdateGroupCommand command);
  void handle(DeleteGroupCommand command);
  void handle(RemoveMemberFromGroupCommand command);
  void handle(LeaveGroupCommand command);
}