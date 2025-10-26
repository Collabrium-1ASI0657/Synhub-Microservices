package pe.edu.upc.iam_service.iam.interfaces.rest.transform;

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

    // Crear instancias de los recursos
    UserLeaderResource leaderResource = user.getLeaderId() != null ?
        new UserLeaderResource(user.getLeaderId().value()) : null;

    UserMemberResource memberResource = user.getMemberId() != null ?
        new UserMemberResource(user.getMemberId().value()) : null;

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
}