package ai.feed.reader.clients;

public abstract class AbstractWebFeedClient implements WebFeedClientInterface {

    protected String getFeedURL(String feedURL){
        return feedURL;
    }
}
