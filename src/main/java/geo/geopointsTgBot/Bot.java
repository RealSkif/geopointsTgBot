package geo.geopointsTgBot;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.IOException;


public class Bot extends TelegramLongPollingBot {

   private Json json;

    public Json getJson() {
        return json;
    }

    public Bot() {

    }

    public Bot(Json json) {
        this.json = json;
    }

    @Override
    public String getBotUsername() {
        return "GGS_Points_bot";
    }

    @Override
    public String getBotToken() {
        return "???";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        String chatId = String.valueOf(msg.getChatId());

        String[] temp = msg.getText().split(",");
        String jsonString = "{\n\"x\":\"" + temp[0].trim() + "\",\n\"y\":\"" + temp[1].trim() + "\"\n}";
        JSONArray jsonList = new JSONArray(json.sendJsonToUrl(jsonString));
        File file;
        try {
            file = JsonToKmlTelegramSender.sendJsonListAsKmlFileInTelegram(jsonList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sendKml(file, chatId);
        file.delete();
    }

    public void sendKml(File file, String chatId) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost upload = new HttpPost("https://api.telegram.org/bot???/sendDocument");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addBinaryBody("document", file, ContentType.DEFAULT_BINARY, file.getName());

        builder.addTextBody("chat_id", chatId);

        HttpEntity entity = builder.build();
        upload.setEntity(entity);

        HttpResponse response = null;
        try {
            response = client.execute(upload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}