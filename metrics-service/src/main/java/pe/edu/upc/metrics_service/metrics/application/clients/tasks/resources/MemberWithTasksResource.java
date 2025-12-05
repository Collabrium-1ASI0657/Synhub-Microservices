package pe.edu.upc.metrics_service.metrics.application.clients.tasks.resources;

import java.util.List;

public record MemberWithTasksResource(
        Long id,
        Long groupId,
        List<TaskSummaryResource> tasks
) {}
