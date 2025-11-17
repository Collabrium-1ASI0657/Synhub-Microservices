package pe.edu.upc.groups_service.groups.application.clients.tasks;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.TaskResource;

import java.util.List;
import java.util.Optional;

@Service
public class TasksServiceClientImpl implements TasksServiceClient {
  private final WebClient webClient;

  public TasksServiceClientImpl(@Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClient) {
    this.webClient = webClient
        .baseUrl("http://tasks-service/api/v1")
        .build();
  }

  @Override
  public Optional<MemberWithUserResource> fetchMemberByMemberId(Long memberId) {
    try {
      var request = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/member/details/{memberId}")
              .build(memberId));

      MemberWithUserResource memberResource = request
          .retrieve()
          .bodyToMono(MemberWithUserResource.class)
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

  @Override
  public List<MemberWithUserResource> fetchMembersByGroupId(Long groupId, String authorizationHeader) {
    try {
      var request = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/member")
              .queryParam("groupId", groupId)
              .build());

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        request = request.header("Authorization", authorizationHeader);
      }

      // Llamamos al endpoint y esperamos una lista de MemberWithUserResource
      List<MemberWithUserResource> members = request
          .retrieve()
          .bodyToFlux(MemberWithUserResource.class) // recibe un flujo (array JSON)
          .collectList() // lo convierte a List<>
          .block();

      return members;

    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return List.of(); // lista vacía
      }
      throw e; // relanzamos otras excepciones
    } catch (Exception e) {
      // En caso de error inesperado (timeout, conexión, etc.)
      e.printStackTrace();
      return List.of();
    }
  }

  @Override
  public List<TaskResource> fetchTasksByGroupId(Long groupId, String authorizationHeader) {
    try {
      var request = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/tasks")
              .queryParam("groupId", groupId)
              .build());

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        request = request.header("Authorization", authorizationHeader);
      }

      List<TaskResource> tasks = request
          .retrieve()
          .bodyToFlux(TaskResource.class)
          .collectList()
          .block();

      return tasks;

    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        return List.of(); // lista vacía
      }
      throw e; // relanzamos otras excepciones
    } catch (Exception e) {
      // En caso de error inesperado (timeout, conexión, etc.)
      e.printStackTrace();
      return List.of();
    }
  }
}
