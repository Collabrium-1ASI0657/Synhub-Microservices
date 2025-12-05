package pe.edu.upc.requests_service.domain.model.commands;

public record CreateRequestCommand(
        String description,
        String requestType,
        Long taskId
) {
}
