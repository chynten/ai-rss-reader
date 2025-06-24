package ai.feed.reader.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String RSS_FEED_QUEUE = "rss.feed.queue";
    public static final String RSS_FEED_EXCHANGE = "rss.feed.exchange";
    public static final String RSS_FEED_ROUTING_KEY = "rss.feed.routing.key";

    @Bean
    public Queue rssFeedQueue() {
        return new Queue(RSS_FEED_QUEUE, true);
    }

    @Bean
    public TopicExchange rssFeedExchange() {
        return new TopicExchange(RSS_FEED_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue rssFeedQueue, TopicExchange rssFeedExchange) {
        return BindingBuilder.bind(rssFeedQueue)
                .to(rssFeedExchange)
                .with(RSS_FEED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
} 