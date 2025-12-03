package pe.edu.upc.metrics_service.metrics.infrastructure.persistenence.jpa.repositories;

import pe.edu.upc.metrics_service.metrics.domain.model.aggregates.GroupMetrics;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupMetricsRepository {
    Optional<GroupMetrics> getGroupMetrics(Long groupId);
}
