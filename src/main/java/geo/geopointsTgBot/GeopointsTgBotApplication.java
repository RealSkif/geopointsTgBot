package geo.geopointsTgBot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Arrays;


@SpringBootApplication
public class GeopointsTgBotApplication {

	public static void main(String[] args) throws Exception {
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		Bot bot = new Bot(new Json());
		botsApi.registerBot(bot);

	}

}
