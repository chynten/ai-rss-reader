package ai.feed.reader;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class AIWebFeedReaderApplicationTest {
    @Test
    void testMain() {
        assertDoesNotThrow(() -> AIWebFeedReaderApplication.main(new String[]{}));
    }
} 