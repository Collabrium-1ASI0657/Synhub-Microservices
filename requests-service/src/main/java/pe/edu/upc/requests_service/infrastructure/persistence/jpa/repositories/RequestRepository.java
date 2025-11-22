package pe.edu.upc.requests_service.infrastructure.persistence.jpa.repositories;

import pe.edu.upc.requests_service.domain.model.aggregates.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.requests_service.domain.model.valueobjects.TaskId;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByTaskId(TaskId taskId);
    void deleteByTaskId(TaskId taskId);
}
