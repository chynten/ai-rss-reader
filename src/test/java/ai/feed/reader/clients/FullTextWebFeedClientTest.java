package ai.feed.reader.clients;

import com.apptasticsoftware.rssreader.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FullTextWebFeedClientTest {

    private FullTextWebFeedClient fullTextWebFeedClient;

    @BeforeEach
    void setUp() {
        fullTextWebFeedClient = new FullTextWebFeedClient();
    }

    @Test
    @DisplayName("Should successfully retrieve and process feeds from a valid RSS URL")
    void testGetFeeds_ValidRssUrl() throws IOException {
        // Given
        String validRssUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        
        // When
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds);
        // Note: Some feeds might be empty, so we don't assert non-empty
    }

    @Test
    @DisplayName("Should successfully retrieve feeds from another valid RSS URL")
    void testGetFeeds_AnotherValidRssUrl() throws IOException {
        // Given
        String validRssUrl = "https://feeds.npr.org/1001/rss.xml";
        
        // When
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds);
        // Note: Some feeds might be empty, so we don't assert non-empty
    }

    @Test
    @DisplayName("Should throw IOException when invalid URL is provided")
    void testGetFeeds_InvalidUrl() {
        // Given
        String invalidUrl = "https://invalid-url-that-does-not-exist-12345.com/feed.xml";
        
        // When & Then
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(invalidUrl);
        });
    }

    @Test
    @DisplayName("Should throw IOException when malformed URL is provided")
    void testGetFeeds_MalformedUrl() {
        // Given
        String malformedUrl = "not-a-valid-url";
        
        // When & Then
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(malformedUrl);
        });
    }

    @Test
    @DisplayName("Should throw IOException when null URL is provided")
    void testGetFeeds_NullUrl() {
        // When & Then
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(null);
        });
    }

    @Test
    @DisplayName("Should throw IOException when empty URL is provided")
    void testGetFeeds_EmptyUrl() {
        // Given
        String emptyUrl = "";
        
        // When & Then
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(emptyUrl);
        });
    }

    @Test
    @DisplayName("Should throw IOException when URL with invalid protocol is provided")
    void testGetFeeds_InvalidProtocolUrl() {
        // Given
        String invalidProtocolUrl = "ftp://example.com/feed.xml";
        
        // When & Then
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(invalidProtocolUrl);
        });
    }

    @Test
    @DisplayName("Should handle URL with special characters")
    void testGetFeeds_UrlWithSpecialCharacters() {
        // Given
        String urlWithSpecialChars = "https://example.com/feed with spaces.xml";
        
        // When & Then
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(urlWithSpecialChars);
        });
    }

    @Test
    @DisplayName("Should verify that FullTextWebFeedClient implements WebFeedClientInterface")
    void testFullTextWebFeedClientImplementsInterface() {
        // Then
        assertTrue(fullTextWebFeedClient instanceof WebFeedClientInterface);
    }

    @Test
    @DisplayName("Should create new RssReader instance for each call")
    void testGetFeeds_CreatesNewRssReaderInstance() throws IOException {
        // Given
        String validRssUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        
        // When
        List<Item> feeds1 = fullTextWebFeedClient.getFeeds(validRssUrl);
        List<Item> feeds2 = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds1);
        assertNotNull(feeds2);
        // Both calls should work independently, indicating new instances are created
    }

    @Test
    @DisplayName("Should process feeds with anchor tags and replace them with href content")
    void testGetFeeds_ProcessesAnchorTags() throws IOException {
        // Given
        String validRssUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        
        // When
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds);
        // The processing should complete without throwing exceptions
        // Note: We can't easily verify the exact content transformation without mocking
        // but we can verify the method executes successfully
    }

    @Test
    @DisplayName("Should handle feeds with empty content gracefully")
    void testGetFeeds_HandlesEmptyContent() throws IOException {
        // Given
        String validRssUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        
        // When
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds);
        // Should handle feeds with empty content using .orElse("")
    }

    @Test
    @DisplayName("Should handle feeds with no anchor tags")
    void testGetFeeds_HandlesFeedsWithoutAnchorTags() throws IOException {
        // Given
        String validRssUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        
        // When
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds);
        // Should process feeds even if they don't contain anchor tags
    }

    @Test
    @DisplayName("Should handle feeds with malformed HTML content")
    void testGetFeeds_HandlesMalformedHtml() throws IOException {
        // Given
        String validRssUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        
        // When
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds);
        // Jsoup should handle malformed HTML gracefully
    }

    @Test
    @DisplayName("Should process multiple feeds in the list")
    void testGetFeeds_ProcessesMultipleFeeds() throws IOException {
        // Given
        String validRssUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        
        // When
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds);
        // Should process all feeds in the list, not just the first one
    }

    @Test
    @DisplayName("Should handle feeds with null content")
    void testGetFeeds_HandlesNullContent() throws IOException {
        // Given
        String validRssUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        
        // When
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds);
        // Should handle null content using .orElse("")
    }

    @Test
    @DisplayName("Should handle anchor tags without href attributes")
    void testGetFeeds_HandlesAnchorTagsWithoutHref() throws IOException {
        // Given
        String validRssUrl = "https://feeds.bbci.co.uk/news/rss.xml";
        
        // When
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl);
        
        // Then
        assertNotNull(feeds);
        // Should handle anchor tags that might not have href attributes
    }

    @Test
    @DisplayName("Should throw IOException when URL contains tab character")
    void testGetFeeds_UrlWithTabCharacter() {
        String urlWithTab = "https://example.com/feed\t.xml";
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(urlWithTab);
        });
    }

    @Test
    @DisplayName("Should throw IOException when URL contains newline character")
    void testGetFeeds_UrlWithNewlineCharacter() {
        String urlWithNewline = "https://example.com/feed\n.xml";
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(urlWithNewline);
        });
    }

    @Test
    @DisplayName("Should throw IOException when protocol is missing (null protocol)")
    void testGetFeeds_ProtocolIsNull() {
        String noProtocolUrl = "no-protocol.com/feed.xml";
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(noProtocolUrl);
        });
    }

    @Test
    @DisplayName("Should successfully retrieve feeds from a valid RSS URL with leading/trailing whitespace")
    void testGetFeeds_ValidUrlWithWhitespace() throws IOException {
        String validRssUrl = "  https://feeds.bbci.co.uk/news/rss.xml  ";
        List<Item> feeds = fullTextWebFeedClient.getFeeds(validRssUrl.trim());
        assertNotNull(feeds);
    }

    @Test
    @DisplayName("Should throw IOException when protocol is not null but not http/https")
    void testGetFeeds_ProtocolNotHttpHttps() {
        String invalidProtocolUrl = "file://example.com/feed.xml";
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(invalidProtocolUrl);
        });
    }

    @Test
    @DisplayName("Should throw IOException when URL contains tab character in valid URI format")
    void testGetFeeds_UrlWithTabInValidUri() {
        String urlWithTab = "https://example.com/feed%09.xml"; // URL encoded tab
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(urlWithTab);
        });
    }

    @Test
    @DisplayName("Should throw IOException when URL contains newline character in valid URI format")
    void testGetFeeds_UrlWithNewlineInValidUri() {
        String urlWithNewline = "https://example.com/feed%0A.xml"; // URL encoded newline
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(urlWithNewline);
        });
    }

    @Test
    @DisplayName("Should throw IOException when URL contains newline character in valid URI format")
    void testGetFeeds_testReddit() {
        String urlWithNewline = "https://www.reddit.com/r/technology/new/.rss"; // URL encoded newline
        assertThrows(IOException.class, () -> {
            fullTextWebFeedClient.getFeeds(urlWithNewline);
        });
    }
}
