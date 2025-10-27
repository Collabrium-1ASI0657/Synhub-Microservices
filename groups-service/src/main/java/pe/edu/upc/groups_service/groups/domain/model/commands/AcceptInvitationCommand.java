package pe.edu.upc.groups_service.groups.domain.model.commands;

public record AcceptInvitationCommand(
    Long leaderId,
    Long invitationId) {
}