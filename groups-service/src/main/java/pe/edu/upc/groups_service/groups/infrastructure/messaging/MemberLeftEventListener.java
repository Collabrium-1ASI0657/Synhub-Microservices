package pe.edu.upc.groups_service.groups.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import pe.edu.upc.groups_service.groups.domain.model.commands.LeaveGroupCommand;
import pe.edu.upc.groups_service.groups.domain.model.events.MemberLeftEvent;
import pe.edu.upc.groups_service.groups.domain.services.GroupCommandService;
import pe.edu.upc.groups_service.groups.infrastructure.config.RabbitMQConfig;

@Service
public class MemberLeftEventListener {
  private final GroupCommandService groupCommandService;

  public MemberLeftEventListener(GroupCommandService groupCommandService) {
    this.groupCommandService = groupCommandService;
  }

  @RabbitListener(queues = RabbitMQConfig.QUEUE_MEMBER_LEFT)
  public void handleMemberLeft(MemberLeftEvent event) {
    System.out.println(" [*] Received event: group.member.left → " + event);

    var command = new LeaveGroupCommand(event.memberId(), event.groupId());
    groupCommandService.handle(command);
  }
}
