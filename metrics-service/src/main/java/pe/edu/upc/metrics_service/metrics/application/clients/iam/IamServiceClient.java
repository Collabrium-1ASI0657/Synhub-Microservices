package pe.edu.upc.metrics_service.metrics.application.clients.iam;

import pe.edu.upc.metrics_service.metrics.application.clients.iam.resources.UserResource;

import java.util.Optional;

public interface IamServiceClient {
  Optional<UserResource> fetchUserByUsername(String username, String authorizationHeader);

  Optional<UserResource> fetchUserByMemberId(Long memberId, String authorizationHeader);
}