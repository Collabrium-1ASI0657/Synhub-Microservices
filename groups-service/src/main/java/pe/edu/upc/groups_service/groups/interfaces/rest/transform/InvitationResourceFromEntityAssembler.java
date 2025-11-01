package pe.edu.upc.groups_service.groups.interfaces.rest.transform;

import pe.edu.upc.groups_service.groups.application.dto.InvitationWithMemberInfo;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.InvitationResource;

public class InvitationResourceFromEntityAssembler {
  public static InvitationResource toResourceFromEntity(
      InvitationWithMemberInfo resource
  ) {
    var invitation = resource.invitation();
    var member = resource.member();

    return new InvitationResource(
        invitation.getId(),
        InvitationMemberResourceFromEntityAssembler.toResourceFromEntity(member),
        GroupResourceFromEntityAssembler.toResourceFromEntity(invitation.getGroup())
    );
  }
}