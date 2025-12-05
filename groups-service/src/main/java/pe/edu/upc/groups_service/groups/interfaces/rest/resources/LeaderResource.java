package pe.edu.upc.groups_service.groups.interfaces.rest.resources;

public record LeaderResource(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl,
    String email,
    String averageSolutionTime,
    Integer solvedRequests) {
}