package ai.feed.reader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ai.feed.reader"})
public class AIWebFeedReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(AIWebFeedReaderApplication.class, args);
    }

}
