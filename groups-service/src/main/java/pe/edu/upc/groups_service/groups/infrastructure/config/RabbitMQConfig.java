package pe.edu.upc.groups_service.groups.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  // -------------------------------------------------------
  // EXCHANGE GENERAL (mismo nombre usado en IAM)
  // -------------------------------------------------------
  public static final String EXCHANGE_NAME = "iam-events-exchange";

  // -------------------------------------------------------
  // EVENTOS QUE GROUPS CONSUME
  // -------------------------------------------------------
  public static final String ROUTING_KEY_LEADER_CREATED = "leader.created";
  public static final String QUEUE_LEADER_CREATED = "groups.leader-created";

  // -------------------------------------------------------
  // EXCHANGE PARA PUBLICAR EVENTOS HACIA TASKS
  // -------------------------------------------------------
  public static final String TASKS_EXCHANGE_NAME = "tasks-events-exchange";
  public static final String ROUTING_KEY_GROUP_ACCEPTED = "group.accepted";

  // -------------------------------------------------------
  // BEANS DE INFRAESTRUCTURA
  // -------------------------------------------------------
  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public TopicExchange tasksExchange() {
    return new TopicExchange(TASKS_EXCHANGE_NAME);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  // -------------------------------------------------------
  // CONFIGURACIÓN DE COLA Y BINDING
  // -------------------------------------------------------
  @Bean
  public Queue leaderCreatedQueue() {
    // durable = true → se mantiene después de reinicios
    return new Queue(QUEUE_LEADER_CREATED, true);
  }

  @Bean
  public Binding leaderCreatedBinding(Queue leaderCreatedQueue, TopicExchange exchange) {
    return BindingBuilder.bind(leaderCreatedQueue)
        .to(exchange)
        .with(ROUTING_KEY_LEADER_CREATED);
  }
}
