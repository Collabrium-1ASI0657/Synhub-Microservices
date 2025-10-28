package pe.edu.upc.iam_service.iam.interfaces.rest.transform;

import pe.edu.upc.iam_service.iam.application.internal.clients.groups.resources.UserWithLeaderResource;
import pe.edu.upc.iam_service.iam.domain.model.aggregates.User;
import pe.edu.upc.iam_service.iam.domain.model.entities.Role;
import pe.edu.upc.iam_service.iam.interfaces.rest.resources.UserLeaderResource;
import pe.edu.upc.iam_service.iam.interfaces.rest.resources.UserMemberResource;
import pe.edu.upc.iam_service.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

  public static UserResource toResourceFromEntity(User user) {
    var roles = user.getRoles().stream()
        .map(Role::getStringName)
        .toList();

    UserLeaderResource leaderResource = user.getLeaderId() != null ?
        new UserLeaderResource(
            user.getLeaderId().value(),
            "string",
            0) : null;

    UserMemberResource memberResource = user.getMemberId() != null ?
        new UserMemberResource(user.getMemberId().value(),
            0L) : null;

    return new UserResource(
        user.getId(),
        user.getUsername(),
        user.getName(),
        user.getSurname(),
        user.getImgUrl(),
        user.getEmail(),
        leaderResource,
        memberResource,
        roles
    );
  }

  public static UserResource toResourceFromLeaderDTO(UserWithLeaderResource dto){
    var user = dto.user();
    var roles = user.getRoles().stream()
        .map(Role::getStringName)
        .toList();
    var leader = dto.leader();

    UserLeaderResource leaderResource = new UserLeaderResource(
        leader.id(),
        leader.averageSolutionTime(),
        leader.solvedRequests()
    );

    return new UserResource(
        user.getId(),
        user.getUsername(),
        user.getName(),
        user.getSurname(),
        user.getImgUrl(),
        user.getEmail(),
        leaderResource,
        null,
        roles
    );
  }
}