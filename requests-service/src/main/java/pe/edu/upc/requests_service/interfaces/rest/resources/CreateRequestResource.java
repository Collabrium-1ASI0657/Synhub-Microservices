package pe.edu.upc.requests_service.interfaces.rest.resources;

public record CreateRequestResource(
        String description,
        String requestType
) {
}