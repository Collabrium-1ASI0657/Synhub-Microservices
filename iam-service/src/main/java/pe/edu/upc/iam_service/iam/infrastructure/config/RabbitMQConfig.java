package pe.edu.upc.iam_service.iam.infrastructure.config;

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
  // EXCHANGE GENERAL
  // -------------------------------------------------------
  public static final String EXCHANGE_NAME = "iam-events-exchange";

  // -------------------------------------------------------
  // EVENTOS PRODUCIDOS (IAM envía)
  // -------------------------------------------------------
  public static final String ROUTING_KEY_LEADER_CREATED = "leader.created";
  public static final String ROUTING_KEY_MEMBER_CREATED = "member.created";

  // -------------------------------------------------------
  // EVENTOS ESCUCHADOS
  // -------------------------------------------------------
  public static final String ROUTING_KEY_LEADER_CREATED_SUCCESS = "leader.created.success";
  public static final String QUEUE_LEADER_UPDATER = "iam.leader-updater"; // Nueva cola

  // -------------------------------------------------------
  // BEANS DE INFRAESTRUCTURA
  // -------------------------------------------------------
  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public Queue leaderUpdaterQueue() {
    return new Queue(QUEUE_LEADER_UPDATER, true);
  }

  @Bean
  public Binding leaderUpdaterBinding(Queue leaderUpdaterQueue, TopicExchange exchange) {
    return BindingBuilder.bind(leaderUpdaterQueue)
        .to(exchange)
        .with(ROUTING_KEY_LEADER_CREATED_SUCCESS);
  }
}
