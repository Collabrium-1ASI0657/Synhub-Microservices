package pe.edu.upc.tasks_service.tasks.interfaces.rest.resources;

public record TaskResource(Long id,
                           String title,
                           String description,
                           String dueDate,
                           String createdAt,
                           String updatedAt,
                           String status,
                           TaskMemberResource member,
                           Long groupId) {
}
