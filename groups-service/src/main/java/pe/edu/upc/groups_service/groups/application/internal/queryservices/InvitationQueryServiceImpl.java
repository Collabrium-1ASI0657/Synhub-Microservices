package pe.edu.upc.groups_service.groups.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Invitation;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetInvitationByMemberIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetInvitationsByGroupIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.MemberId;
import pe.edu.upc.groups_service.groups.domain.services.InvitationQueryService;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.InvitationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationQueryServiceImpl implements InvitationQueryService {

  private final InvitationRepository invitationRepository;

  public InvitationQueryServiceImpl(InvitationRepository invitationRepository) {
    this.invitationRepository = invitationRepository;
  }

  @Override
  public Optional<Invitation> handle(GetInvitationByMemberIdQuery query) {
    return this.invitationRepository.findByMemberId(new MemberId(query.memberId()));
  }

  @Override
  public List<Invitation> handle(GetInvitationsByGroupIdQuery query) {
    return this.invitationRepository.findByGroup_Id(query.groupId());
  }
}