package pe.edu.upc.groups_service.groups.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.groups_service.groups.application.clients.iam.IamServiceClient;
import pe.edu.upc.groups_service.groups.application.clients.tasks.TasksServiceClient;
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
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/invitations")
@Tag(name = "Invitations", description = "Invitation Management Endpoints")
public class InvitationController {
  private final InvitationQueryService invitationQueryService;
  private final InvitationCommandService invitationCommandService;
  private final LeaderQueryService leaderQueryService;
  private final GroupQueryService groupQueryService;
  private final TasksServiceClient tasksServiceClient;
  private final IamServiceClient iamServiceClient;
  private static final Logger logger = LoggerFactory.getLogger(InvitationController.class);

  public InvitationController(InvitationQueryService invitationQueryService,
                              InvitationCommandService invitationCommandService,
                              LeaderQueryService leaderQueryService,
                              GroupQueryService groupQueryService,
                              TasksServiceClient tasksServiceClient,
                              IamServiceClient iamServiceClient) {
    this.invitationQueryService = invitationQueryService;
    this.invitationCommandService = invitationCommandService;
    this.leaderQueryService = leaderQueryService;
    this.groupQueryService = groupQueryService;
    this.tasksServiceClient = tasksServiceClient;
    this.iamServiceClient = iamServiceClient;
  }

  @PostMapping("/groups/{groupId}")
  @Operation(summary = "Create a new invitation", description = "Create a new invitation for a group")
  public ResponseEntity<InvitationResource> createInvitation(
      @PathVariable Long groupId,
      @RequestHeader("X-Username") String username,
      @RequestHeader("Authorization") String authorizationHeader) {

    // Llamar al microservicio de tareas para obtener el miembro
    var member = this.tasksServiceClient.fetchMemberByUsername(username, authorizationHeader);
    if (member.isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    // Crear el comando, pero ahora pasamos el ID del miembro directamente
    var createInvitationCommand = new CreateInvitationCommand(member.get().id(), groupId);

    // Delegamos al servicio de comandos
    var invitation = this.invitationCommandService.handle(createInvitationCommand);
    if (invitation.isEmpty()) return ResponseEntity.notFound().build();

    // Transformamos la entidad en recurso
    var invitationResource = InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation.get(), member.get());

    return ResponseEntity.ok(invitationResource);
  }


  @GetMapping("/group")
  @Operation(summary = "Get all invitations for a group", description = "Get all invitations for a specific group")
  public ResponseEntity<List<InvitationResource>> getInvitationByGroupId(@RequestHeader("X-Username") String username,
                                                                         @RequestHeader("Authorization") String authorizationHeader) {
    // 1️⃣ Obtener el líder por username
    var getLeaderByUsernameQuery = new GetLeaderByUsernameQuery(username);
    var leader = this.leaderQueryService.handle(getLeaderByUsernameQuery, authorizationHeader);
    if (leader.isEmpty()) {
      logger.warn("No se encontró líder con username: {}", username);
      return ResponseEntity.notFound().build();
    }

    // 2️⃣ Obtener el grupo del líder
    var getGroupByLeaderIdQuery = new GetGroupByLeaderIdQuery(leader.get().leader().getId());
    var group = this.groupQueryService.handle(getGroupByLeaderIdQuery);
    if (group.isEmpty()) {
      logger.warn("No se encontró grupo para el líder con id: {}", leader.get().leader().getId());
      return ResponseEntity.notFound().build();
    }

    // 3️⃣ Obtener las invitaciones
    var getInvitationsByGroupIdQuery = new GetInvitationsByGroupIdQuery(group.get().getId());
    var invitations = this.invitationQueryService.handle(getInvitationsByGroupIdQuery);
    if (invitations.isEmpty()) {
      logger.info("No hay invitaciones para el grupo con id {}", group.get().getId());
      return ResponseEntity.ok(List.of());
    }

    // 4️⃣ Mapear a recursos con control de errores
    var invitationResources = invitations.stream()
        .map(invitation -> {
          var invitationMember = invitation.getMemberId();
          if (invitationMember == null) {
            logger.warn("La invitación con id {} no tiene miembro asociado", invitation.getId());
            return null;
          }

          logger.info("Buscando miembro con id: {}", invitationMember.value());
          var memberOpt = this.iamServiceClient.fetchUserByMemberId(invitationMember.value(), authorizationHeader);
          if (memberOpt.isEmpty()) {
            logger.warn("No se encontró el miembro con id: {}", invitationMember.value());
          }

          var member = memberOpt.get();
          return InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation, member);
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    return ResponseEntity.ok(invitationResources);
  }

  @DeleteMapping("/member")
  @Operation(summary = "Cancel an invitation", description = "Cancel an existing invitation by a member")
  public ResponseEntity<Void> cancelInvitation(@RequestHeader("X-Username") String username,
                                               @RequestHeader("Authorization") String authorizationHeader) {

    var member = this.tasksServiceClient.fetchMemberByUsername(username, authorizationHeader);
    if (member.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Long memberId = member.get().id();

    var getInvitationByMemberIdQuery = new GetInvitationByMemberIdQuery(memberId);
    var invitationId = this.invitationQueryService.handle(getInvitationByMemberIdQuery).get().getId();

    var cancelInvitationCommand = new CancelInvitationCommand(memberId, invitationId);
    this.invitationCommandService.handle(cancelInvitationCommand);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/member")
  @Operation(summary = "Get invitation of a member", description = "Get invitation for a specific member")
  public ResponseEntity<InvitationResource> getInvitationByMember(@RequestHeader("X-Username") String username,
                                                                  @RequestHeader("Authorization") String authorizationHeader) {
    var member = this.tasksServiceClient.fetchMemberByUsername(username, authorizationHeader);
    if (member.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    Long memberId = member.get().id();

    var getInvitationByMemberIdQuery = new GetInvitationByMemberIdQuery(memberId);
    var invitation = this.invitationQueryService.handle(getInvitationByMemberIdQuery);
    if(invitation.isEmpty()) return ResponseEntity.notFound().build();

    var invitationResource = InvitationResourceFromEntityAssembler.toResourceFromEntity(invitation.get(), member.get());
    return ResponseEntity.ok(invitationResource);
  }
}