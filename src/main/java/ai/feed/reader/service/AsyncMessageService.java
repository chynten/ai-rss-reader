package ai.feed.reader.service;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.apptasticsoftware.rssreader.Item;

import ai.feed.reader.component.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AsyncMessageService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OpenAIService openAIService;

    public void sendRssFeedMessage(@Payload Item feed) {
        log.trace("Sending RSS feed object message: {}", feed);
        rabbitTemplate.convertAndSend(RabbitMQConfig.RSS_FEED_EXCHANGE, RabbitMQConfig.RSS_FEED_ROUTING_KEY, feed);
    }

    @RabbitListener(queues = RabbitMQConfig.RSS_FEED_QUEUE)
    public void handleRssFeedMessage(Item feed) {
    
        log.trace("Received RSS feed message: {}", feed);

        try {
            List<String> tags = openAIService.fetchTags(feed.getContent().orElse(""));
        } catch (Exception e) {
            log.error("Error processing RSS feed message: {}", feed, e);
            // You can implement dead letter queue handling here if needed
        }
    }
}
