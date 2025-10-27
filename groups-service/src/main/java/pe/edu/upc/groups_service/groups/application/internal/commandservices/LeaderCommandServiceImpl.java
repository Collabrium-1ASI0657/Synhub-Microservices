package pe.edu.upc.groups_service.groups.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;
import pe.edu.upc.groups_service.groups.domain.model.commands.CreateLeaderCommand;
import pe.edu.upc.groups_service.groups.domain.services.LeaderCommandService;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.LeaderRepository;

import java.util.Optional;

@Service
public class LeaderCommandServiceImpl implements LeaderCommandService {
  private final LeaderRepository leaderRepository;

  public LeaderCommandServiceImpl(LeaderRepository leaderRepository) {
    this.leaderRepository = leaderRepository;
  }

  @Override
  public Optional<Leader> handle(CreateLeaderCommand command) {
    Leader leader = new Leader();
    leaderRepository.save(leader);
    return Optional.of(leader);
  }
}