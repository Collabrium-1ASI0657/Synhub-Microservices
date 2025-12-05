package pe.edu.upc.requests_service.application.clients.tasks.resources;

public record MemberResource(
        Long id,
        String name,
        String surname,
        String urlImage
) {
}