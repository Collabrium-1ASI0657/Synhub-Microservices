package pe.edu.upc.groups_service.groups.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.events.AcceptInvitationEvent;
import pe.edu.upc.groups_service.groups.domain.model.events.RemoveMemberEvent;
import pe.edu.upc.groups_service.groups.infrastructure.config.RabbitMQConfig;

@Service
public class TasksEventPublisher {
  private final RabbitTemplate rabbitTemplate;

  public TasksEventPublisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  // Publica un evento cuando un miembro acepta una invitación
  public void publishInvitationAccepted(Long groupId, Long memberId) {
    var event = new AcceptInvitationEvent(groupId, memberId);

    rabbitTemplate.convertAndSend(
        RabbitMQConfig.TASKS_EXCHANGE_NAME,
        RabbitMQConfig.ROUTING_KEY_GROUP_ACCEPTED,
        event
    );
  }

  // Evento: un miembro es eliminado del grupo
  public void publishMemberRemoved(Long groupId, Long memberId) {
    var event = new RemoveMemberEvent(groupId, memberId);

    rabbitTemplate.convertAndSend(
        RabbitMQConfig.TASKS_EXCHANGE_NAME,
        RabbitMQConfig.ROUTING_KEY_MEMBER_REMOVED,
        event
    );
  }
}
