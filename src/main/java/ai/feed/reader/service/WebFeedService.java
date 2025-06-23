package ai.feed.reader.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebFeedService {
    @Autowired
    OpenAIService openAIService;

    @Autowired
    WebPageService webPageService;

    RssReader rssReader;

    /**
     * Fetches and processes RSS feed items from the given feed URL.
     *
     * <p>
     * This method performs the following:
     * <ul>
     *   <li>Validates the provided feed URL for null, emptiness, and proper format.</li>
     *   <li>Ensures the protocol is either HTTP or HTTPS.</li>
     *   <li>Reads the RSS feed using {@link RssReader} and limits the result to the first 5 items.</li>
     *   <li>If {@code fullText} is true, attempts to fetch and replace anchor tag content with the full web page content for each feed item.</li>
     *   <li>If {@code includeAITags} is true, may enhance items with AI-generated tags (implementation dependent).</li>
     * </ul>
     * </p>
     *
     * @param feedURL        the URL of the RSS feed to fetch
     * @param fullText       whether to enhance feed items with full web page content
     * @param includeAITags  whether to include AI-generated tags for each article
     * @return a list of processed {@link Item} objects from the feed
     * @throws IOException if the feed URL is invalid or fetching/parsing fails
     */
    public List<Item> getFeeds(String feedURL, boolean fullText, boolean includeAITags) throws IOException {
        if (feedURL == null) {
            throw new IOException("Feed URL cannot be null");
        }

        if (feedURL.trim().isEmpty()) {
            throw new IOException("Feed URL cannot be empty");
        }

        if (feedURL.contains(" ") || feedURL.contains("\t") || feedURL.contains("\n")) {
            throw new IOException("Malformed URL: URL contains invalid characters");
        }

        // URL validation and parsing
        URI uri;
        try {
            uri = new URI(feedURL);
        } catch (URISyntaxException e) {
            throw new IOException("Malformed URL: " + feedURL);
        }
        String protocol = uri.getScheme();
        if (!protocol.equalsIgnoreCase("http") && !protocol.equalsIgnoreCase("https")) {
            throw new IOException("Invalid protocol. Only HTTP and HTTPS are supported");
        }

        if (rssReader == null) {
            rssReader = new RssReader();
        }

        List<Item> allFeeds = rssReader.read(feedURL).toList();
        List<Item> feeds = allFeeds.subList(0, Math.min(5, allFeeds.size()));
        log.info("Total Feed Size: " + feeds.size());

        if (fullText) {
            for (Item feed : feeds) {
                Document feedContent = Jsoup.parse(feed.getContent().orElse(""));

                for (Element a : feedContent.getElementsByTag("a")) {
                    String href = a.hasAttr("href") ? a.attr("href") : null;

                    log.trace("######### Href: " + href);

                    if (href != null && !href.isEmpty() && !feed.getLink().orElse("").equals(href)) {

                        Document hrefDocument = webPageService.getWebPageContent(href);
                        a.replaceWith(hrefDocument.body());

                    }
                    feed.setContent(feedContent.html());
                }

                log.trace(feedContent.html());
                String content = webPageService.getReadableContent(feed.getLink().orElse(""), feedContent.html());

                feed.setContent(content);
            }
        }
        if (includeAITags) {
            feeds.stream().forEach(feed -> {
                List<String> tags = openAIService.fetchTags(feed.getContent().orElse(""));
            });
        }
        return feeds;
    }
}
