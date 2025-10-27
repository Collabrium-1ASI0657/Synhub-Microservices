package pe.edu.upc.iam_service.iam.infrastructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;

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
}
