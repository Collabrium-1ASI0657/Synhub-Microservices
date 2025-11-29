package pe.edu.upc.metrics_service.metrics.application.clients.groups;

<<<<<<< Updated upstream
=======
import java.util.Optional;

>>>>>>> Stashed changes
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
<<<<<<< Updated upstream
import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.GroupIdResource;

import java.util.Optional;
=======
import pe.edu.upc.metrics_service.metrics.application.clients.groups.resources.GroupDetailsResource;
>>>>>>> Stashed changes

@Service
public class GroupsServiceClientImpl implements GroupsServiceClient {

    private final WebClient webClient;

    public GroupsServiceClientImpl(
<<<<<<< Updated upstream
            @Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClientBuilder
    ) {
=======
            @Qualifier("loadBalancedWebClientBuilder") WebClient.Builder webClientBuilder) {

>>>>>>> Stashed changes
        this.webClient = webClientBuilder
                .baseUrl("http://groups-service/api/v1")
                .build();
    }

    @Override
<<<<<<< Updated upstream
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
=======
    public Optional<GroupDetailsResource> fetchGroupByGroupId(Long groupId) {
        try {
            GroupDetailsResource groupDetails = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/groups/{groupId}")
                            .build(groupId))
                    .retrieve()
                    .bodyToMono(GroupDetailsResource.class)
                    .block();

            return Optional.ofNullable(groupDetails);
        } catch (WebClientResponseException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                return Optional.empty();
            }
            throw ex;
>>>>>>> Stashed changes
        }
    }
}