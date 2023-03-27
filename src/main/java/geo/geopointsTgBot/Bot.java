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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;


public class Bot extends TelegramLongPollingBot {

    private Json json;

    public Json getJson() {
        return json;
    }

    private String greeting = "Привет! Для получения kml файла с пунктами отправьте " +
            "сообщение с географическими координатами и радиусом поиска в формате:\n" +
            "широта, долгота, радиус поиска (например '55.168949, 61.212220, 10'";
    private String wrongInput = "Неправильно введен запрос, убедитесь, что он соотвествует следующему формату:\n" +
            "\"широта, долгота, радиус поиска (например '55.168949, 61.212220, 10'";


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

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        String chatId = String.valueOf(msg.getChatId());
        sendText(msg.getChatId(), greeting);

        String[] temp = msg.getText().split(",");
        if (temp.length != 3)
            sendText(msg.getChatId(), wrongInput);
        else {
            String jsonString = "{\n\"x\":\"" + temp[0].trim() + "\",\n\"y\":\"" + temp[1].trim() +
                    "\",\n\"radius\":\"" + temp[2].trim() + "\"\n}";
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