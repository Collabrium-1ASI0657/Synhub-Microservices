package pe.edu.upc.metrics_service.metrics.application.clients.tasks;

import pe.edu.upc.metrics_service.metrics.application.clients.tasks.resources.MemberWithTasksResource;
import pe.edu.upc.metrics_service.metrics.application.clients.tasks.resources.TaskSummaryResource;

import java.util.List;

public interface TasksServiceClient {

    List<MemberWithTasksResource> fetchMembersByGroupId(Long groupId, String authorizationHeader);

    List<TaskSummaryResource> fetchTasksByMemberId(Long memberId, String authorizationHeader);

    List<TaskSummaryResource> fetchTasksByGroupId(Long groupId, String authorizationHeader);


}
