package pe.edu.upc.requests_service.interfaces.rest.transform;

import pe.edu.upc.requests_service.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.requests_service.application.clients.tasks.resources.TaskDetailsResource;
import pe.edu.upc.requests_service.domain.model.aggregates.Request;
import pe.edu.upc.requests_service.interfaces.rest.resources.RequestResource;

public class RequestResourceFromEntityAssembler {
    public static RequestResource toResourceFromEntity(
            Request entity,
            TaskDetailsResource taskDetailsResource) {
        return new RequestResource(
                entity.getId(),
                entity.getDescription(),
                entity.getRequestType(),
                entity.getRequestStatus(),
                TaskResourceFromEntityAssembler.toResourceFromEntity(taskDetailsResource)
        );

    }
}
