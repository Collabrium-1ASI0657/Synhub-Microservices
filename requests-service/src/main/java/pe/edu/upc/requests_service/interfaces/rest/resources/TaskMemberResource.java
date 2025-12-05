package pe.edu.upc.requests_service.interfaces.rest.resources;

public record TaskMemberResource(
        Long id,
        String name,
        String surname,
        String urlImage
) {
}
