package pe.edu.upc.groups_service.groups.domain.model.commands;

public record LeaveGroupCommand(
    Long memberId,
    Long groupId) {
}
