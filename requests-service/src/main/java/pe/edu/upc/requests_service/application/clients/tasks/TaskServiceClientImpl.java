package pe.edu.upc.requests_service.application.clients.tasks;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.edu.upc.requests_service.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.requests_service.application.clients.tasks.resources.TaskDetailsResource;
import pe.edu.upc.requests_service.application.clients.tasks.resources.TaskSimpleResource;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceClientImpl implements TaskServiceClient{
    private final WebClient webClient;

    public TaskServiceClientImpl(@Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClient) {
        this.webClient = webClient
                .baseUrl("http://tasks-service/api/v1")
                .build();
    }

    @Override
    public Optional<TaskSimpleResource> fetchTaskById(Long taskId) {
        try {
            var request = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/tasks/details/{taskId}")
                            .build(taskId));


            TaskSimpleResource taskResource = request
                    .retrieve()
                    .bodyToMono(TaskSimpleResource.class)
                    .block();

            return Optional.ofNullable(taskResource);
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<TaskDetailsResource> fetchTaskDetailsById(Long taskId, String authorizationHeader) {
        try {
            var request = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/tasks/{taskId}")
                            .build(taskId));

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                request = request.header("Authorization", authorizationHeader);
            }

            TaskDetailsResource taskDetailsResource = request
                    .retrieve()
                    .bodyToMono(TaskDetailsResource.class)
                    .block();

            return Optional.ofNullable(taskDetailsResource);
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
    public Optional<MemberWithUserResource> fetchMemberByMemberId(Long memberId, String authorizationHeader) {
        try {
            var request = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/member/details/{memberId}")
                            .build(memberId));

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                request = request.header("Authorization", authorizationHeader);
            }

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
    public List<TaskDetailsResource> fetchAllTasksByGroupId(Long groupId, String authorizationHeader) {
        try {
            var request = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/tasks")
                            .queryParam("groupId", groupId)
                            .build());

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                request = request.header("Authorization", authorizationHeader);
            }

            List<TaskDetailsResource> taskDetailsResource = request
                    .retrieve()
                    .bodyToFlux(TaskDetailsResource.class)
                    .collectList()
                    .block();

            return taskDetailsResource != null ? taskDetailsResource : List.of();

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return List.of();
            }
        }
        return List.of();
    }

    @Override
    public List<TaskDetailsResource> fetchAllTasksByMemberId(Long memberId, String authorizationHeader) {
        try {
            var request = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/members/{memberId}/tasks")
                            .build(memberId));

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                request = request.header("Authorization", authorizationHeader);
            }

            List<TaskDetailsResource> taskDetailsResource = request
                    .retrieve()
                    .bodyToFlux(TaskDetailsResource.class)
                    .collectList()
                    .block();

            return taskDetailsResource != null ? taskDetailsResource : List.of();

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return List.of();
            }
        }
        return List.of();
    }

}
