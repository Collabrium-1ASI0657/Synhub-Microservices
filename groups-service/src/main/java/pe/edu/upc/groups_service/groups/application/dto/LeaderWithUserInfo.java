package pe.edu.upc.groups_service.groups.application.dto;

import pe.edu.upc.groups_service.groups.application.clients.iam.resources.UserResource;
import pe.edu.upc.groups_service.groups.domain.model.aggregates.Leader;

public record LeaderWithUserInfo(
    Leader leader,
    UserResource user) {
}
