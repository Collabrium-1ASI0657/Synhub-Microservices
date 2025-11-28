package pe.edu.upc.metrics_service.metrics.application.clients.groups;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.GroupIdResource;

import java.util.Optional;

@Service
public class GroupsServiceClientImpl implements GroupsServiceClient {

    private final WebClient webClient;

    public GroupsServiceClientImpl(
            @Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClientBuilder
    ) {
        this.webClient = webClientBuilder
                .baseUrl("http://groups-service/api/v1")
                .build();
    }

    @Override
    public Optional<GroupIdResource> fetchGroupByLeaderId(Long leaderId) {
        try {
            var request = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/groups")
                            .queryParam("leaderId", leaderId)
                            .build());

            GroupIdResource resource = request
                    .retrieve()
                    .bodyToMono(GroupIdResource.class)
                    .block();

            return Optional.ofNullable(resource);

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }
}