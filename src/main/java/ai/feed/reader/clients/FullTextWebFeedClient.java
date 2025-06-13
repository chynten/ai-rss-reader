package ai.feed.reader.clients;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;

import java.io.IOException;
import java.util.List;

class FullTextWebFeedClient extends AbstractWebFeedClient {


    @Override
    public List<Item> getFeeds(String feedURL) throws IOException {
        RssReader rssReader = new RssReader();
        return rssReader.read(feedURL).toList();
    }
}
