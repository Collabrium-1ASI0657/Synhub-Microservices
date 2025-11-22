package pe.edu.upc.requests_service.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.requests_service.domain.model.commands.DeleteRequestCommand;
import pe.edu.upc.requests_service.domain.model.queries.GetRequestsByTaskIdQuery;
import pe.edu.upc.requests_service.domain.services.RequestCommandService;
import pe.edu.upc.requests_service.domain.services.RequestQueryService;
import pe.edu.upc.requests_service.interfaces.rest.resources.RequestResource;
import pe.edu.upc.requests_service.interfaces.rest.transform.RequestResourceFromEntityAssembler;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
@RestController
@RequestMapping(value = "/api/v1/tasks/{taskId}/requests", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Request", description = "Request management API")
public class RequestController {
    private final RequestCommandService requestCommandService;
    private final RequestQueryService requestQueryService;

    public RequestController(RequestCommandService requestCommandService, RequestQueryService requestQueryService) {
        this.requestCommandService = requestCommandService;
        this.requestQueryService = requestQueryService;
    }

    @DeleteMapping("/{requestId}")
    @Operation(summary = "Delete a request by id", description = "Delete a request by id")
    public ResponseEntity<Void> deleteRequestById(@PathVariable Long taskId, @PathVariable Long requestId) {
        var deleteRequestCommand = new DeleteRequestCommand(requestId);
        this.requestCommandService.handle(deleteRequestCommand);
        return ResponseEntity.noContent().build();
    }
}
