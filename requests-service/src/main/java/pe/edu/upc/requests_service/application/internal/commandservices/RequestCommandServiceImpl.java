package pe.edu.upc.requests_service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.requests_service.domain.model.aggregates.Request;
import pe.edu.upc.requests_service.domain.model.commands.CreateRequestCommand;
import pe.edu.upc.requests_service.domain.model.commands.DeleteAllRequestsByTaskIdCommand;
import pe.edu.upc.requests_service.domain.model.commands.DeleteRequestCommand;
import pe.edu.upc.requests_service.domain.model.commands.UpdateRequestCommand;
import pe.edu.upc.requests_service.domain.services.RequestCommandService;
import pe.edu.upc.requests_service.infrastructure.persistence.jpa.repositories.RequestRepository;

import java.util.Optional;

@Service
public class RequestCommandServiceImpl implements RequestCommandService {

    private final RequestRepository requestRepository;

    public RequestCommandServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public Long handle(CreateRequestCommand command) {
        // TODO
        return 0L;
    }

    @Override
    public Optional<Request> handle(UpdateRequestCommand command) {
        var requestId = command.requestId();

        if (!this.requestRepository.existsById(requestId))
            throw new IllegalArgumentException("Request with id " + requestId + " does not exist");

        var requestToUpdate = this.requestRepository.findById(requestId).get();
        requestToUpdate.updateRequestStatus(command.requestStatus());

        try {
            var updatedRequest = this.requestRepository.save(requestToUpdate);
            return Optional.of(updatedRequest);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating request: " + e.getMessage());
        }
    }

    @Override
    public void handle(DeleteRequestCommand command) {
        var requestId = command.requestId();

        if (!requestRepository.existsById(requestId))
            throw new IllegalArgumentException("Request with id " + requestId + " does not exist");

        try {
            requestRepository.deleteById(requestId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting request: " + e.getMessage());
        }
    }

    @Override
    public void handle(DeleteAllRequestsByTaskIdCommand command) {
        // TODO
    }
}
