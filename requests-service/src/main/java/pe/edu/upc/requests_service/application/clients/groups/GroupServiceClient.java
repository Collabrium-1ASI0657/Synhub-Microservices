package pe.edu.upc.requests_service.application.clients.groups;

import pe.edu.upc.requests_service.application.clients.groups.resources.GroupResource;
import pe.edu.upc.requests_service.application.clients.groups.resources.LeaderResource;

import java.util.Optional;

public interface GroupServiceClient {
    Optional<LeaderResource> fetchLeaderByUsername(String username, String authorizationHeader);

    Optional<GroupResource> fetchGroupByLeaderId(Long leaderId, String authorizationHeader);
}
