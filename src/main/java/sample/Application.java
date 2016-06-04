package sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.sql.DataSource;
import java.time.LocalDateTime;

/**
 * アプリケーションのエントリーポイントとなるクラスです。
 *
 * このクラスを実行する事で用意されているAPIがすべて使用可能になります。
 *
 * @author backpaper0
 */
@SpringBootApplication
public class Application {

    @Autowired
    TweetService tweetService;
    @Autowired
    AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * LocalDateTimeをJSONに変換するためカスタマイズしたObjectMapperを返します。
     *
     * @return
     */
    @Bean
    ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().serializerByType(
                LocalDateTime.class, new LocalDateTimeJsonSerializer()).build();
    }

    @Autowired
    DataSource dataSource;

    /**
     * 動作確認のためのテストデータを作成します。
     *
     * @return
     */
    @Bean
    ApplicationListener<ContextRefreshedEvent> listener() {
        return new ApplicationListener<ContextRefreshedEvent>() {

            @Override
            public void onApplicationEvent(ContextRefreshedEvent event) {
                Flyway flyway = new Flyway();
                flyway.setDataSource(dataSource);
                flyway.migrate();

                accountService.signUp("hogekun");
                tweetService.tweet("hogekun", "こんにちは！");
            }
        };
    }
}
