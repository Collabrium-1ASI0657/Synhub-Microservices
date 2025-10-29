package pe.edu.upc.tasks_service.tasks.infrastructure.config;

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
  // Mismo exchange general usado por todos los servicios
  public static final String EXCHANGE_NAME = "iam-events-exchange";

  // Evento que Tasks CONSUME
  public static final String ROUTING_KEY_MEMBER_CREATED = "member.created";
  public static final String QUEUE_MEMBER_CREATED = "tasks.member-created";

  // Evento que Tasks PRODUCE (respuesta)
  public static final String ROUTING_KEY_MEMBER_CREATED_SUCCESS = "member.created.success";

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
  public Queue memberCreatedQueue() {
    return new Queue(QUEUE_MEMBER_CREATED, true);
  }

  @Bean
  public Binding memberCreatedBinding(Queue memberCreatedQueue, TopicExchange exchange) {
    return BindingBuilder.bind(memberCreatedQueue)
        .to(exchange)
        .with(ROUTING_KEY_MEMBER_CREATED);
  }
}
