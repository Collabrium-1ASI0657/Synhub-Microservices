package pe.edu.upc.requests_service.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import pe.edu.upc.requests_service.domain.model.commands.CreateRequestCommand;
import pe.edu.upc.requests_service.domain.model.valueobjects.RequestStatus;
import pe.edu.upc.requests_service.domain.model.valueobjects.RequestType;
import pe.edu.upc.requests_service.domain.model.valueobjects.TaskId;
import pe.edu.upc.requests_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Getter
@Entity
@Table(name = "requests")
@AttributeOverrides({
        @AttributeOverride(name = "taskId.value", column = @Column(name = "task_id"))
})
public class Request extends AuditableAbstractAggregateRoot<Request> {
    @NonNull
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type")
    private RequestType requestType;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private RequestStatus requestStatus;

    @Embedded
    @Setter
    private TaskId taskId;

    public Request() {
    }

    public String getRequestType() {
        return requestType.toString();
    }

    public String getRequestStatus() {
        return requestStatus.toString();
    }

    public Request(CreateRequestCommand command) {
        this.description = command.description();
        this.requestType = RequestType.fromString(command.requestType());
        this.requestStatus = RequestStatus.PENDING;
        this.taskId = new TaskId(command.taskId());
    }

    public void updateRequestStatus(String requestStatus) {
        this.requestStatus = RequestStatus.fromString(requestStatus);
    }
}
