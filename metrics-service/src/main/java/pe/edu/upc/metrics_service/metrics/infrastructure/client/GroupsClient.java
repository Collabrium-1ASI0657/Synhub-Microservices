package pe.edu.upc.metrics_service.metrics.infrastructure.client;

<<<<<<< Updated upstream
import nrg.inc.synhubbackend.groups.domain.model.queries.GetGroupByIdQuery;
import nrg.inc.synhubbackend.groups.domain.services.GroupQueryService;
import nrg.inc.synhubbackend.metrics.domain.model.aggregates.GroupMetrics;
import nrg.inc.synhubbackend.metrics.infrastructure.persistenence.jpa.repositories.GroupMetricsRepository;
import org.springframework.stereotype.Repository;
=======
import org.springframework.stereotype.Repository;
import pe.edu.upc.metrics_service.metrics.application.clients.groups.GroupsServiceClient;
import pe.edu.upc.metrics_service.metrics.domain.model.aggregates.GroupMetrics;
import pe.edu.upc.metrics_service.metrics.domain.model.repositories.GroupMetricsRepository;
>>>>>>> Stashed changes

import java.util.Optional;

@Repository
public class GroupsClient implements GroupMetricsRepository {

<<<<<<< Updated upstream
    private final GroupQueryService groupQueryService;

    public GroupsClient(GroupQueryService groupQueryService) {
        this.groupQueryService = groupQueryService;
=======
    private final GroupsServiceClient groupsServiceClient;

    public GroupsClient(GroupsServiceClient groupsServiceClient) {
        this.groupsServiceClient = groupsServiceClient;
>>>>>>> Stashed changes
    }

    @Override
    public Optional<GroupMetrics> getGroupMetrics(Long groupId) {
<<<<<<< Updated upstream
        return groupQueryService.handle(new GetGroupByIdQuery(groupId))
                .map(group -> new GroupMetrics(groupId, group.getMemberCount()));
    }
}
=======
        return groupsServiceClient.fetchGroupByGroupId(groupId)
                .map(groupDetails -> new GroupMetrics(groupId, groupDetails.memberCount()));
    }
}

>>>>>>> Stashed changes
