package pe.edu.upc.groups_service.groups.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.groups_service.groups.domain.model.commands.CancelInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.commands.CreateInvitationCommand;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByLeaderIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetInvitationByMemberIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetInvitationsByGroupIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;
import pe.edu.upc.groups_service.groups.domain.services.GroupQueryService;
import pe.edu.upc.groups_service.groups.domain.services.InvitationCommandService;
import pe.edu.upc.groups_service.groups.domain.services.InvitationQueryService;
import pe.edu.upc.groups_service.groups.domain.services.LeaderQueryService;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.InvitationResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.transform.InvitationResourceFromEntityAssembler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/invitations")
@Tag(name = "Invitations", description = "Invitation Management Endpoints")
public class InvitationController {
  private final InvitationQueryService invitationQueryService;
  private final InvitationCommandService invitationCommandService;
  private final LeaderQueryService leaderQueryService;
  private final GroupQueryService groupQueryService;
  private static final Logger logger = LoggerFactory.getLogger(InvitationController.class);

  public InvitationController(InvitationQueryService invitationQueryService,
                              InvitationCommandService invitationCommandService,
                              LeaderQueryService leaderQueryService,
                              GroupQueryService groupQueryService) {
    this.invitationQueryService = invitationQueryService;
    this.invitationCommandService = invitationCommandService;
    this.leaderQueryService = leaderQueryService;
    this.groupQueryService = groupQueryService;
  }

//  @PostMapping("/groups/{groupId}")
//  @Operation(summary = "Create a new invitation", description = "Create a new invitation for a group")
//  public ResponseEntity<InvitationResource> createInvitation(@PathVariable Long groupId, @AuthenticationPrincipal UserDetails userDetails) {
//    String username = userDetails.getUsername();
//
//    var getMemberByUsernameQuery = new GetMemberByUsernameQuery(username);
//
//    var member = this.memberQueryService.handle(getMemberByUsernameQuery);
//
//    if(member.isEmpty()) return ResponseEntity.notFound().build();
//
//    Long memberId = member.get().getId();
//
//    var createInvitationCommand = new CreateInvitationCommand(memberId, groupId);
//    var createdInvitation = this.invitationCommandService.handle(createInvitationCommand);
//    if (createdInvitation.isEmpty()) {
//      return ResponseEntity.badRequest().build();
//    }
//    var invitationResource = InvitationResourceFromEntityAssembler.toResourceFromEntity(createdInvitation.get(),member.get());
//    return ResponseEntity.ok(invitationResource);
//  }

//  @GetMapping("/group")
//  @Operation(summary = "Get all invitations for a group", description = "Get all invitations for a specific group")
//  public ResponseEntity<List<InvitationResource>> getInvitationByGroupId(@AuthenticationPrincipal UserDetails userDetails) {
//    String username = userDetails.getUsername();
//
//    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
//
//    var leader = this.leaderQueryService.handle(getLeaderByUsernameQuery);
//
//    if (leader.isEmpty()) return ResponseEntity.notFound().build();
//
//    var group = this.groupQueryService.handle(new GetGroupByLeaderIdQuery(leader.get().getId()));
//
//    if (group.isEmpty()) return ResponseEntity.notFound().build();
//
//    var getInvitationsByGroupIdQuery = new GetInvitationsByGroupIdQuery(group.get().getId());
//    var invitations = this.invitationQueryService.handle(getInvitationsByGroupIdQuery);
//    var invitationResources = invitations.stream()
//        .map(invitation -> {
//          var invitationMember = invitation.getMember();
//          if (invitationMember == null) {
//            logger.warn("La invitación con id {} no tiene miembro asociado", invitation.getId());
//            return null;
//          }
//          logger.info("Buscando miembro con id: {}", invitationMember.getId());
//          var member = this.memberQueryService.handle(new GetMemberByIdQuery(invitationMember.getId()));
//          if (member.isEmpty()) {
//            logger.warn("No se encontró el miembro con id: {}", invitationMember.getId());
//          }
//          return InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation, member.orElse(null));
//        })
//        .collect(Collectors.toList());
//    return ResponseEntity.ok(invitationResources);
//  }

//  @DeleteMapping("/member")
//  @Operation(summary = "Cancel an invitation", description = "Cancel an existing invitation by a member")
//  public ResponseEntity<Void> cancelInvitation(@AuthenticationPrincipal UserDetails userDetails) {
//    String username = userDetails.getUsername();
//
//    var getMemberByUsernameQuery = new GetMemberByUsernameQuery(username);
//
//    var member = this.memberQueryService.handle(getMemberByUsernameQuery);
//
//    if(member.isEmpty()) return ResponseEntity.notFound().build();
//
//    Long memberId = member.get().getId();
//
//    var getInvitationByMemberIdQuery = new GetInvitationByMemberIdQuery(memberId);
//
//    var invitationId = this.invitationQueryService.handle(getInvitationByMemberIdQuery).get().getId();
//
//    var cancelInvitationCommand = new CancelInvitationCommand(memberId, invitationId);
//
//    this.invitationCommandService.handle(cancelInvitationCommand);
//
//    return ResponseEntity.noContent().build();
//  }

//  @GetMapping("/member")
//  @Operation(summary = "Get invitation of a member", description = "Get invitation for a specific member")
//  public ResponseEntity<InvitationResource> getInvitationByMember(@AuthenticationPrincipal UserDetails userDetails) {
//    String username = userDetails.getUsername();
//
//    var getMemberByUsernameQuery = new GetMemberByUsernameQuery(username);
//
//    var member = this.memberQueryService.handle(getMemberByUsernameQuery);
//
//    if(member.isEmpty()) return ResponseEntity.notFound().build();
//
//    Long memberId = member.get().getId();
//
//    var getInvitationByMemberIdQuery = new GetInvitationByMemberIdQuery(memberId);
//
//    var invitation = this.invitationQueryService.handle(getInvitationByMemberIdQuery);
//
//    if(invitation.isEmpty()) return ResponseEntity.notFound().build();
//
//    var invitationResource = InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation.get(), member.get());
//    return ResponseEntity.ok(invitationResource);
//  }
}