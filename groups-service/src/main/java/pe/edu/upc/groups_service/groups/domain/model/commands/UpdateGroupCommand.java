package pe.edu.upc.groups_service.groups.domain.model.commands;

public record UpdateGroupCommand(
    Long leaderId,
    String name,
    String description,
    String imgUrl) {
}