package pe.edu.upc.metrics_service.metrics.domain.model.aggregates;

<<<<<<< Updated upstream
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
=======
>>>>>>> Stashed changes
public class GroupMetrics {
    private Long groupId;
    private int memberCount;

<<<<<<< Updated upstream
=======
    public GroupMetrics() {
    }

>>>>>>> Stashed changes
    public GroupMetrics(Long groupId, int memberCount) {
        this.groupId = groupId;
        this.memberCount = memberCount;
    }
<<<<<<< Updated upstream
}
=======

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}

>>>>>>> Stashed changes
