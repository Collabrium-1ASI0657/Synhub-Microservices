package pe.edu.upc.metrics_service.metrics.application.clients.groups;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.GroupDetailsResource;
import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.LeaderResource;

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
    public Optional<GroupDetailsResource> fetchGroupByLeaderId(Long leaderId, String authorizationHeader) {
        try {
            var request = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/groups")
                            .queryParam("leaderId", leaderId)
                            .build());

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                request = request.header("Authorization", authorizationHeader);
            }

            GroupDetailsResource resource = request
                    .retrieve()
                    .bodyToMono(GroupDetailsResource.class)

                    .block();

            return Optional.ofNullable(resource);

        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw e;
        }
    }

    @Override
    public Optional<LeaderResource> fetchLeaderByAuthentication(String username, String authorizationHeader) {
        try {
            var request = webClient.get()
                    .uri("/leader/details")
                    .header("X-Username", username);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                request = request.header("Authorization", authorizationHeader);
            }

            LeaderResource resource = request
                    .retrieve()
                    .bodyToMono(LeaderResource.class)
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
