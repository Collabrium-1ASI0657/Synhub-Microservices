package pe.edu.upc.groups_service.groups.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pe.edu.upc.groups_service.groups.domain.model.commands.CreateLeaderCommand;
import pe.edu.upc.groups_service.groups.domain.model.events.LeaderCreatedEvent;
import pe.edu.upc.groups_service.groups.domain.services.LeaderCommandService;
import pe.edu.upc.groups_service.groups.infrastructure.config.RabbitMQConfig;

@Component
public class LeaderCreatedEventListener {

  private final LeaderCommandService leaderCommandService;

  public LeaderCreatedEventListener(LeaderCommandService leaderCommandService) {
    this.leaderCommandService = leaderCommandService;
  }

  @RabbitListener(queues = RabbitMQConfig.QUEUE_LEADER_CREATED)
  public void handleLeaderCreatedEvent(LeaderCreatedEvent event) {
    System.out.println("📩 Received LeaderCreatedEvent from IAM");

    var createLeaderCommand = new CreateLeaderCommand();

    leaderCommandService.handle(createLeaderCommand);
  }
}