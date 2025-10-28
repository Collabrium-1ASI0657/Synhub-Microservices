package pe.edu.upc.groups_service.groups.domain.model.commands;

public record CreateGroupCommand(
    String name,
    String imgUrl,
    String description,
    Long leaderId) {
}