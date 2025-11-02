package pe.edu.upc.requests_service.requests.application.clients.tasks.resources;

public record TaskResource(
        Long id,
        String description,
        String status,
        String dueDate,
        Long memberId,
        Long groupId,
        Integer timesRearranged,
        Long timePassed
) {
}
