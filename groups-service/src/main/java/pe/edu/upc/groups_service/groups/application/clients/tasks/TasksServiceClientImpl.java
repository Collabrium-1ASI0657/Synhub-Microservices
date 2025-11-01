package pe.edu.upc.groups_service.groups.application.clients.tasks;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberResource;
import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberWithUserResource;

import java.util.Optional;

@Service
public class TasksServiceClientImpl implements TasksServiceClient {
  private final WebClient webClient;

  public TasksServiceClientImpl(@Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClient) {
    this.webClient = webClient
        .baseUrl("http://tasks-service/api/v1")
        .build();;
  }

  @Override
  public Optional<MemberResource> fetchMemberByMemberId(Long memberId) {
    try {
      var request = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/member/{memberId}")
              .build(memberId));

      MemberResource memberResource = request
          .retrieve()
          .bodyToMono(MemberResource.class)
          .block();

      return Optional.ofNullable(memberResource);

    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return Optional.empty();
      }
    }
    return Optional.empty();
  }

  @Override
  public Optional<MemberWithUserResource> fetchMemberByUsername(String username, String authorizationHeader) {
    try {
      var request = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/member/details")
              .build());

      if (username != null && !username.isBlank()) {
        request = request.header("X-Username", username);
      }

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        request = request.header("Authorization", authorizationHeader);
      }

      MemberWithUserResource memberWithUserResource = request
          .retrieve()
          .bodyToMono(MemberWithUserResource.class)
          .block();

      return Optional.ofNullable(memberWithUserResource);


    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return Optional.empty();
      }
    }
    return Optional.empty();
  }
}
