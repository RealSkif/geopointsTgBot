package geo.geopointsTgBot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class GeopointsTgBotApplication {
    public static void main(String[] args) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot(new Json());
        botsApi.registerBot(bot);

    }
}