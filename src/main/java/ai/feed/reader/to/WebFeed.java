package ai.feed.reader.to;

import com.apptasticsoftware.rssreader.Item;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebFeed {
    private Item item;
    private String id;
}
