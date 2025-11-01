package pe.edu.upc.groups_service.groups.application.dto;

import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberWithUserResource;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Invitation;

public record InvitationWithMemberInfo(Invitation invitation,
                                       MemberWithUserResource member) {
}
