package pe.edu.upc.groups_service.groups.interfaces.acl;

import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;

import java.util.Optional;

public interface LeaderContextFacade {
  Optional<Leader> createLeader();
}