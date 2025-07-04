package ai.feed.reader.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.apptasticsoftware.rssreader.Item;
import com.apptasticsoftware.rssreader.RssReader;

import ai.feed.reader.to.WebFeed;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WebFeedServiceTest {
    @Mock
    OpenAIService openAIService;
    @Mock
    WebPageService webPageService;
    @Mock
    RssReader rssReader;
    @Mock
    AsyncMessageService asyncMessageService;
    WebFeedService webFeedService;

    @BeforeEach
    void setUp() {
        webFeedService = new WebFeedService();
        webFeedService.openAIService = openAIService;
        webFeedService.webPageService = webPageService;
        webFeedService.asyncMessageService = asyncMessageService;
    }

    @Test
    void testGetFeeds_nullUrl() {
        Exception ex = assertThrows(IOException.class, () -> webFeedService.getFeeds(null, false, false));
        assertTrue(ex.getMessage().contains("null"));
    }

    @Test
    void testGetFeeds_emptyUrl() {
        Exception ex = assertThrows(IOException.class, () -> webFeedService.getFeeds("   ", false, false));
        assertTrue(ex.getMessage().contains("empty"));
    }

    @Test
    void testGetFeeds_malformedUrl_invalidChars() {
        Exception ex = assertThrows(IOException.class, () -> webFeedService.getFeeds("http://bad url.com", false, false));
        assertTrue(ex.getMessage().contains("invalid characters"));
    }

    @Test
    void testGetFeeds_malformedUrl_badSyntax() {
        Exception ex = assertThrows(IOException.class, () -> webFeedService.getFeeds("ht!tp://bad-url", false, false));
        assertTrue(ex.getMessage().contains("Malformed URL"));
    }

    @Test
    void testGetFeeds_invalidProtocol() {
        Exception ex = assertThrows(IOException.class, () -> webFeedService.getFeeds("ftp://example.com", false, false));
        assertTrue(ex.getMessage().contains("Invalid protocol"));
    }

    @Test
    void testGetFeeds_success_basic() throws IOException, NoSuchAlgorithmException {
        String url = "http://example.com/rss";
        Item item = mock(Item.class);
        when(item.getContent()).thenAnswer(invocation -> Optional.of("content"));
        when(item.getLink()).thenAnswer(invocation -> Optional.of("http://example.com/article"));
        List<Item> items = Arrays.asList(item, item, item, item, item, item); // 6 items
        RssReader mockRssReader = mock(RssReader.class);
        when(mockRssReader.read(url)).thenReturn(items.stream());
        webFeedService.rssReader = mockRssReader;
        List<WebFeed> result = webFeedService.getFeeds(url, false, false);
        assertEquals(5, result.size());
    }

    @Test
    void testGetFeeds_fullText() throws IOException, NoSuchAlgorithmException {
        String url = "http://example.com/rss";
        Item item = mock(Item.class);
        when(item.getContent()).thenAnswer(invocation -> Optional.of("<a href=\"http://other.com\">link</a>"));
        when(item.getLink()).thenAnswer(invocation -> Optional.of("http://example.com/article"));
        List<Item> items = Collections.singletonList(item);
        RssReader mockRssReader = mock(RssReader.class);
        when(mockRssReader.read(url)).thenReturn(items.stream());
        webFeedService.rssReader = mockRssReader;
        // Mock the Document and Element for Jsoup.parse and .body()
        Document mockFeedContent = mock(Document.class);
        org.jsoup.nodes.Element mockAnchor = mock(org.jsoup.nodes.Element.class);
        org.jsoup.nodes.Element mockBody = mock(org.jsoup.nodes.Element.class);
        org.jsoup.select.Elements mockElements = mock(org.jsoup.select.Elements.class);
        java.util.Iterator<org.jsoup.nodes.Element> mockIterator = mock(java.util.Iterator.class);
        when(mockElements.iterator()).thenReturn(mockIterator);
        when(mockIterator.hasNext()).thenReturn(true, false);
        when(mockIterator.next()).thenReturn(mockAnchor);
        when(mockFeedContent.getElementsByTag("a")).thenReturn(mockElements);
        when(mockAnchor.hasAttr("href")).thenReturn(true);
        when(mockAnchor.attr("href")).thenReturn("http://other.com");
        when(webPageService.getWebPageContent(anyString())).thenReturn(mockFeedContent);
        when(mockFeedContent.body()).thenReturn(mockBody);
        doNothing().when(mockAnchor).replaceWith(mockBody);
        when(mockFeedContent.html()).thenReturn("html");
        when(webPageService.getReadableContent(anyString(), anyString())).thenReturn("readable");
        try (MockedStatic<org.jsoup.Jsoup> jsoupMockedStatic = mockStatic(org.jsoup.Jsoup.class)) {
            jsoupMockedStatic.when(() -> org.jsoup.Jsoup.parse(anyString())).thenReturn(mockFeedContent);
            List<WebFeed> result = webFeedService.getFeeds(url, true, false);
            assertEquals(1, result.size());
        }
    }

    @Test
    void testGetFeeds_includeAITags() throws IOException, NoSuchAlgorithmException {
        String url = "http://example.com/rss";
        Item item = mock(Item.class);
        when(item.getContent()).thenAnswer(invocation -> Optional.of("content"));
        when(item.getLink()).thenAnswer(invocation -> Optional.of("https://www.reddit.com/r/technology/new/.rss"));
        List<Item> items = Collections.singletonList(item);
        RssReader mockRssReader = mock(RssReader.class);
        when(mockRssReader.read(url)).thenReturn(items.stream());
        webFeedService.rssReader = mockRssReader;
        when(openAIService.fetchTags(anyString())).thenReturn(Arrays.asList("tag1", "tag2"));
        List<WebFeed> result = webFeedService.getFeeds(url, false, true);
        assertEquals(1, result.size());
        verify(asyncMessageService, atLeastOnce()).sendRssFeedMessage(any(Item.class));
    }
} 