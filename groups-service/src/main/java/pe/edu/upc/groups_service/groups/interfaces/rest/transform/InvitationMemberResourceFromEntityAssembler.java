package pe.edu.upc.groups_service.groups.interfaces.rest.transform;

import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.InvitationMemberResource;

public class InvitationMemberResourceFromEntityAssembler {
  public static InvitationMemberResource toResourceFromEntity(MemberWithUserResource member) {
    return new InvitationMemberResource(
        member.id(),
        member.username(),
        member.name(),
        member.surname(),
        member.imgUrl()
    );
  }
}
