package pe.edu.upc.requests_service.interfaces.rest.transform;

import pe.edu.upc.requests_service.domain.model.commands.UpdateRequestCommand;

public class UpdateRequestCommandFromResourceAssembler {
    public static UpdateRequestCommand toCommandFromResource(Long requestId, String status) {
        return new UpdateRequestCommand(requestId, status);
    }
}