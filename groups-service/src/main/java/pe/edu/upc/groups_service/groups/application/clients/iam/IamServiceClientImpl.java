package pe.edu.upc.groups_service.groups.application.clients.iam;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.edu.upc.groups_service.groups.application.clients.iam.resources.UserResource;

import java.util.Optional;

@Service
public class IamServiceClientImpl implements IamServiceClient {
  private final WebClient webClient;

  public IamServiceClientImpl(@Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder
        .baseUrl("http://iam-service/api/v1")
        .build();
  }

  @Override
  public Optional<UserResource> fetchUserByUsername(String username) {
    try {
      UserResource resource = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/users")
              .queryParam("username", username)
              .build())
          .retrieve()
          .bodyToMono(UserResource.class)
          .block();
      return Optional.ofNullable(resource);

    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return Optional.empty();
      }
      throw new IllegalArgumentException("IAM Service unavailable");
    }
  }
}
