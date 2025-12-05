package pe.edu.upc.requests_service.domain.model.commands;

public record UpdateRequestCommand(Long requestId, String requestStatus) {
}
