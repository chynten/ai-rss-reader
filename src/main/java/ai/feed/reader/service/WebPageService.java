package ai.feed.reader.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import net.dankito.readability4j.Readability4J;

@Service
public class WebPageService {

    public Document getWebPageContent(String url) throws IOException {

        Document hrefDocument = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:138.0) Gecko/20100101 Firefox/138.0")
                .timeout(5000)
                .get();
        return hrefDocument;

    }

    public String getReadableContent(String url, String content) throws IOException {
        Readability4J readability = new Readability4J(url, content);
        return readability.parse().getContent();
    }
}
