package pe.edu.upc.requests_service.domain.services;

import pe.edu.upc.requests_service.domain.model.aggregates.Request;
import pe.edu.upc.requests_service.domain.model.queries.GetAllRequestsQuery;
import pe.edu.upc.requests_service.domain.model.queries.GetRequestByIdQuery;
import pe.edu.upc.requests_service.domain.model.queries.GetRequestsByTaskIdQuery;

import java.util.List;
import java.util.Optional;

public interface RequestQueryService {
    List<Request> handle(GetAllRequestsQuery query);
    List<Request> handle(GetRequestsByTaskIdQuery query);
    Optional<Request> handle(GetRequestByIdQuery query);
}
