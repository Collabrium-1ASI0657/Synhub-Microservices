package pe.edu.upc.requests_service.application.clients.groups.resources;

public record GroupResource(
        Long id,
        String name,
        String imgUrl,
        String description,
        String code,
        Integer memberCount
) {
}
