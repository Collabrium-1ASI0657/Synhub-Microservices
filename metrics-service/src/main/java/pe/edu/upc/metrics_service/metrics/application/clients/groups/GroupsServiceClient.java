package pe.edu.upc.metrics_service.metrics.application.clients.groups;

import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.GroupDetailsResource;
import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.LeaderResource;

import java.util.Optional;

public interface GroupsServiceClient {
    Optional<GroupDetailsResource> fetchGroupByLeaderId(Long leaderId, String authorizationHeader);

    Optional<LeaderResource> fetchLeaderByAuthentication(String username, String authorizationHeader);
}