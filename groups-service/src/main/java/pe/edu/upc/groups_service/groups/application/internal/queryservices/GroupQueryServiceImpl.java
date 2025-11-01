package pe.edu.upc.groups_service.groups.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Group;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByCodeQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByLeaderIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetGroupByMemberIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.GroupCode;
import pe.edu.upc.groups_service.groups.domain.services.GroupQueryService;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.GroupRepository;

import java.util.Optional;

@Service
public class GroupQueryServiceImpl implements GroupQueryService {
  private final GroupRepository groupRepository;

  public GroupQueryServiceImpl(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  @Override
  public Optional<Group> handle(GetGroupByLeaderIdQuery query) {
    return this.groupRepository.findByLeaderId(query.leaderId());
  }

  @Override
  public Optional<Group> handle(GetGroupByCodeQuery query) {
    var code = new GroupCode(query.code());
    var group = this.groupRepository.findByCode(code);
    if (group.isEmpty()) {
      return Optional.empty();
    }
    return group;
  }

  @Override
  public Optional<Group> handle(GetGroupByMemberIdQuery query) {

    return Optional.empty();
  }

  @Override
  public Optional<Group> handle(GetGroupByIdQuery query) {
    return this.groupRepository.findById(query.groupId());
  }
}