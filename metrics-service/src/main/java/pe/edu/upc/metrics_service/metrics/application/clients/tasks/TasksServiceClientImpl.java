package pe.edu.upc.metrics_service.metrics.application.clients.tasks;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.edu.upc.metrics_service.metrics.application.clients.tasks.resources.MemberWithTasksResource;
import pe.edu.upc.metrics_service.metrics.application.clients.tasks.resources.TaskSummaryResource;

import java.util.Collections;
import java.util.List;

@Service
public class TasksServiceClientImpl implements TasksServiceClient {

    private final WebClient webClient;

    public TasksServiceClientImpl(
            @Qualifier("loadBalancedWebClientBuilder") WebClient.Builder builder
    ) {
        this.webClient = builder
                .baseUrl("http://tasks-service/api/v1")
                .build();
    }

    @Override
    public List<MemberWithTasksResource> fetchMembersByGroupId(Long groupId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/members")
                            .queryParam("groupId", groupId)
                            .build())
                    .retrieve()
                    .bodyToFlux(MemberWithTasksResource.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Collections.emptyList();
            }
            throw e;
        }
    }

    @Override
    public List<TaskSummaryResource> fetchTasksByMemberId(Long memberId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/tasks")
                            .queryParam("memberId", memberId)
                            .build())
                    .retrieve()
                    .bodyToFlux(TaskSummaryResource.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Collections.emptyList();
            }
            throw e;
        }
    }

    @Override
    public List<TaskSummaryResource> fetchTasksByGroupId(Long groupId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/tasks")
                            .queryParam("groupId", groupId)
                            .build())
                    .retrieve()
                    .bodyToFlux(TaskSummaryResource.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Collections.emptyList();
            }
            throw e;
        }
    }
}
