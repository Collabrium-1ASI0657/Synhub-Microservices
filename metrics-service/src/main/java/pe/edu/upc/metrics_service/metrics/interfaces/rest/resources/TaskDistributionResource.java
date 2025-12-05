package pe.edu.upc.metrics_service.metrics.interfaces.rest.resources;

import java.util.Map;

public record TaskDistributionResource(
        String type,
        int value,
        Map<String, MemberTaskInfo> details
) {}
