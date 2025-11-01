package pe.edu.upc.groups_service.groups.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.groups_service.groups.domain.model.commands.CreateGroupCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.DeleteGroupCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.UpdateGroupCommand;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByLeaderIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;
import pe.edu.upc.groups_service.groups.domain.services.GroupCommandService;
import pe.edu.upc.groups_service.groups.domain.services.GroupQueryService;
import pe.edu.upc.groups_service.groups.domain.services.LeaderQueryService;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.CreateGroupResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.GroupResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.InvitationResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.UpdateGroupResource;
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

  @PutMapping
  @Operation(summary = "Update a group", description = "Updates a group")
  public ResponseEntity<GroupResource> updateGroup(@RequestBody UpdateGroupResource groupResource,
                                                   @RequestHeader("X-Username") String username,
                                                   @RequestHeader("Authorization") String authorizationHeader) {
    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
    var leaderWithUserInfo = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);
    if (leaderWithUserInfo.isEmpty()) return ResponseEntity.notFound().build();

    var updateGroupCommand = new UpdateGroupCommand(
        leaderWithUserInfo.get().leader().getId(),
        groupResource.name(),
        groupResource.description(),
        groupResource.imgUrl()
    );

    var group = this.groupCommandService.handle(updateGroupCommand);
    if (group.isEmpty()) return ResponseEntity.notFound().build();
    var groupResourceUpdated = GroupResourceFromEntityAssembler.toResourceFromEntity(group.get());

    return ResponseEntity.ok(groupResourceUpdated);
  }

  @DeleteMapping
  @Operation(summary = "Delete a group", description = "Deletes a group")
  public ResponseEntity<Void> deleteGroup(@RequestHeader("X-Username") String username,
                                          @RequestHeader("Authorization") String authorizationHeader) {
    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
    var leaderWithUserInfo = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);
    if (leaderWithUserInfo.isEmpty()) return ResponseEntity.notFound().build();

    var deleteGroupCommand = new DeleteGroupCommand(leaderWithUserInfo.get().leader().getId());
    this.groupCommandService.handle(deleteGroupCommand);

    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "Get a group by ID", description = "Gets a group by ID")
  public ResponseEntity<GroupResource> getGroupById(@RequestHeader("X-Username") String username,
                                                    @RequestHeader("Authorization") String authorizationHeader) {
    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
    var leaderWithUserInfo = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);
    if (leaderWithUserInfo.isEmpty()) return ResponseEntity.notFound().build();

    var getGroupByLeaderIdQuery = new GetGroupByLeaderIdQuery(leaderWithUserInfo.get().leader().getId());
    var group = this.groupQueryService.handle(getGroupByLeaderIdQuery);
    if (group.isEmpty()) return ResponseEntity.notFound().build();

    var groupResource = GroupResourceFromEntityAssembler.toResourceFromEntity(group.get());

    return ResponseEntity.ok(groupResource);
  }

  @DeleteMapping("/members/{memberId}")
  @Operation(summary = "Remove a member from the group", description = "Removes a member from the group")
  public ResponseEntity<Void> removeMemberFromGroup(@RequestHeader("X-Username") String username,
                                                    @RequestHeader("Authorization") String authorizationHeader,
                                                    @PathVariable Long memberId) {

    return null;
  }
}
