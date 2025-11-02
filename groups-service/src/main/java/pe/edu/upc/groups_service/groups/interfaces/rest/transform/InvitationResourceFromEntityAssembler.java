package pe.edu.upc.groups_service.groups.interfaces.rest.transform;

import pe.edu.upc.groups_service.groups.application.clients.iam.resources.UserResource;
import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Invitation;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.InvitationResource;

public class InvitationResourceFromEntityAssembler {
  public static InvitationResource toResourceFromEntity(
      Invitation entity, MemberWithUserResource memberInfo
  ) {

    return new InvitationResource(
        entity.getId(),
        InvitationMemberResourceFromEntityAssembler.toResourceFromEntity(memberInfo),
        GroupResourceFromEntityAssembler.toResourceFromEntity(entity.getGroup())
    );
  }

  public static InvitationResource toResourceFromEntity(Invitation entity, UserResource userInfo) {
    return new InvitationResource(
        entity.getId(),
        InvitationMemberResourceFromEntityAssembler.ToResourceFromUserResource(userInfo),
        GroupResourceFromEntityAssembler.toResourceFromEntity(entity.getGroup())
    );
  }
}