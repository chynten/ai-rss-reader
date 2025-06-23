package ai.feed.reader.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    @Autowired
    ChatClient.Builder chatClientBuilder;

    public List<String> fetchTags(String content) {
        PromptTemplate pt = new PromptTemplate(
                """
                         You are a bot in a read-it-later app and your responsibility is to help with automatic tagging.
                         Please analyze the text between the sentences "CONTENT START HERE" and "CONTENT END HERE" and suggest relevant tags that describe its key themes, topics, and main ideas. The rules are:
                         - Aim for a variety of tags, including broad categories, specific keywords, and potential sub-genres.
                         - The tags language must be in english.
                         - If it's a famous website you may also include a tag for the website. If the tag is not generic enough, don't include it.
                         - The content can include text for cookie consent and privacy policy, ignore those while tagging.
                         - Aim for 3-5 tags.
                         - If there are no good tags, leave the array empty.

                         CONTENT START HERE

                        """
                        + content +
                        """ 
                        CONTENT END HERE
                        You must respond in JSON with the key "tags" and the value is an array of string tags.
                        """);
        ChatClient chatClient = chatClientBuilder.build();
        String tags = chatClient.prompt(pt.create()).call().content();
        return Arrays.asList(tags);
    }

}