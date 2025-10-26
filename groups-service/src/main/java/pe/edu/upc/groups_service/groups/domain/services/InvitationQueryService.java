package pe.edu.upc.groups_service.groups.domain.services;

import pe.edu.upc.groups_service.groups.domain.model.aggregates.Invitation;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetInvitationByMemberIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetInvitationsByGroupIdQuery;

import java.util.List;
import java.util.Optional;

public interface InvitationQueryService {
  Optional<Invitation> handle(GetInvitationByMemberIdQuery query);
  List<Invitation> handle(GetInvitationsByGroupIdQuery query);
}