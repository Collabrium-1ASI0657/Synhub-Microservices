package pe.edu.upc.requests_service.interfaces.rest.resources;

public record TaskResource(
        Long id,
        String title,
        String description,
        String dueDate,
        String createdAt,
        String updatedAt,
        String status,
        TaskMemberResource member,
        Long groupId
) {
}
