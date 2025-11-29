package pe.edu.upc.metrics_service.metrics.domain.model.repositories;

import pe.edu.upc.metrics_service.metrics.domain.model.aggregates.GroupMetrics;

import java.util.Optional;

public interface GroupMetricsRepository {

    Optional<GroupMetrics> getGroupMetrics(Long groupId);
}

