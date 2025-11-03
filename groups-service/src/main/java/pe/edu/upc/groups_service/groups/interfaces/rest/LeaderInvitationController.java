package pe.edu.upc.groups_service.groups.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.groups_service.groups.domain.model.commands.AcceptInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.RejectInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;
import pe.edu.upc.groups_service.groups.domain.services.InvitationCommandService;
import pe.edu.upc.groups_service.groups.domain.services.LeaderQueryService;

@RestController
@RequestMapping("/api/v1/group/invitations")
@Tag(name = "Invitations", description = "Invitation Management Endpoints")
public class LeaderInvitationController {
  private final InvitationCommandService invitationCommandService;
  private final LeaderQueryService leaderQueryService;

  public LeaderInvitationController(InvitationCommandService invitationCommandService,
                                    LeaderQueryService leaderQueryService) {
    this.invitationCommandService = invitationCommandService;
    this.leaderQueryService = leaderQueryService;
  }

  @PatchMapping("/{invitationId}")
  @Operation(summary = "Accept or decline an invitation", description = "Accept or decline an invitation for a leader")
  public ResponseEntity<Void> processInvitation(@PathVariable Long invitationId,
                                                @RequestHeader("X-Username") String username,
                                                @RequestHeader("Authorization") String authorizationHeader,
                                                @RequestParam(defaultValue = "false") boolean accept) {
    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
    var leader = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);
    if (leader.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Long leaderId = leader.get().leader().getId();

    if (accept) {
      var acceptInvitationCommand = new AcceptInvitationCommand(leaderId, invitationId);
      this.invitationCommandService.handle(acceptInvitationCommand);
    } else {
      var cancelInvitationCommand = new RejectInvitationCommand(leaderId, invitationId);
      this.invitationCommandService.handle(cancelInvitationCommand);
    }
    return ResponseEntity.ok().build();
  }
}
