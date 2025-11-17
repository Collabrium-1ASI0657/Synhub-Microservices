package pe.edu.upc.groups_service.groups.domain.model.events;

public record RemoveMemberEvent(Long groupId, Long memberId) {
}
