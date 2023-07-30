package geo.geopointsTgBot;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInputHandler {
    Menu menu;
    Json json;


    public void handleUserInput(String msg, long chatId) throws IOException {
        String[] temp = msg.split("[,\\s]+");
        String jsonString = "{\n\"x\":\"" + temp[0].trim() + "\",\n\"y\":\"" + temp[1].trim() +
                "\",\n\"radius\":\"" + menu.getRadius() + "\"\n}";
        JSONArray ggsList = new JSONArray();
        JSONArray gnsList = new JSONArray();
        if (menu.isGgs()) {
            String GGS_URL = "http://194.87.199.170:8888/ggs";
            ggsList = new JSONArray(json.sendJsonToUrl(jsonString, GGS_URL));
        }
        if (menu.isGns()) {
            String GNS_URL = "http://194.87.199.170:8888/gns";
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
}
