package pe.edu.upc.requests_service.interfaces.rest.resources;

import pe.edu.upc.requests_service.domain.model.valueobjects.TaskId;

public record RequestDetailsResource(
        Long id,
        String description,
        String requestType,
        String requestStatus,
        TaskId taskId
) {
}
