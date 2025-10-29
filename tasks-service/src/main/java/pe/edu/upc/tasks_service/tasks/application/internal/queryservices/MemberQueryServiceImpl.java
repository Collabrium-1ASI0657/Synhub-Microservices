package pe.edu.upc.tasks_service.tasks.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.tasks_service.tasks.domain.model.aggregates.Member;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetAllMembersQuery;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetMemberByIdQuery;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetMemberByUsernameQuery;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetMembersByGroupIdQuery;
import pe.edu.upc.tasks_service.tasks.domain.model.valueobjects.GroupId;
import pe.edu.upc.tasks_service.tasks.domain.services.MemberQueryService;
import pe.edu.upc.tasks_service.tasks.infrastructure.persistence.jpa.repositories.MemberRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MemberQueryServiceImpl implements MemberQueryService {
  private final MemberRepository memberRepository;

  public MemberQueryServiceImpl(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public Optional<Member> handle(GetMemberByIdQuery query) {
    return memberRepository.findById(query.memberId());
  }

  @Override
  public Optional<Member> handle(GetMemberByUsernameQuery query) {


    return Optional.empty();
  }

  @Override
  public List<Member> handle(GetAllMembersQuery query) {
    return this.memberRepository.findAll();
  }

  @Override
  public List<Member> handle(GetMembersByGroupIdQuery query) {
    return memberRepository.findMembersByGroupId(new GroupId(query.groupId()));
  }
}
