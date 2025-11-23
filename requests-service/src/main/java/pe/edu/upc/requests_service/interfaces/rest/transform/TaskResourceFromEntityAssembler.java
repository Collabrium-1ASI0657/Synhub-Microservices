package pe.edu.upc.requests_service.interfaces.rest.transform;

import pe.edu.upc.requests_service.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.requests_service.application.clients.tasks.resources.TaskDetailsResource;
import pe.edu.upc.requests_service.interfaces.rest.resources.TaskResource;

public class TaskResourceFromEntityAssembler {
    public static TaskResource toResourceFromEntity(TaskDetailsResource resource, MemberWithUserResource memberWithUserResource){
        return new TaskResource(
                resource.id(),
                resource.title(),
                resource.description(),
                resource.dueDate(),
                resource.createdAt(),
                resource.updatedAt(),
                resource.status(),
                TaskMemberResourceFromEntityAssembler.toResourceFromEntity(memberWithUserResource),
                resource.groupId()
        );
    }

}
