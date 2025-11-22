package pe.edu.upc.requests_service.application.internal.queryservices;

import pe.edu.upc.requests_service.domain.model.aggregates.Request;
import pe.edu.upc.requests_service.domain.model.queries.GetAllRequestsQuery;
import pe.edu.upc.requests_service.domain.model.queries.GetRequestByIdQuery;
import pe.edu.upc.requests_service.domain.model.queries.GetRequestsByTaskIdQuery;
import pe.edu.upc.requests_service.domain.model.valueobjects.TaskId;
import pe.edu.upc.requests_service.domain.services.RequestQueryService;
import pe.edu.upc.requests_service.infrastructure.persistence.jpa.repositories.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestQueryServiceImpl implements RequestQueryService {

    private final RequestRepository requestRepository;

    public RequestQueryServiceImpl(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Request> handle(GetAllRequestsQuery query) {
        return requestRepository.findAll();
    }

    @Override
    public List<Request> handle(GetRequestsByTaskIdQuery query) {
        var taskId = new TaskId(query.taskId());
        return requestRepository.findByTaskId(taskId);
    }

    @Override
    public Optional<Request> handle(GetRequestByIdQuery query) {
        return this.requestRepository.findById(query.requestId());
    }
}
