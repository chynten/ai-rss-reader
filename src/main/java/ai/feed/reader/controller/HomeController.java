package ai.feed.reader.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ai.feed.reader.service.WebFeedService;
import ai.feed.reader.to.WebFeed;

@Controller
public class HomeController {

    @Autowired
    WebFeedService webFeedService;

    @GetMapping("/")
    public String index(Model model) throws IOException, NoSuchAlgorithmException {
        
        List<WebFeed> webFeeds = webFeedService.getFeeds("https://www.reddit.com/r/technology/new/.rss", false, false);

        model.addAttribute("webFeeds", webFeeds);
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

}
