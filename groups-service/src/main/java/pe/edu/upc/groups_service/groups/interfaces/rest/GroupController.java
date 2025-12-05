package pe.edu.upc.groups_service.groups.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.groups_service.groups.application.clients.tasks.TasksServiceClient;
import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.TaskResource;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByCodeQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByLeaderIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;
import pe.edu.upc.groups_service.groups.domain.services.GroupQueryService;
import pe.edu.upc.groups_service.groups.domain.services.LeaderQueryService;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.GroupMemberResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.GroupResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.transform.GroupMemberResourceFromEntityAssembler;
import pe.edu.upc.groups_service.groups.interfaces.rest.transform.GroupResourceFromEntityAssembler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/groups")
@Tag(name = "Groups", description = "Group management API")
public class GroupController {
  private final GroupQueryService groupQueryService;
  private final LeaderQueryService leaderQueryService;
  private final TasksServiceClient tasksServiceClient;

  public GroupController(GroupQueryService groupQueryService,
                         LeaderQueryService leaderQueryService,
                         TasksServiceClient tasksServiceClient) {
    this.groupQueryService = groupQueryService;
    this.leaderQueryService = leaderQueryService;
    this.tasksServiceClient = tasksServiceClient;
  }

  @GetMapping("/{groupId}")
  @Operation(summary = "Search group by its id.", description = "Search group by id.")
  public ResponseEntity<GroupResource> getGroupById(@PathVariable Long groupId) {
    var getGroupByIdQuery = new GetGroupByIdQuery(groupId);
    var group = this.groupQueryService.handle(getGroupByIdQuery);
    if (group.isEmpty()) return ResponseEntity.notFound().build();

    var groupResource = GroupResourceFromEntityAssembler.toResourceFromEntity(group.get());

    return ResponseEntity.ok(groupResource);
  }

  @GetMapping("/search")
  @Operation(summary = "Search for a group by code", description = "Search for a group by code")
  public ResponseEntity<GroupResource> searchGroupByCode(@RequestParam String code) {
    var getGroupByCodeQuery = new GetGroupByCodeQuery(code);
    var group = this.groupQueryService.handle(getGroupByCodeQuery);
    if (group.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var groupResource = GroupResourceFromEntityAssembler.toResourceFromEntity(group.get());
    return ResponseEntity.ok(groupResource);
  }

  @GetMapping("/members")
  @Operation(summary = "Get all group members", description = "Retrieve all members of a group")
  public ResponseEntity<List<GroupMemberResource>> getAllMembersByGroupId(@RequestHeader("X-Username") String username,
                                                                          @RequestHeader("Authorization") String authorizationHeader) {
    // 1. Obtener el líder autenticado
    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
    var leaderWithUserInfo = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);
    if (leaderWithUserInfo.isEmpty()) return ResponseEntity.notFound().build();

    // 2. Obtener el grupo asociado al líder
    var getGroupByLeaderIdQuery = new GetGroupByLeaderIdQuery(leaderWithUserInfo.get().leader().getId());
    var group = this.groupQueryService.handle(getGroupByLeaderIdQuery);
    if (group.isEmpty()) return ResponseEntity.notFound().build();

    Long groupId = group.get().getId();

    // 3. Obtener los miembros desde tasks-service
    var members = tasksServiceClient.fetchMembersByGroupId(groupId, authorizationHeader);
    if (members == null || members.isEmpty()) return ResponseEntity.noContent().build();

    // 4. Mapearlos con el assembler
    var memberResources = members.stream()
        .map(GroupMemberResourceFromEntityAssembler::toResourceFromEntity)
        .collect(Collectors.toList());

    return ResponseEntity.ok(memberResources);
  }

  @GetMapping("/tasks")
  @Operation(summary = "Get all tasks by group ID", description = "Retrieve all tasks associated with a specific group ID")
  public ResponseEntity<List<TaskResource>> getAllTasksByGroupId(
      @RequestHeader("X-Username") String username,
      @RequestHeader("Authorization") String authorizationHeader) {
    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
    var leader = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);
    if (leader.isEmpty()) return ResponseEntity.notFound().build();

    var getGroupByLeaderIdQuery = new GetGroupByLeaderIdQuery(leader.get().leader().getId());
    var group = this.groupQueryService.handle(getGroupByLeaderIdQuery);
    if (group.isEmpty()) return ResponseEntity.notFound().build();
    Long groupId = group.get().getId();

    var tasks = tasksServiceClient.fetchTasksByGroupId(groupId, authorizationHeader);
    if (tasks == null || tasks.isEmpty()) return ResponseEntity.noContent().build();

    return ResponseEntity.ok(tasks);
  }

  @GetMapping
  @Operation(summary = "Get group by leader Id", description = "Retrive the group of the leader by its id")
  public ResponseEntity<GroupResource> getGroupByLeaderId(@RequestParam Long leaderId){
    var getGroupByLeaderIdQuery = new GetGroupByLeaderIdQuery(leaderId);
    var group = this.groupQueryService.handle(getGroupByLeaderIdQuery);
    if (group.isEmpty()) return ResponseEntity.notFound().build();

    var groupResource = GroupResourceFromEntityAssembler.toResourceFromEntity(group.get());

    return ResponseEntity.ok(groupResource);
  }
}