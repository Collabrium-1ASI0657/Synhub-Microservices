package pe.edu.upc.requests_service.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.requests_service.application.clients.groups.GroupServiceClient;
import pe.edu.upc.requests_service.application.clients.tasks.TaskServiceClient;
import pe.edu.upc.requests_service.domain.model.queries.GetRequestsByTaskIdQuery;
import pe.edu.upc.requests_service.domain.services.RequestQueryService;
import pe.edu.upc.requests_service.interfaces.rest.resources.RequestResource;
import pe.edu.upc.requests_service.interfaces.rest.transform.RequestResourceFromEntityAssembler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(value = "/api/v1")
@Tag(name = "Group Requests", description = "Group Requests management API")
public class GroupRequestController {

    private final RequestQueryService requestQueryService;
    private final TaskServiceClient taskServiceClient;
    private final GroupServiceClient groupServiceClient;

    public GroupRequestController(
            RequestQueryService requestQueryService,
            TaskServiceClient taskServiceClient,
            GroupServiceClient groupServiceClient) {
        this.requestQueryService = requestQueryService;
        this.taskServiceClient = taskServiceClient;
        this.groupServiceClient = groupServiceClient;
    }

    @GetMapping("/leader/group/requests")
    @Operation(summary = "Get all requests from a group", description = "Get all requests from a group")
    public ResponseEntity<List<RequestResource>> getAllRequestsFromGroup(
            @RequestHeader("X-Username") String username,
            @RequestHeader("Authorization") String authorizationHeader) {

        var leader = groupServiceClient.fetchLeaderByUsername(username, authorizationHeader);
        if (leader.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var group = groupServiceClient.fetchGroupByLeaderId(leader.get().id(), authorizationHeader);
        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var tasks = taskServiceClient.fetchAllTasksByGroupId(group.get().id(), authorizationHeader);

        var requests = tasks.stream()
                .flatMap(task -> {
                    var getRequestsByTaskIdQuery = new GetRequestsByTaskIdQuery(task.id());
                    return requestQueryService.handle(getRequestsByTaskIdQuery)
                            .stream()
                            .map(r -> RequestResourceFromEntityAssembler.toResourceFromEntity(r, task));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/member/group/requests")
    @Operation(summary = "Get all requests from member", description = "Get all requests from member")
    public ResponseEntity<List<RequestResource>> getAllRequestsFromMember(
            @RequestHeader("X-Username") String username,
            @RequestHeader("Authorization") String authorizationHeader) {
        var member = taskServiceClient.fetchMemberByUsername(username, authorizationHeader);
        if (member.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var tasks = taskServiceClient.fetchAllTasksByMemberId(member.get().id(), authorizationHeader);
        var requests = tasks.stream()
                .flatMap(task -> {
                    var getRequestsByTaskIdQuery = new GetRequestsByTaskIdQuery(task.id());
                    return requestQueryService.handle(getRequestsByTaskIdQuery)
                            .stream()
                            .map(r -> RequestResourceFromEntityAssembler.toResourceFromEntity(r, task));
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(requests);
    }
}
