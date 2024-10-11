import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    @Bean
    fun connectionFactory(): CachingConnectionFactory {
        return CachingConnectionFactory("localhost")
    }

    @Bean
    fun amqpAdmin(): RabbitAdmin {
        return RabbitAdmin(connectionFactory())
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate {
        return RabbitTemplate(connectionFactory())
    }

    @Bean
    fun myQueue(): Queue {
        return Queue("myqueue")
    }
}
