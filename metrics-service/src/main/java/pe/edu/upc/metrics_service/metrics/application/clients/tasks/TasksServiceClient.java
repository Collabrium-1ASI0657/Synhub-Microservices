package pe.edu.upc.metrics_service.metrics.application.clients.tasks;

import pe.edu.upc.metrics_service.metrics.application.clients.tasks.resources.MemberWithTasksResource;

import java.util.List;

public interface TasksServiceClient {

    List<MemberWithTasksResource> fetchMembersByGroupId(Long groupId);
}