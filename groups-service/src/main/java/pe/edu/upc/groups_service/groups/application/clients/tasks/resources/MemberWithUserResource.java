package pe.edu.upc.groups_service.groups.application.clients.tasks.resources;

public record MemberWithUserResource(Long id,
                                     String username,
                                     String name,
                                     String surname,
                                     String imgUrl,
                                     String email,
                                     Long groupId) {
}
