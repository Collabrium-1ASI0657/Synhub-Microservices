package pe.edu.upc.groups_service.groups.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.events.LeaderCreatedSuccessfullyEvent;
import pe.edu.upc.groups_service.groups.infrastructure.config.RabbitMQConfig;

import java.sql.Time;

@Service
public class IamEventPublisher {
  private final RabbitTemplate rabbitTemplate;

  public IamEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  // Usaremos una nueva Routing Key
  public static final String ROUTING_KEY_LEADER_CREATED_SUCCESS = "leader.created.success";

  public void publishLeaderCreatedSuccessfully(Long userId,
                                               Long leaderId,
                                               Time averageSolutionTime,
                                               Integer solvedRequests) {
    var event = new LeaderCreatedSuccessfullyEvent(userId, leaderId, averageSolutionTime, solvedRequests);

    rabbitTemplate.convertAndSend(
        RabbitMQConfig.EXCHANGE_NAME,
        ROUTING_KEY_LEADER_CREATED_SUCCESS,
        event
    );
  }
}