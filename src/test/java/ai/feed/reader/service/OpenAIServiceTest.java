package ai.feed.reader.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;

@ExtendWith(MockitoExtension.class)
class OpenAIServiceTest {

    @InjectMocks
    OpenAIService openAIService;

    @Mock
    ChatClient.Builder chatClientBuilder;
    @Mock
    ChatClient chatClient;
    @Mock
    ChatClient.ChatClientRequestSpec requestSpec;
    @Mock
    ChatClient.CallResponseSpec callResponseSpec;

    @BeforeEach
    void setUp() {
        openAIService.chatClientBuilder = chatClientBuilder;
    }

    @Test
    void testFetchTags() {
        String content = "test content";
        String jsonTags = "[\"tag1\",\"tag2\"]";
        
        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt(any(Prompt.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn(jsonTags);
        
        List<String> tags = openAIService.fetchTags(content);
        assertNotNull(tags);
        assertEquals(1, tags.size());
        assertEquals(jsonTags, tags.get(0));
    }
} 