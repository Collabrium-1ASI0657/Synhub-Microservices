package pe.edu.upc.groups_service.groups.interfaces.rest.transform;

import pe.edu.upc.groups_service.groups.application.dto.LeaderWithUserInfo;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.LeaderResource;

public class LeaderResourceFromEntityAssembler {
  public static LeaderResource toResourceFromEntity(LeaderWithUserInfo entity) {
    var leader = entity.leader();
    var user = entity.user();

    return new LeaderResource(
        leader.getId(),
        user.username(),
        user.name(),
        user.surname(),
        user.imgUrl(),
        user.email(),
        leader.getAverageSolutionTime().toString(),
        leader.getSolvedRequests()
    );
  }
}