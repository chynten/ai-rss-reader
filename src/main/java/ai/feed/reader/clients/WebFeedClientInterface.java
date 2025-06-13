package ai.feed.reader.clients;

import com.apptasticsoftware.rssreader.Item;

import java.io.IOException;
import java.util.List;

public interface WebFeedClientInterface {
    List<Item> getFeeds(String feedURL) throws IOException;
}
