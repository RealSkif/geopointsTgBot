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
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Bot extends TelegramLongPollingBot {
    private Json json;
    Menu menu;
    private final String GGS_URL = "http://194.87.199.170:8888/ggs";
    private final String GNS_URL = "http://194.87.199.170:8888/gns";

    public Json getJson() {
        return json;
    }

    private final String wrongInput = "Неправильно введен запрос. Убедитесь, что он соотвествует следующему формату:\n" +
            "\"широта, долгота (например '55.168949, 61.212220')";

    private final String outOfRange = "Введенные координаты выходят за границы Российской Федерации." +
            " Для РФ диапазон широт от 41 до 82 градусов, долгот от 19 до 180";

    public Bot() {
    }

    public Bot(Json json) {
        this.json = json;
        this.menu = new Menu();
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
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void onUpdateReceived(Update update) {

        long chatId;
        String msg;
        try {
            if (update.hasCallbackQuery()) {
                chatId = update.getCallbackQuery().getMessage().getChatId();
                menu.handleCallback(update);
                execute(menu.inlineMenu(chatId, menu.isGgs(), menu.isGns(), menu.getRadius()));
            } else {
                msg = update.getMessage().getText();
                chatId = update.getMessage().getChatId();
                execute(menu.replyMenu(chatId));
                if (msg.equals("Настройки"))
                    execute(menu.inlineMenu(chatId, menu.isGgs(), menu.isGns(), menu.getRadius()));
                if (validateInput(msg) &&
                        (!msg.equals("Настройки") &&
                                !msg.equals("/start"))) {
                    sendText(chatId, wrongInput);
                    if (validateCoords(msg))
                        sendText(chatId, outOfRange);
                } else {
                    String[] temp = msg.split(",");
                    String jsonString = "{\n\"x\":\"" + temp[0].trim() + "\",\n\"y\":\"" + temp[1].trim() +
                            "\",\n\"radius\":\"" + menu.getRadius() + "\"\n}";
                    JSONArray ggsList = new JSONArray();
                    JSONArray gnsList = new JSONArray();
                    if (menu.isGgs()) {
                        ggsList = new JSONArray(json.sendJsonToUrl(jsonString, GGS_URL));
                    }
                    if (menu.isGns()) {
                        gnsList = new JSONArray(json.sendJsonToUrl(jsonString, GNS_URL));
                    }
                    File fileGgs;
                    fileGgs = JsonToKmlTelegramSender.sendJsonListAsKmlFileInTelegram(ggsList, gnsList);
                    sendKml(fileGgs, String.valueOf(chatId));
                    fileGgs.delete();
                }
            }
        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateInput(String input) {
        String[] temp = input.split(",");
        if (temp.length != 2) return false;
        Pattern pattern = Pattern.compile("\\d{2,3}.\\d{0,10}");
        Matcher matcher1 = pattern.matcher(temp[0]);
        Matcher matcher2 = pattern.matcher(temp[1]);
        boolean valid = true;
        if (!matcher1.matches()) valid = false;
        if (!matcher2.matches()) valid = false;
        return valid;
    }

    public boolean validateCoords(String input) {
        String[] temp = input.split(",");
        return !(Double.parseDouble(temp[0]) < 41.0) && !(Double.parseDouble(temp[0]) > 82.0)
                && !(Double.parseDouble(temp[1]) < 19) && !(Double.parseDouble(temp[0]) > 180);
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