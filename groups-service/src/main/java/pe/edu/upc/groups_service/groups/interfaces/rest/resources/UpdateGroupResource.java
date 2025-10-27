package pe.edu.upc.groups_service.groups.interfaces.rest.resources;

public record UpdateGroupResource(
    String name,
    String imgUrl,
    String description) {
}