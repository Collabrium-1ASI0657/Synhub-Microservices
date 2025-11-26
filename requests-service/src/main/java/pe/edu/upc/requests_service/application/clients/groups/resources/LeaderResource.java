package pe.edu.upc.requests_service.application.clients.groups.resources;

public record LeaderResource(
        Long id,
        String username,
        String name,
        String surname,
        String imgUrl,
        String email,
        String averageSolutionTime,
        Integer solvedRequests
) {
}
