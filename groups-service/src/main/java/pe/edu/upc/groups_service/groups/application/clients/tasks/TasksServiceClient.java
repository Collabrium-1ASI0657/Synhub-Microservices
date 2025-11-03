package pe.edu.upc.groups_service.groups.application.clients.tasks;

import pe.edu.upc.groups_service.groups.application.clients.tasks.resources.MemberWithUserResource;

import java.util.List;
import java.util.Optional;

public interface TasksServiceClient {
  Optional<MemberWithUserResource> fetchMemberByMemberId(Long memberId);

  Optional<MemberWithUserResource> fetchMemberByUsername(String username, String authorizationHeader);

  List<MemberWithUserResource> fetchMembersByGroupId(Long groupId, String authorizationHeader);
}
