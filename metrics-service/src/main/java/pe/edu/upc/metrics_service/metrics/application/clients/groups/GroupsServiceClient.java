package pe.edu.upc.metrics_service.metrics.application.clients.groups;

import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.GroupDetailsResource;

import java.util.Optional;

public interface GroupsServiceClient {
    Optional<GroupDetailsResource> fetchGroupByLeaderId(Long leaderId);
}