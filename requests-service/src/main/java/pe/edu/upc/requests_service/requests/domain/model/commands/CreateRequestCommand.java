package pe.edu.upc.requests_service.requests.domain.model.commands;

public record CreateRequestCommand(
        String description,
        String requestType,
        Long taskId
) {
}
