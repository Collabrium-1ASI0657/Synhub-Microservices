package pe.edu.upc.requests_service.domain.services;

import pe.edu.upc.requests_service.domain.model.aggregates.Request;
import pe.edu.upc.requests_service.domain.model.commands.CreateRequestCommand;
import pe.edu.upc.requests_service.domain.model.commands.DeleteAllRequestsByTaskIdCommand;
import pe.edu.upc.requests_service.domain.model.commands.DeleteRequestCommand;
import pe.edu.upc.requests_service.domain.model.commands.UpdateRequestCommand;

import java.util.Optional;

public interface RequestCommandService {
    Long handle(CreateRequestCommand command);
    Optional<Request> handle(UpdateRequestCommand command);
    void handle(DeleteRequestCommand command);
    void handle(DeleteAllRequestsByTaskIdCommand command);
}
