package ai.feed.reader.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import net.dankito.readability4j.Readability4J;
import net.dankito.readability4j.Article;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class WebPageServiceTest {

    @Test
    void testGetWebPageContent() throws IOException {
        String url = "http://example.com";
        Document mockDoc = mock(Document.class);
        try (MockedStatic<Jsoup> jsoupMockedStatic = mockStatic(Jsoup.class)) {
            jsoupMockedStatic.when(() -> Jsoup.connect(url)).thenReturn(mock(org.jsoup.Connection.class));
            org.jsoup.Connection connection = Jsoup.connect(url);
            when(connection.userAgent(anyString())).thenReturn(connection);
            when(connection.timeout(anyInt())).thenReturn(connection);
            when(connection.get()).thenReturn(mockDoc);
            WebPageService service = new WebPageService();
            Document result = service.getWebPageContent(url);
            assertNotNull(result);
        }
    }

    // @Test
    // void testGetReadableContent() throws IOException {
    //     String url = "http://example.com";
    //     String content = "<html></html>";
    //     Readability4J mockReadability = mock(Readability4J.class);
    //     Article mockArticle = mock(Article.class);
    //     when(mockReadability.parse()).thenReturn(mockArticle);
    //     when(mockArticle.getContent()).thenReturn("readable content");
    //     try (MockedStatic<Readability4J> readabilityMockedStatic = mockStatic(Readability4J.class)) {
    //         readabilityMockedStatic.when(() -> new Readability4J(url, content)).thenReturn(mockReadability);
    //         WebPageService service = new WebPageService();
    //         String result = service.getReadableContent(url, content);
    //         assertEquals("readable content", result);
    //     }
    // }
} 