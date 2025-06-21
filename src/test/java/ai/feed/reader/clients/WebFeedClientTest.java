package ai.feed.reader.clients;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.junit.jupiter.api.Assertions.*;

class WebFeedClientTest {

    @Configuration
    static class TestConfig {
        @Bean
        public WebFeedClient webFeedClient() {
            return new WebFeedClient();
        }
    }

    @Test
    @DisplayName("Should return FullTextWebFeedClient when fullText is true")
    void testGetWebFeedClient_FullTextTrue() {
        // Given
        WebFeedClient webFeedClient = new WebFeedClient();
        
        // When
        WebFeedClientInterface client = webFeedClient.getWebFeedClient(true);
        
        // Then
        assertNotNull(client);
        assertTrue(client instanceof FullTextWebFeedClient);
    }

    @Test
    @DisplayName("Should return DefaultWebFeedClient when fullText is false")
    void testGetWebFeedClient_FullTextFalse() {
        // Given
        WebFeedClient webFeedClient = new WebFeedClient();
        
        // When
        WebFeedClientInterface client = webFeedClient.getWebFeedClient(false);
        
        // Then
        assertNotNull(client);
        assertTrue(client instanceof DefaultWebFeedClient);
    }
} 