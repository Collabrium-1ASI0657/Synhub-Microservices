package pe.edu.upc.groups_service.groups.domain.model.commands;

public record CreateInvitationCommand(
    Long groupId,
    String username,
    String authorizationHeader) {
}