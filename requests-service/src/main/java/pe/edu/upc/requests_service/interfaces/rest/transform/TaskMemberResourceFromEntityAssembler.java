package pe.edu.upc.requests_service.interfaces.rest.transform;

import pe.edu.upc.requests_service.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.requests_service.interfaces.rest.resources.TaskMemberResource;

public class TaskMemberResourceFromEntityAssembler {
    public static TaskMemberResource toResourceFromEntity(MemberWithUserResource resource){
        return new TaskMemberResource(
                resource.id(),
                resource.name(),
                resource.surname(),
                resource.imgUrl()
        );
    }
}
