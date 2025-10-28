package pe.edu.upc.groups_service.groups.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.groups_service.groups.domain.model.commands.CreateGroupCommand;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;
import pe.edu.upc.groups_service.groups.domain.services.GroupCommandService;
import pe.edu.upc.groups_service.groups.domain.services.GroupQueryService;
import pe.edu.upc.groups_service.groups.domain.services.LeaderQueryService;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.CreateGroupResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.GroupResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.transform.GroupResourceFromEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/leader/group")
@Tag(name = "Groups", description = "Group management API")
public class LeaderGroupController {
  private final GroupQueryService groupQueryService;
  private final GroupCommandService groupCommandService;
  private final LeaderQueryService leaderQueryService;

  public LeaderGroupController(GroupQueryService groupQueryService,
                               GroupCommandService groupCommandService,
                               LeaderQueryService leaderQueryService) {
    this.groupQueryService = groupQueryService;
    this.groupCommandService = groupCommandService;
    this.leaderQueryService = leaderQueryService;
  }

  @PostMapping
  @Operation(summary = "Create a new group", description = "Creates a new group")
  public ResponseEntity<GroupResource> createGroup(@RequestBody CreateGroupResource resource,
                                                   @RequestHeader("X-Username") String username,
                                                   @RequestHeader("Authorization") String authorizationHeader) {

    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
    var leaderWithUserInfo = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);
    if (leaderWithUserInfo.isEmpty()) return ResponseEntity.notFound().build();

    var createGroupCommand = new CreateGroupCommand(
        resource.name(),
        resource.imgUrl(),
        resource.description(),
        leaderWithUserInfo.get().leader().getId()
    );

    var group = this.groupCommandService.handle(createGroupCommand);
    if (group.isEmpty()) return ResponseEntity.notFound().build();

    var groupResourceCreated = GroupResourceFromEntityAssembler.toResourceFromEntity(group.get());
    return ResponseEntity.ok(groupResourceCreated);
  }
}
