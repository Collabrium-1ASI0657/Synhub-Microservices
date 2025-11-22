package pe.edu.upc.requests_service.interfaces.rest.transform;

import pe.edu.upc.requests_service.domain.model.commands.CreateRequestCommand;
import pe.edu.upc.requests_service.interfaces.rest.resources.CreateRequestResource;

public class CreateRequestCommandFromResourceAssembler {
    public static CreateRequestCommand toCommandFromResource(CreateRequestResource resource, Long taskId) {
        return new CreateRequestCommand(
                resource.description(),
                resource.requestType(),
                taskId
        );
    }
}