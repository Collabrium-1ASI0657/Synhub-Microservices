package pe.edu.upc.groups_service.groups.domain.model.aggregates;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import pe.edu.upc.groups_service.groups.domain.model.commands.UpdateGroupCommand;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.GroupCode;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.ImgUrl;
import pe.edu.upc.groups_service.groups.domain.model.valueobjects.LeaderId;
import pe.edu.upc.groups_service.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Group extends AuditableAbstractAggregateRoot<Group> {

  @Embedded
  @AttributeOverride(name = "code", column = @Column(name = "code", unique = true))
  private GroupCode code;

  @NotNull
  private String name;

  @NotNull
  @Column(columnDefinition = "TEXT")
  private String description;

  @Embedded
  private ImgUrl imgUrl;

  @Nullable
  @Setter
  @Embedded
  @AttributeOverride(name = "leaderId.value", column = @Column(name = "leader_id"))
  private LeaderId leaderId;

  @NotNull
  private Integer memberCount;

  public Group(String name, String description, String imgUrl , LeaderId leaderId, GroupCode code) {
    this.name = name;
    this.imgUrl = new ImgUrl(imgUrl);
    this.leaderId = leaderId;
    this.description = description;
    this.memberCount = 0;
    this.code = code;
  }

  public void updateInformation(UpdateGroupCommand command) {
    this.name = command.name().isEmpty() ? this.name : command.name();
    this.description = command.description().isEmpty() ? this.description : command.description();
    this.imgUrl = command.imgUrl().isEmpty() ? this.imgUrl : new ImgUrl(command.imgUrl());
  }
}