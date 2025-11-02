package pe.edu.upc.requests_service.requests.interfaces.rest.resources;

import nrg.inc.synhubbackend.tasks.interfaces.rest.resources.TaskResource;

public record RequestResource(
        Long id,
        String description,
        String requestType,
        String requestStatus,
        TaskResource task
) {
}
