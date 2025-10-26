package pe.edu.upc.groups_service.groups.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public record MemberId(@NotNull Long value) {
  public MemberId {
    if (value == null || value <= 0) {
      throw new IllegalArgumentException("Member ID cannot be null or negative.");
    }
  }
}