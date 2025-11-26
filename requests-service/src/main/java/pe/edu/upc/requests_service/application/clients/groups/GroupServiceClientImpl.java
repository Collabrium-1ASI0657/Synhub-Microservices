package pe.edu.upc.requests_service.application.clients.groups;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.upc.requests_service.application.clients.groups.resources.GroupResource;
import pe.edu.upc.requests_service.application.clients.groups.resources.LeaderResource;

import java.util.Optional;

@Service
public class GroupServiceClientImpl implements GroupServiceClient {
    private final WebClient webClient;

    public GroupServiceClientImpl(@Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClient) {
        this.webClient = webClient
                .baseUrl("http://groups-service/api/v1")
                .build();
    }

    @Override
    public Optional<LeaderResource> fetchLeaderByUsername(String username, String authorizationHeader) {
        try {
            var request = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/leader/group")
                            .build());

            if (username != null && !username.isBlank()) {
                request = request.header("X-Username", username);
            }

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                request = request.header("Authorization", authorizationHeader);
            }

            LeaderResource leaderResource = request
                    .retrieve()
                    .bodyToMono(LeaderResource.class)
                    .block();

            return Optional.ofNullable(leaderResource);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<GroupResource> fetchGroupByLeaderId(Long leaderId, String authorizationHeader) {
        try {
            var request = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/group/leader")
                            .build(leaderId));

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                request = request.header("Authorization", authorizationHeader);
            }

            GroupResource groupResource = request
                    .retrieve()
                    .bodyToMono(GroupResource.class)
                    .block();

            return Optional.ofNullable(groupResource);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
