package pe.edu.upc.groups_service.groups.application.clients.iam;

import pe.edu.upc.groups_service.groups.application.clients.iam.resources.UserResource;

import java.util.Optional;

public interface IamServiceClient {
  Optional<UserResource> fetchUserByUsername(String username, String authorizationHeader);
}
