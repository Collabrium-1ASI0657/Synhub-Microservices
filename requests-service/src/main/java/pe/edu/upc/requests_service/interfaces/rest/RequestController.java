package pe.edu.upc.requests_service.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.requests_service.application.clients.tasks.TaskServiceClient;
import pe.edu.upc.requests_service.domain.model.commands.DeleteRequestCommand;
import pe.edu.upc.requests_service.domain.model.queries.GetRequestByIdQuery;
import pe.edu.upc.requests_service.domain.model.queries.GetRequestsByTaskIdQuery;
import pe.edu.upc.requests_service.domain.services.RequestCommandService;
import pe.edu.upc.requests_service.domain.services.RequestQueryService;
import pe.edu.upc.requests_service.interfaces.rest.resources.CreateRequestResource;
import pe.edu.upc.requests_service.interfaces.rest.resources.RequestResource;
import pe.edu.upc.requests_service.interfaces.rest.transform.CreateRequestCommandFromResourceAssembler;
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
    private final TaskServiceClient taskServiceClient;

    public RequestController(RequestCommandService requestCommandService, RequestQueryService requestQueryService, TaskServiceClient taskServiceClient) {
        this.requestCommandService = requestCommandService;
        this.requestQueryService = requestQueryService;
        this.taskServiceClient = taskServiceClient;
    }

    @PostMapping
    @Operation(summary = "Create a new request", description = "Create a new request")
    public ResponseEntity<RequestResource> createRequest(@PathVariable Long taskId,
                                                         @RequestHeader("X-Username") String username,
                                                         @RequestHeader("Authorization") String authorizationHeader,
                                                         @RequestBody CreateRequestResource resource) {
        var member = this.taskServiceClient.fetchMemberByUsername(username, authorizationHeader);
        if (member.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var task = this.taskServiceClient.fetchTaskDetailsById(taskId);
        if (task.isEmpty() || !task.get().memberId().equals(member.get().id())) {
            return ResponseEntity.badRequest().build();
        }

        var createRequestCommand = CreateRequestCommandFromResourceAssembler.toCommandFromResource(resource, taskId);
        var requestId = requestCommandService.handle(createRequestCommand);

        if(requestId.equals(0L))
            return ResponseEntity.badRequest().build();

        var getRequestByIdQuery = new GetRequestByIdQuery(requestId);
        var optionalRequest = this.requestQueryService.handle(getRequestByIdQuery);
        if (optionalRequest.isEmpty())
            return ResponseEntity.badRequest().build();

        var requestResource = RequestResourceFromEntityAssembler.toResourceFromEntity(optionalRequest.get(), task.get(), member.get());
        return new ResponseEntity<>(requestResource, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get requests from a task", description = "Get a list of requests from a task id")
    public ResponseEntity<List<RequestResource>> getRequestsByTaskId(@PathVariable Long taskId,
                                                                    @RequestHeader("Authorization") String authorizationHeader) {
        var getRequestsByTaskIdQuery = new GetRequestsByTaskIdQuery(taskId);
        var requests = this.requestQueryService.handle(getRequestsByTaskIdQuery);

        var taskOpt = this.taskServiceClient.fetchTaskDetailsById(taskId);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var memberOpt = this.taskServiceClient.fetchMemberByMemberId(taskOpt.get().memberId(), authorizationHeader);
        if (memberOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var requestResources = requests.stream()
                .map(request -> RequestResourceFromEntityAssembler.toResourceFromEntity(request, taskOpt.get(), memberOpt.get()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestResources);
    }

    @DeleteMapping("/{requestId}")
    @Operation(summary = "Delete a request by id", description = "Delete a request by id")
    public ResponseEntity<Void> deleteRequestById(@PathVariable Long taskId, @PathVariable Long requestId) {
        var deleteRequestCommand = new DeleteRequestCommand(requestId);
        this.requestCommandService.handle(deleteRequestCommand);
        return ResponseEntity.noContent().build();
    }
}
