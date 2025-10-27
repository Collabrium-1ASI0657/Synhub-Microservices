package pe.edu.upc.groups_service.groups.interfaces.rest.transform;

import pe.edu.upc.groups_service.groups.domain.model.aggregates.Group;
import pe.edu.upc.groups_service.groups.interfaces.rest.resources.GroupResource;

public class GroupResourceFromEntityAssembler {
  public static GroupResource toResourceFromEntity(Group group) {
    return new GroupResource(
        group.getId(),
        group.getName(),
        group.getImgUrl().imgUrl(),
        group.getDescription(),
        group.getCode().code(),
        group.getMemberCount()
    );
  }
}