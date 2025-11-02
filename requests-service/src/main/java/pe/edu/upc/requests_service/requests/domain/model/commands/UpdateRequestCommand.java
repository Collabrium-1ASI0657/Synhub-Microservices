package pe.edu.upc.requests_service.requests.domain.model.commands;

public record UpdateRequestCommand(Long requestId, String requestStatus) {
}
