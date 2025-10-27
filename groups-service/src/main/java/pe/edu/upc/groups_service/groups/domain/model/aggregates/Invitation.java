package pe.edu.upc.groups_service.groups.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.MemberId;
import pe.edu.upc.groups_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Setter
@Getter
public class Invitation extends AuditableAbstractAggregateRoot<Invitation> {
  @Nullable
  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "member_id"))
  private MemberId memberId;

  @ManyToOne
  @JoinColumn(name = "group_id")
  private Group group;

  public Invitation() {}

  public Invitation(MemberId memberId, Group group) {
    this.memberId = memberId;
    this.group = group;
  }
}