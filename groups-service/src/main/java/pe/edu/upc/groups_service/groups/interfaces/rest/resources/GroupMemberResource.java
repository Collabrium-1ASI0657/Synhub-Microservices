package pe.edu.upc.groups_service.groups.interfaces.rest.resources;

public record GroupMemberResource(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl) {
}