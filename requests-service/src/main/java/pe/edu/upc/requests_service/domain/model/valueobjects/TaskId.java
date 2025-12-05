package pe.edu.upc.requests_service.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public record TaskId(@NotNull Long value) {
    public TaskId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Task ID cannot be null or negative.");
        }
    }
}