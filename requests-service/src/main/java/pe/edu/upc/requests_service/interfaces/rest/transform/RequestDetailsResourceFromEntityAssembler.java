package pe.edu.upc.requests_service.interfaces.rest.transform;

import pe.edu.upc.requests_service.domain.model.aggregates.Request;
import pe.edu.upc.requests_service.interfaces.rest.resources.RequestDetailsResource;

public class RequestDetailsResourceFromEntityAssembler {
    public static RequestDetailsResource toResourceFromEntity(Request entity) {
        return new RequestDetailsResource(
                entity.getId(),
                entity.getDescription(),
                entity.getRequestType(),
                entity.getRequestStatus(),
                entity.getTaskId()
        );
    }
}
