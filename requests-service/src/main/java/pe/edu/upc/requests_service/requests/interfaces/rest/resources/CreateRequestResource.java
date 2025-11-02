package pe.edu.upc.requests_service.requests.interfaces.rest.resources;

public record CreateRequestResource(
        String description,
        String requestType
) {
}