package pe.edu.upc.requests_service.application.clients.tasks;

import pe.edu.upc.requests_service.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.requests_service.application.clients.tasks.resources.TaskDetailsResource;
import pe.edu.upc.requests_service.application.clients.tasks.resources.TaskSimpleResource;

import java.util.List;
import java.util.Optional;

public interface TaskServiceClient {
    Optional<TaskSimpleResource> fetchTaskById(Long taskId);

    Optional<TaskDetailsResource> fetchTaskDetailsById(Long taskId, String authorizationHeader);

    Optional<MemberWithUserResource> fetchMemberByUsername(String username, String authorizationHeader);

    Optional<MemberWithUserResource> fetchMemberByMemberId(Long memberId, String authorizationHeader);

    List<TaskDetailsResource> fetchAllTasksByGroupId(Long groupId, String authorizationHeader);

    List<TaskDetailsResource> fetchAllTasksByMemberId(Long memberId, String authorizationHeader);
}
