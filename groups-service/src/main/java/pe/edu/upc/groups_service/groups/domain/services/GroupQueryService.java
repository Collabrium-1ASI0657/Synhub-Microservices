package pe.edu.upc.groups_service.groups.domain.services;

import pe.edu.upc.groups_service.groups.domain.model.aggregates.Group;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByCodeQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByLeaderIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByMemberIdQuery;

import java.util.Optional;

public interface GroupQueryService {
  Optional<Group> handle(GetGroupByLeaderIdQuery query);
  Optional<Group> handle(GetGroupByCodeQuery query);
  Optional<Group> handle(GetGroupByMemberIdQuery query);
  Optional<Group> handle(GetGroupByIdQuery query);
}