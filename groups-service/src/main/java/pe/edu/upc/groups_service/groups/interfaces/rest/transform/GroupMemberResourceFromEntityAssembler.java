package pe.edu.upc.groups_service.groups.interfaces.rest.transform;

import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.GroupMemberResource;

public class GroupMemberResourceFromEntityAssembler {
  public static GroupMemberResource toResourceFromEntity(MemberWithUserResource entity) {
    if (entity == null) return null;

    return new GroupMemberResource(
        entity.id(),
        entity.username(),
        entity.name(),
        entity.surname(),
        entity.imgUrl()
    );
  }
}
