package pe.edu.upc.groups_service.groups.domain.services;

import pe.edu.upc.groups_service.groups.application.dto.LeaderWithUserInfo;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;

import java.util.Optional;

public interface LeaderQueryService {
  Optional<Leader> handle(GetLeaderByIdQuery query);
  Optional<LeaderWithUserInfo> handle(GetLeaderByUsernameQuery query, String authorizationHeader);
}