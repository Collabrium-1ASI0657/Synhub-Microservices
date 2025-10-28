package pe.edu.upc.groups_service.groups.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;
import pe.edu.upc.groups_service.groups.domain.services.LeaderQueryService;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.LeaderDetailsResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.LeaderResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.transform.LeaderDetailsResourceFromEntityAssembler;
import pe.edu.upc.groups_service.groups.interfaces.rest.transform.LeaderResourceFromEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/leader")
@Tag(name = "Leaders", description = "Leader management API")
public class LeaderController {

  private final LeaderQueryService leaderQueryService;

  public LeaderController(LeaderQueryService leaderQueryService) {
    this.leaderQueryService = leaderQueryService;
  }

  @GetMapping("/{leaderId}")
  @Operation(summary = "Get leader details by id", description = "Fetches the details of the leader.")
  public ResponseEntity<LeaderDetailsResource> getLeaderById(@PathVariable Long leaderId) {
    var getLeaderByIdQuery = new GetLeaderByIdQuery(leaderId);
    var leaderWithUserInfo = this.leaderQueryService.handle(getLeaderByIdQuery);
    if (leaderWithUserInfo.isEmpty()) return ResponseEntity.notFound().build();
    var leaderDetailsResource = LeaderDetailsResourceFromEntityAssembler.toResourceFromEntity(leaderWithUserInfo.get());
    return ResponseEntity.ok(leaderDetailsResource);
  }

  @GetMapping("/details")
  @Operation(summary = "Get leader details by authentication", description = "Fetches the details of the authenticated leader.")
  public ResponseEntity<LeaderResource> getLeaderByAuthentication(
      @RequestHeader("X-Username") String username,
      @RequestHeader("Authorization") String authorizationHeader) {

    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
    var leaderWithUserInfo = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);
    if (leaderWithUserInfo.isEmpty()) return ResponseEntity.notFound().build();
    var leaderResource = LeaderResourceFromEntityAssembler.toResourceFromEntity(leaderWithUserInfo.get());
    return ResponseEntity.ok(leaderResource);
  }
}