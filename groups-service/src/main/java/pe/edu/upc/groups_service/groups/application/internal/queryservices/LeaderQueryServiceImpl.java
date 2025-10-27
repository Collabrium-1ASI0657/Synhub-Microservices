package pe.edu.upc.groups_service.groups.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;
import pe.edu.upc.groups_service.groups.domain.services.LeaderQueryService;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.LeaderRepository;

import java.util.Optional;

@Service
public class LeaderQueryServiceImpl implements LeaderQueryService {

  private final LeaderRepository leaderRepository;

  public LeaderQueryServiceImpl(LeaderRepository leaderRepository) {
    this.leaderRepository = leaderRepository;
  }

  @Override
  public Optional<Leader> handle(GetLeaderByIdQuery query) {
    return leaderRepository.findById(query.leaderId());
  }

  @Override
  public Optional<Leader> handle(GetLeaderByUsernameQuery query) {
    return Optional.empty();
  }
}