package pe.edu.upc.metrics_service.metrics.application.clients.groups;

import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.GroupIdResource;

import java.util.Optional;

public interface GroupsServiceClient {
    Optional<GroupIdResource> fetchGroupByLeaderId(Long leaderId);
}