package pe.edu.upc.metrics_service.metrics.interfaces.rest.resources;

import java.util.List;
import java.util.Map;

public record RescheduledTasksResource(
        String type,
        long value,
        Map<String, Integer> details,
        List<Long> rescheduledMemberIds
) {}
