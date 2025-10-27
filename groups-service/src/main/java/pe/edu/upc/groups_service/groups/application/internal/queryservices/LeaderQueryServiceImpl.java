package pe.edu.upc.groups_service.groups.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.application.clients.iam.IamServiceClient;
import pe.edu.upc.groups_service.groups.application.dto.LeaderWithUserInfo;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByIdQuery;
import pe.edu.upc.groups_service.groups.domain.model.queries.GetLeaderByUsernameQuery;
import pe.edu.upc.groups_service.groups.domain.services.LeaderQueryService;
import pe.edu.upc.groups_service.groups.infrastructure.persistence.jpa.repositories.LeaderRepository;

import java.util.Optional;

@Service
public class LeaderQueryServiceImpl implements LeaderQueryService {

  private final LeaderRepository leaderRepository;
  private final IamServiceClient iamServiceClient;

  public LeaderQueryServiceImpl(LeaderRepository leaderRepository,
                                IamServiceClient iamServiceClient) {
    this.leaderRepository = leaderRepository;
    this.iamServiceClient = iamServiceClient;
  }

  @Override
  public Optional<Leader> handle(GetLeaderByIdQuery query) {
    return leaderRepository.findById(query.leaderId());
  }

  @Override
  public Optional<LeaderWithUserInfo> handle(GetLeaderByUsernameQuery query, String authorizationHeader) {
    var userOptional = iamServiceClient.fetchUserByUsername(query.username(), authorizationHeader);
//    if (userOptional.isEmpty()) {
//      return Optional.empty();
//    }
    var user = userOptional.get();
    var primaryRoleOptional = user.roles().stream().findFirst();
    if (primaryRoleOptional.isEmpty()) {
      return Optional.empty();
    }

    var role = primaryRoleOptional.get();
    if (!role.equals("ROLE_LEADER")) {
      return Optional.empty();
    }

    var leader = leaderRepository.findById(user.leaderId().id());
    if (leader.isEmpty()) {
      return Optional.empty();
    }

    var leaderWithUserInfo = new LeaderWithUserInfo(leader.get(), user);

    return Optional.of(leaderWithUserInfo);
  }
}