package pe.edu.upc.requests_service.application.clients.tasks;

import pe.edu.upc.requests_service.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.requests_service.application.clients.tasks.resources.TaskDetailsResource;

import java.util.Optional;

public interface TaskServiceClient {
    Optional<TaskDetailsResource> fetchTaskDetailsById(Long taskId);

    Optional<MemberWithUserResource> fetchMemberByUsername(String username, String authorizationHeader);
}
