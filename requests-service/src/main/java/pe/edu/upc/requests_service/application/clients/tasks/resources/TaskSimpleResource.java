package pe.edu.upc.requests_service.application.clients.tasks.resources;

public record TaskSimpleResource(Long id,
                                  String title,
                                  String description,
                                  String dueDate,
                                  String createdAt,
                                  String updatedAt,
                                  String status,
                                  Long memberId,
                                  Long groupId) {
}
