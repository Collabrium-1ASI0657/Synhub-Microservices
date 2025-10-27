package pe.edu.upc.groups_service.groups.application.internal.acl;

import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.LeaderRepository;
import pe.edu.upc.groups_service.groups.interfaces.acl.LeaderContextFacade;

import java.util.Optional;

@Service
public class LeaderContextFacadeImpl implements LeaderContextFacade {

  private final LeaderRepository leaderRepository;

  public LeaderContextFacadeImpl(LeaderRepository leaderRepository) {
    this.leaderRepository = leaderRepository;
  }

  @Override
  public Optional<Leader> createLeader() {
    var leader = new Leader();
    var createdLeader = leaderRepository.save(leader);
    return Optional.of(createdLeader);
  }
}