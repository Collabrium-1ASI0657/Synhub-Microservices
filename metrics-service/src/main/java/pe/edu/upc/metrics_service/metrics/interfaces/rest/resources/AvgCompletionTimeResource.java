package pe.edu.upc.metrics_service.metrics.interfaces.rest.resources;

import java.util.Map;

public record AvgCompletionTimeResource(
        String type,
        double value,
        Map<String, Integer> details
) {}
