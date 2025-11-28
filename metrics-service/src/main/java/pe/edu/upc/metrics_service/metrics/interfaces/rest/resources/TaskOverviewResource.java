package pe.edu.upc.metrics_service.metrics.interfaces.rest.resources;

import java.util.Map;

public record TaskOverviewResource(
        String type,
        int value,
        Map<String, Integer> details
) {}
