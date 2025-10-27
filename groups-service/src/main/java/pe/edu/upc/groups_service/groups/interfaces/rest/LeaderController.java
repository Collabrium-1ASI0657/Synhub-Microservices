package pe.edu.upc.groups_service.groups.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;
import pe.edu.upc.groups_service.groups.domain.services.LeaderQueryService;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.LeaderResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.transform.LeaderResourceFromEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/leader")
@Tag(name = "Leaders", description = "Leader management API")
public class LeaderController {

  private final LeaderQueryService leaderQueryService;

  public LeaderController(LeaderQueryService leaderQueryService) {
    this.leaderQueryService = leaderQueryService;
  }

  @GetMapping("/details")
  @Operation(summary = "Get leader details by authentication", description = "Fetches the details of the authenticated leader.")
  public ResponseEntity<LeaderResource> getLeaderByAuthentication(
      @RequestHeader("X-Username") String username,
      @RequestHeader("Authorization") String authorizationHeader) {

//    String miguel = "miguel";

    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);

    var leaderWithUserInfo = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);

    if (leaderWithUserInfo.isEmpty()) return ResponseEntity.notFound().build();

    var leaderResource = LeaderResourceFromEntityAssembler.toResourceFromEntity(leaderWithUserInfo.get());

    return ResponseEntity.ok(leaderResource);
  }
}