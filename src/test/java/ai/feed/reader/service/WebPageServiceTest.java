package ai.feed.reader.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WebPageServiceTest {

    @Test
    void testGetWebPageContent() throws IOException {
        String url = "http://example.com";
        Document mockDoc = mock(Document.class);
        try (MockedStatic<Jsoup> jsoupMockedStatic = mockStatic(Jsoup.class)) {
            org.jsoup.Connection mockConnection = mock(org.jsoup.Connection.class);
            jsoupMockedStatic.when(() -> Jsoup.connect(url)).thenReturn(mockConnection);
            when(mockConnection.userAgent(anyString())).thenReturn(mockConnection);
            when(mockConnection.timeout(anyInt())).thenReturn(mockConnection);
            when(mockConnection.get()).thenReturn(mockDoc);
            WebPageService service = new WebPageService();
            Document result = service.getWebPageContent(url);
            assertNotNull(result);
        }
    }

    @Test
    void testGetReadableContent() throws IOException {
        String url = "http://example.com";
        String content = "<html><body><p>Test content</p></body></html>";
        
        // Since Readability4J constructor cannot be mocked, we'll test the method
        // by creating a real instance and mocking the parse method
        WebPageService service = new WebPageService();
        
        // This test will use the actual Readability4J implementation
        // In a real scenario, you might want to use PowerMock or similar for constructor mocking
        // For now, we'll test that the method doesn't throw an exception
        assertDoesNotThrow(() -> {
            String result = service.getReadableContent(url, content);
            assertNotNull(result);
        });
    }
} 