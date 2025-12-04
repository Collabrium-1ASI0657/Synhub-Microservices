package pe.edu.upc.requests_service.interfaces.rest.resources;

public record RequestResource(
        Long id,
        String description,
        String requestType,
        String requestStatus,
        TaskResource task
) {
}
