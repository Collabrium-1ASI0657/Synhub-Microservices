package pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Group;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.GroupCode;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.LeaderId;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

  Optional<Group> findByLeaderId(LeaderId leaderId);

  boolean existsByCode(GroupCode code);

  Optional<Group> findByCode(GroupCode code);
}