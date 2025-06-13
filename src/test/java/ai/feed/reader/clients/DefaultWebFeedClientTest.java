package ai.feed.reader.clients;

import com.apptasticsoftware.rssreader.Item;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultWebFeedClientTest {

    @Test
    public void testGetFeeds_NotNull() throws IOException {
        WebFeedClient webFeedClient = new WebFeedClient();
        WebFeedClientInterface webFeedClientInt = webFeedClient.getWebFeedClient(false);
        List<Item> feeds = webFeedClientInt.getFeeds("https://www.reddit.com/r/technology/new/.rss");
        assertNotNull(feeds);
        assertFalse(feeds.isEmpty());
    }
}