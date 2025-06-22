package ai.feed.reader.clients;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;

import lombok.extern.slf4j.Slf4j;

/**
 * FullTextWebFeedClient - A web feed client that retrieves RSS feeds and extracts full text content
 * from linked articles by following anchor tags and parsing the full HTML content.
 * 
 * This client extends the basic RSS reading functionality by:
 * 1. Validating feed URLs for security and format compliance
 * 2. Reading RSS feeds using the RssReader library
 * 3. Parsing feed content to find anchor tags with article links
 * 4. Fetching full article content from those links
 * 5. Replacing anchor tags with the full article content
 */
@Slf4j
class FullTextWebFeedClient implements WebFeedClientInterface {

    /**
     * Retrieves RSS feeds from the specified URL and enhances them with full text content.
     * 
     * @param feedURL The URL of the RSS feed to retrieve
     * @return List of RSS feed items with enhanced content
     * @throws IOException if the feed URL is invalid, network issues occur, or content parsing fails
     */
    @Override
    public List<Item> getFeeds(String feedURL) throws IOException {
        // Validate input - Check if feedURL is null
        if (feedURL == null) {
            throw new IOException("Feed URL cannot be null");
        }

        // Validate input - Check if feedURL is empty or contains only whitespace
        if (feedURL.trim().isEmpty()) {
            throw new IOException("Feed URL cannot be empty");
        }

        // Validate URL format and protocol
        try {
            URI uri = new URI(feedURL);
            String protocol = uri.getScheme();

            // Only allow HTTP and HTTPS protocols for security reasons
            if (protocol == null || (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https"))) {
                throw new IOException("Invalid protocol. Only HTTP and HTTPS are supported");
            }

            // Check if URL contains special characters that might cause issues
            // This prevents potential injection attacks and malformed URLs
            if (feedURL.contains(" ") || feedURL.contains("\t") || feedURL.contains("\n")) {
                throw new IOException("URL contains invalid characters");
            }

        } catch (URISyntaxException e) {
            throw new IOException("Malformed URL: " + feedURL, e);
        }

        // Initialize RSS reader and fetch feeds
        RssReader rssReader = new RssReader();
        // Limit to first feed item for processing (subList(0, 1))
        List<Item> feeds = rssReader.read(feedURL).toList().subList(0, 1);
        log.info("######### Total Feed Size: " + feeds.size());
        
        // Iterate through each feed item to enhance content with full text
        for (Item feed : feeds) {
            // Parse the feed content as HTML to extract anchor tags
            Document feedContent = Jsoup.parse(feed.getContent().orElse(""));
            
            // Find all anchor tags in the feed content that might contain article links
            for (Element a : feedContent.getElementsByTag("a")) {
                // Extract the href attribute from the anchor tag
                String href = a.attribute("href").getValue();

                // Check if the href attribute exists and is not empty
                // This ensures we only process valid links
                if (href != null && !href.isEmpty()) {

                    // Fetch the full content from the linked article
                    // Set a 5-second timeout to prevent hanging on slow responses
                    Document hrefDocument = Jsoup.connect(href).timeout(5000).get();
                    // Replace the anchor tag with the full body content of the linked article
                    a.replaceWith(hrefDocument.body());

                }
                // Log the current state of the feed content after processing each anchor tag
                log.info(feedContent.html());

            }

            // Extract the final HTML content from the feed item
            String htmlContent = feed.getContent().orElse("");
            // TODO: generate markdown from the enhanced HTML content
        }
        return feeds;
    }

}
