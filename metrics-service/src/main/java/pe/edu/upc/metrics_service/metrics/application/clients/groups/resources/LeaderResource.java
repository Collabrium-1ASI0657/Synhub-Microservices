package pe.edu.upc.metrics_service.metrics.application.clients.groups.resources;

public record LeaderResource(Long id,
                             String averageSolutionTime,
                             Integer solvedRequests) {
}
