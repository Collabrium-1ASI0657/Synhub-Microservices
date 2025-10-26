package pe.edu.upc.groups_service.groups.domain.model.commands;

public record CreateInvitationCommand(
    Long memberId,
    Long groupId) {
}