package pe.edu.upc.groups_service.groups.domain.model.events;

public record MemberLeftEvent(Long memberId, Long groupId) {
}
