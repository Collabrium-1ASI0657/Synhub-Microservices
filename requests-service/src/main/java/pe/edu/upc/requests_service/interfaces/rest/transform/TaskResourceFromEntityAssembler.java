package pe.edu.upc.requests_service.interfaces.rest.transform;

import pe.edu.upc.requests_service.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.requests_service.application.clients.tasks.resources.TaskDetailsResource;
import pe.edu.upc.requests_service.interfaces.rest.resources.TaskMemberResource;
import pe.edu.upc.requests_service.interfaces.rest.resources.TaskResource;

public class TaskResourceFromEntityAssembler {
    public static TaskResource toResourceFromEntity(TaskDetailsResource resource){
        var memberResource = resource.member() != null
                ? new TaskMemberResource(
                resource.member().id(),
                resource.member().name(),
                resource.member().surname(),
                resource.member().urlImage()
        )
                : null;

        return new TaskResource(
                resource.id(),
                resource.title(),
                resource.description(),
                resource.dueDate(),
                resource.createdAt(),
                resource.updatedAt(),
                resource.status(),
                memberResource,
                resource.groupId()
        );
    }

}
