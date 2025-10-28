package pe.edu.upc.groups_service.groups.interfaces.rest.transform;

import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.LeaderDetailsResource;

public class LeaderDetailsResourceFromEntityAssembler {
  public static LeaderDetailsResource toResourceFromEntity(Leader entity) {
    return new LeaderDetailsResource(
        entity.getId(),
        entity.getAverageSolutionTime().toString(),
        entity.getSolvedRequests());
  }
}
