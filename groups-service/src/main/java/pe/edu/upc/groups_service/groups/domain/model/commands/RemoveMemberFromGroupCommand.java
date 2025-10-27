package pe.edu.upc.groups_service.groups.domain.model.commands;

public record RemoveMemberFromGroupCommand(
    Long leaderId,
    Long memberId) {
}