package pe.edu.upc.metrics_service.metrics.domain.model.aggregates;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GroupMetrics {
    private Long groupId;
    private int memberCount;

    public GroupMetrics(Long groupId, int memberCount) {
        this.groupId = groupId;
        this.memberCount = memberCount;
    }
}
