package ai.feed.reader.clients;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

class DefaultWebFeedClient implements WebFeedClientInterface {

    @Override
    public List<Item> getFeeds(String feedURL) throws IOException {
        // Validate input
        if (feedURL == null) {
            throw new IOException("Feed URL cannot be null");
        }
        
        if (feedURL.trim().isEmpty()) {
            throw new IOException("Feed URL cannot be empty");
        }
        
        // Validate URL format and protocol
        try {
            URI uri = new URI(feedURL);
            String protocol = uri.getScheme();
            
            // Only allow HTTP and HTTPS protocols
            if (protocol == null || (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https"))) {
                throw new IOException("Invalid protocol. Only HTTP and HTTPS are supported");
            }
            
            // Check if URL contains special characters that might cause issues
            if (feedURL.contains(" ") || feedURL.contains("\t") || feedURL.contains("\n")) {
                throw new IOException("URL contains invalid characters");
            }
            
        } catch (URISyntaxException e) {
            throw new IOException("Malformed URL: " + feedURL, e);
        }
        
        RssReader rssReader = new RssReader();
        return rssReader.read(feedURL).toList();
    }

}
