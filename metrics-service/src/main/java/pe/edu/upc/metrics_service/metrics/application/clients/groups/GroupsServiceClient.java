package pe.edu.upc.metrics_service.metrics.application.clients.groups;

<<<<<<< Updated upstream
import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.GroupIdResource;

import java.util.Optional;

public interface GroupsServiceClient {
    Optional<GroupIdResource> fetchGroupByLeaderId(Long leaderId);
=======
import java.util.Optional;
import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.GroupDetailsResource;

public interface GroupsServiceClient {
    Optional<GroupDetailsResource> fetchGroupByGroupId(Long groupId);
>>>>>>> Stashed changes
}