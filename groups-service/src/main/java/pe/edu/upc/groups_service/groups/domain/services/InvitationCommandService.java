package pe.edu.upc.groups_service.groups.domain.services;

import pe.edu.upc.groups_service.groups.domain.model.aggregates.Invitation;
import pe.edu.upc.groups_service.groups.domain.model.commands.AcceptInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.CancelInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.CreateInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.RejectInvitationCommand;

import java.util.Optional;

public interface InvitationCommandService {
  Optional<Invitation> handle(CreateInvitationCommand command);
  void handle(RejectInvitationCommand command);
  void handle(CancelInvitationCommand command);
  void handle(AcceptInvitationCommand command);
}