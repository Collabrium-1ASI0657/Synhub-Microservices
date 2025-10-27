package pe.edu.upc.groups_service.groups.interfaces.rest.resources;

public record CreateGroupResource(
    String name,
    String imgUrl,
    String description) {
}