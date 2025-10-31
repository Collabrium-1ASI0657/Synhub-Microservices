package pe.edu.upc.tasks_service.tasks.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetMemberByIdQuery;
import pe.edu.upc.tasks_service.tasks.domain.model.queries.GetMemberInfoByIdQuery;
import pe.edu.upc.tasks_service.tasks.domain.services.MemberQueryService;
import pe.edu.upc.tasks_service.tasks.interfaces.rest.resources.MemberOnlyResource;
import pe.edu.upc.tasks_service.tasks.interfaces.rest.resources.MemberResource;
import pe.edu.upc.tasks_service.tasks.interfaces.rest.transform.MemberResourceFromEntityAssembler;

@RestController
@RequestMapping("/api/v1/member")
@Tag(name = "Member", description = "Member API")
@ApiResponse(responseCode = "201", description = "Member created")
public class MemberController {
  private final MemberQueryService memberQueryService;

  public MemberController(MemberQueryService memberQueryService) {
    this.memberQueryService = memberQueryService;
  }

  @GetMapping("/details/{memberId}")
  @Operation(summary = "Get member details by member ID", description = "Fetches the details of a member by their ID.")
  public ResponseEntity<MemberResource> getMemberById(@PathVariable Long memberId,
                                                      @RequestHeader("Authorization") String authorizationHeader) {
    var getMemberInfoByIdQuery = new GetMemberInfoByIdQuery(memberId, authorizationHeader);
    var memberWithUserInfo = this.memberQueryService.handle(getMemberInfoByIdQuery);
    if (memberWithUserInfo.isEmpty()) return ResponseEntity.notFound().build();

    var memberResource = MemberResourceFromEntityAssembler.toResourceFromUserResource(memberWithUserInfo.get(), memberId);
    return ResponseEntity.ok(memberResource);
  }

  @GetMapping("{memberId}")
  @Operation(summary = "Get member only by member ID", description = "Fetches the member by their ID.")
  public ResponseEntity<MemberOnlyResource> getMemberOnlyById(@PathVariable Long memberId) {
    var getMemberByIdQuery = new GetMemberByIdQuery(memberId);
    var member = this.memberQueryService.handle(getMemberByIdQuery);
    if (member.isEmpty()) return ResponseEntity.notFound().build();
    var memberResource = MemberResourceFromEntityAssembler.toResourceFromEntity(member.get());

    return ResponseEntity.ok(memberResource);
  }
}
