package sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * ツイートを取得したり投稿するAPIを提供するコントローラーです。
 * 
 * @author backpaper0
 *
 */
@RestController
public class TweetController {

    @Autowired
    TweetService service;

    /**
     * タイムライン(投稿されたツイートの一覧)を取得するAPIです。
     * 
     * @return タイムライン
     */
    @RequestMapping(value = "/timeline", method = RequestMethod.GET)
    public Timeline getTimeline() {
        Timeline timeline = new Timeline();
        timeline.tweets = service.getTimeline().stream()
                .map(TweetView::fromTweet).collect(Collectors.toList());
        return timeline;
    }

    /**
     * ツイートを投稿するAPIです。
     * 
     * @param userId ユーザーID
     * @param text ツイートの内容
     * @return 投稿された内容(サーバー側で振られたツイートの主キーを含む)
     */
    @RequestMapping(value = "/tweet", method = RequestMethod.POST)
    public TweetView tweet(@RequestParam String userId,
            @RequestParam String text) {
        Tweet tweet = service.tweet(userId, text);
        return TweetView.fromTweet(tweet);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleAccountNotFound(AccountNotFoundException e) {
    }
}
