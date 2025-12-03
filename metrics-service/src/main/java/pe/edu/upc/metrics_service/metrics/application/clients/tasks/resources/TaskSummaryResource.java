package pe.edu.upc.metrics_service.metrics.application.clients.tasks.resources;

public record TaskSummaryResource(
        Long id,
        Long memberId,
        Long groupId,
        String status,
        Boolean rescheduled,
        String createdAt,
        String updatedAt,
        Integer rearrangedCount
) {}
