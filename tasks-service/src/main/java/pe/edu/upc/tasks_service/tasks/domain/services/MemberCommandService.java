package pe.edu.upc.tasks_service.tasks.domain.services;

import pe.edu.upc.tasks_service.tasks.domain.model.aggregates.Member;
import pe.edu.upc.tasks_service.tasks.domain.model.commands.AddGroupToMemberCommand;
import pe.edu.upc.tasks_service.tasks.domain.model.commands.CreateMemberCommand;

import java.util.Optional;

public interface MemberCommandService {
  Optional<Member> handle(CreateMemberCommand command);
  Optional<Member> handle(AddGroupToMemberCommand command);
}