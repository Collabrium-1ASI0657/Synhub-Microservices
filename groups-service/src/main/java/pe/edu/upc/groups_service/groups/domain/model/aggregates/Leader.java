package pe.edu.upc.groups_service.groups.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.groups_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.sql.Time;

@Entity
@Getter
@Setter
public class Leader extends AuditableAbstractAggregateRoot<Leader> {

  Time averageSolutionTime;

  Integer solvedRequests;

  public Leader() {
    this.averageSolutionTime = new Time(0);
    this.solvedRequests = 0;
  }
}