package pe.edu.upc.groups_service.groups.domain.services;

import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;
import pe.edu.upc.groups_service.groups.domain.model.commands.CreateLeaderCommand;

import java.util.Optional;

public interface LeaderCommandService {
  Optional<Leader> handle(CreateLeaderCommand command);
}