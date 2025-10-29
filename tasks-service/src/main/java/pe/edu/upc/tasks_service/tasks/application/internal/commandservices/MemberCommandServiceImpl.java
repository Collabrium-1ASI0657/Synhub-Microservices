package pe.edu.upc.tasks_service.tasks.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.tasks_service.tasks.domain.model.aggregates.Member;
import pe.edu.upc.tasks_service.tasks.domain.model.commands.AddGroupToMemberCommand;
import pe.edu.upc.tasks_service.tasks.domain.model.commands.CreateMemberCommand;
import pe.edu.upc.tasks_service.tasks.domain.services.MemberCommandService;
import pe.edu.upc.tasks_service.tasks.infrastructure.persistence.jpa.repositories.MemberRepository;

import java.util.Optional;

@Service
public class MemberCommandServiceImpl implements MemberCommandService {
  private final MemberRepository memberRepository;

  public MemberCommandServiceImpl(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public Optional<Member> handle(CreateMemberCommand command) {
    var member = new Member(command);
    var createdMember = memberRepository.save(member);
    return Optional.of(createdMember);
  }

  @Override
  public Optional<Member> handle(AddGroupToMemberCommand command) {
    return Optional.empty();
  }
}
