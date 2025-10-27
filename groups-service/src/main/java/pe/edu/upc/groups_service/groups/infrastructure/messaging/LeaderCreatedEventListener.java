package pe.edu.upc.groups_service.groups.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.upc.groups_service.groups.domain.model.commands.CreateLeaderCommand;
import pe.edu.upc.groups_service.groups.domain.model.events.LeaderCreatedEvent;
import pe.edu.upc.groups_service.groups.domain.services.LeaderCommandService;

@Component
public class LeaderCreatedEventListener {

  private final LeaderCommandService leaderCommandService;

  public LeaderCreatedEventListener(LeaderCommandService leaderCommandService) {
    this.leaderCommandService = leaderCommandService;
  }

  @RabbitListener(queues = "groups.leader-created")
  public void handleLeaderCreatedEvent(LeaderCreatedEvent event) {
    System.out.println("📩 Received LeaderCreatedEvent from IAM");

    var createLeaderCommand = new CreateLeaderCommand();

    leaderCommandService.handle(createLeaderCommand);
  }
}