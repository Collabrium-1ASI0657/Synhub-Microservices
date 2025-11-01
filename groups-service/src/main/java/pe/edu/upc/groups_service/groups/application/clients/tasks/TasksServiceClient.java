package pe.edu.upc.groups_service.groups.application.clients.tasks;

import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberResource;
import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberWithUserResource;

import java.util.Optional;

public interface TasksServiceClient {
  Optional<MemberResource> fetchMemberByMemberId(Long memberId);

  Optional<MemberWithUserResource> fetchMemberByUsername(String username, String authorizationHeader);
}
