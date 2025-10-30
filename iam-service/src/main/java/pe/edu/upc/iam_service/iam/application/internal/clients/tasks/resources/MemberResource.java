package pe.edu.upc.iam_service.iam.application.internal.clients.tasks.resources;

public record MemberResource(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl,
    String email,
    Long groupId) {

}
