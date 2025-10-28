package pe.edu.upc.groups_service.groups.interfaces.rest.resources;

public record LeaderDetailsResource(Long id,
                                    String averageSolutionTime,
                                    Integer solvedRequests) {
}
