package pe.edu.upc.groups_service.groups.application.clients.iam.resources;

import java.util.List;

public record UserResource(
    Long id,
    String username,
    String name,
    String surname,
    String imgUrl,
    String email,
    LeaderResource leaderId,
    List<String> roles) {
}