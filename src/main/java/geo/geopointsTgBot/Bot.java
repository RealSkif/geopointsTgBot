package geo.geopointsTgBot;

import org.json.JSONArray;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Bot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "GGS_Points_bot";
    private static final String BOT_TOKEN = "6054614033:AAFyCqN0X42aLqczS4zux9m_VCAAsjk6EAM";
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
        var user = msg.getFrom();
        var id = user.getId();
        String chatId = String.valueOf(msg.getChatId());

        String[] temp = msg.getText().split(",");
        String jsonString = "{\n\"x\":\"" + temp[0].trim() + "\",\n\"y\":\"" + temp[1].trim() + "\"\n}";
        System.out.println(jsonString);
        System.out.println(json.sendJsonToUrl(jsonString));
        JSONArray jsonList = new JSONArray(json.sendJsonToUrl(jsonString));
        try {
            JsonToKmlTelegramSender.sendJsonListAsKmlFileInTelegram(jsonList, BOT_TOKEN, chatId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }}