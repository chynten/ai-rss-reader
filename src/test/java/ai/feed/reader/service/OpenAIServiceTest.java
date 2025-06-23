package ai.feed.reader.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

    @Test
    void testFetchTags() {
        String content = "test content";
        String jsonTags = "tag1,tag2";
        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt(any(Prompt.class))).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(callResponseSpec);
        when(callResponseSpec.content()).thenReturn(jsonTags);
        List<String> tags = openAIService.fetchTags(content);
        assertNotNull(tags);
    }
} 