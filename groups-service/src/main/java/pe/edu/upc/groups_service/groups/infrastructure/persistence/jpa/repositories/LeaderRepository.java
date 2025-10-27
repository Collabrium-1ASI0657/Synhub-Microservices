package pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;

import java.util.Optional;

@Repository
public interface LeaderRepository extends JpaRepository<Leader, Long> {
  Optional<Leader> findById(Long id);
}