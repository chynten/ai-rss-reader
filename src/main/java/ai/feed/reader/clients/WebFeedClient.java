package ai.feed.reader.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebFeedClient {

    @Bean
    public WebFeedClientInterface getWebFeedClient(boolean fullText) {
        if (fullText) {
            return new FullTextWebFeedClient();
        } else {
            return new DefaultWebFeedClient();
        }
    }
}
