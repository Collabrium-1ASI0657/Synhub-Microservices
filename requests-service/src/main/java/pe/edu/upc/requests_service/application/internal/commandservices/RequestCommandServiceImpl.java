package pe.edu.upc.requests_service.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.requests_service.application.clients.tasks.TaskServiceClient;
import pe.edu.upc.requests_service.domain.model.aggregates.Request;
import pe.edu.upc.requests_service.domain.model.commands.CreateRequestCommand;
import pe.edu.upc.requests_service.domain.model.commands.DeleteAllRequestsByTaskIdCommand;
import pe.edu.upc.requests_service.domain.model.commands.DeleteRequestCommand;
import pe.edu.upc.requests_service.domain.model.commands.UpdateRequestCommand;
import pe.edu.upc.requests_service.domain.model.valueobjects.RequestType;
import pe.edu.upc.requests_service.domain.model.valueobjects.TaskId;
import pe.edu.upc.requests_service.domain.services.RequestCommandService;
import pe.edu.upc.requests_service.infrastructure.persistence.jpa.repositories.RequestRepository;

import java.util.Optional;

@Service
public class RequestCommandServiceImpl implements RequestCommandService {

    private final RequestRepository requestRepository;
    private final TaskServiceClient taskServiceClient;

    public RequestCommandServiceImpl(RequestRepository requestRepository, TaskServiceClient taskServiceClient) {
        this.requestRepository = requestRepository;
        this.taskServiceClient = taskServiceClient;
    }

    @Override
    public Long handle(CreateRequestCommand command) {
        try {
            RequestType.fromString(command.requestType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid request type");
        }

        var task = taskServiceClient.fetchTaskById(command.taskId());

        if (task.isEmpty())
            throw new IllegalArgumentException("Task with id " + command.taskId() + " does not exist");

        var request = new Request(command);
        this.requestRepository.save(request);
        return request.getId();
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
