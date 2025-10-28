package pe.edu.upc.groups_service.groups.domain.model.commands;

public record CancelInvitationCommand(
    Long memberId,
    Long invitationId) {
}