package pe.edu.upc.groups_service.groups.domain.model.commands;

public record RejectInvitationCommand(
    Long leaderId,
    Long invitationId) {
}