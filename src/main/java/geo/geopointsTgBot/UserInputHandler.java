package geo.geopointsTgBot;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class UserInputHandler {
    Bot bot;

    public UserInputHandler() {
        bot = new Bot();
    }

    public void handleUserMessage(String msg, long chatId, Menu menu, Json json) throws TelegramApiException, IOException {
        String outOfRange = "Введенные координаты выходят за границы Российской Федерации." + " Для РФ диапазон широт от 41 до 82 градусов, долгот от 19 до 180";
        String wrongInput = "Неправильно введен запрос. Убедитесь, что он соотвествует следующему формату:\n" + "\"широта, долгота (например '55.168949, 61.212220')";
        bot.execute(menu.replyMenu(chatId));
        if (msg.equals("Настройки"))
            bot.execute(menu.inlineMenu(chatId, menu.isGgs(), menu.isGns(), menu.getRadius()));
        if (!validateInput(msg) && (!msg.equals("Настройки") && !msg.equals("/start"))) {
            sendText(chatId, wrongInput);
            if (!validateCoords(msg)) sendText(chatId, outOfRange);
        } else {
            handleUserInput(msg, chatId, menu, json);
        }
    }

    public void handleUserInput(String msg, long chatId, Menu menu, Json json) throws IOException {
        String[] temp = msg.split("[,\\s]+");
        String jsonString = "{\n\"x\":\"" + temp[0].trim() + "\",\n\"y\":\"" + temp[1].trim() +
                "\",\n\"radius\":\"" + menu.getRadius() + "\"\n}";
        JSONArray ggsList = new JSONArray();
        JSONArray gnsList = new JSONArray();
        if (menu.isGgs()) {
            String GGS_URL = "http://193.176.158.169:8888/ggs";
            ggsList = new JSONArray(json.sendJsonToUrl(jsonString, GGS_URL));
        }
        if (menu.isGns()) {
            String GNS_URL = "http://193.176.158.169:8888/gns";
            gnsList = new JSONArray(json.sendJsonToUrl(jsonString, GNS_URL));
        }
        File fileGgs;
        fileGgs = KmlHandler.createKML(ggsList, gnsList);
        KmlHandler.sendKml(fileGgs, String.valueOf(chatId));
        fileGgs.delete();
    }

    public boolean validateInput(String input) {
        String[] temp = input.split("[,\\s]+");
        if (temp.length != 2) return false;
        Pattern pattern = Pattern.compile("\\d{2,3}\\.\\d+");
        Matcher matcher1 = pattern.matcher(temp[0]);
        Matcher matcher2 = pattern.matcher(temp[1]);
        return matcher1.matches() && matcher2.matches();
    }

    public boolean validateCoords(String input) {
        double minLongitude = 19;
        double maxLongitude = 180;
        double minLatitude = 41;
        double maxLatitude = 82;
        String[] temp = input.split("[,\\s]+");
        return !(Double.parseDouble(temp[0]) < minLatitude) && !(Double.parseDouble(temp[0]) > maxLatitude)
                && !(Double.parseDouble(temp[1]) < minLongitude) && !(Double.parseDouble(temp[0]) > maxLongitude);
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).build();
        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
