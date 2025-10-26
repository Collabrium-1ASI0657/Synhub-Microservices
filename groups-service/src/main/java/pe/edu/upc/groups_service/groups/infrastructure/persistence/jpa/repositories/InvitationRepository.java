package pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Invitation;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.MemberId;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
  Optional<Invitation> findByMemberId(MemberId memberId);
  List<Invitation> findByGroup_Id(Long groupId);
  boolean existsByMemberId(MemberId memberId);
}