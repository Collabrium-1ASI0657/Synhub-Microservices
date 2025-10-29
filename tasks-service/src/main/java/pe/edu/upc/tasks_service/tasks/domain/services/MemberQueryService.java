package pe.edu.upc.tasks_service.tasks.domain.services;

import pe.edu.upc.tasks_service.tasks.domain.model.aggregates.Member;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetAllMembersQuery;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetMemberByIdQuery;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetMemberByUsernameQuery;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetMembersByGroupIdQuery;

import java.util.List;
import java.util.Optional;

public interface MemberQueryService {

  /**
   * Retrieves a member by their ID.
   *
   * @param query the query containing the member ID
   * @return an Optional containing the member if found, or empty if not found
   */
  Optional<Member> handle(GetMemberByIdQuery query);

  /**
   * Retrieves a user member by their ID.
   *
   * @param query the query containing the user ID
   * @return an Optional containing the User if found, or empty if not found
   */
  Optional<Member> handle(GetMemberByUsernameQuery query);

  /**
   * Retrieves all members.
   *
   * @return a list of all members
   */
  List<Member> handle(GetAllMembersQuery query);

  /**
   * Retrieves all members by group ID.
   *
   * @param query the query containing the group ID
   * @return a list of members belonging to the specified group
   */
  List<Member> handle(GetMembersByGroupIdQuery query);
}