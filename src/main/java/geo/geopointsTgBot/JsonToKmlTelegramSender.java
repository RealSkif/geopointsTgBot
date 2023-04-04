package geo.geopointsTgBot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonToKmlTelegramSender {

    public static File sendJsonListAsKmlFileInTelegram(JSONArray ggs, JSONArray gns) throws IOException {
        String kmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n";
        String kmlFooter = "</kml>";

        StringBuilder kmlPoints = new StringBuilder();
        String kmlPoint = "";
        for (int i = 0; i < gns.length(); i++) {
            JSONObject json = gns.getJSONObject(i);

            Double longitude = json.getDouble("latitude");
            Double latitude = json.getDouble("longitude");
            String name = (json.getString("name") != null) ? json.getString("name") : "Нет данных";
            String index = (json.getString("index") != null) ? json.getString("index") : "Нет данных";
            String mark = (json.getString("mark") != null) ? json.getString("mark") : "Нет данных";
            String centerType = (json.getString("centerType") != null) ? json.getString("centerType") : "Нет данных";
            String sighType = (json.getString("sighType") != null) ? json.getString("sighType") : "Нет данных";
            String maingeographyfeature = (json.getString("maingeographyfeature") != null) ? json.getString("maingeographyfeature") : "Нет данных";
            kmlPoint = "<Placemark>\n" +
                    "<name>" + name + "</name>\n" +
                    "<description>Индекс: " + index + ", марка: " + mark + ", тип центра: "
                    + centerType + ", оп. знак: " + sighType + ", кроки: " + maingeographyfeature + "</description>\n" +
                    "<Point>\n" +
                    "<coordinates>" + longitude + "," + latitude + "</coordinates>\n" +
                    "</Point>\n" +
                    "</Placemark>\n";

            kmlPoints.append(kmlPoint);
        }
        for (int i = 0; i < ggs.length(); i++) {
            JSONObject json = ggs.getJSONObject(i);

            Double longitude = json.getDouble("latitude");
            Double latitude = json.getDouble("longitude");
            String name = (json.getString("name") != null) ? json.getString("name") : "Нет данных";
            String index = (json.getString("index") != null) ? json.getString("index") : "Нет данных";
            String mark = (json.getString("mark") != null) ? json.getString("mark") : "Нет данных";
            String centerType = (json.getString("centerType") != null) ? json.getString("centerType") : "Нет данных";
            String sighType = (json.getString("sighType") != null) ? json.getString("sighType") : "Нет данных";
            kmlPoint = "<Placemark>\n" +
                    "<name>" + name + "</name>\n" +
                    "<description>Индекс: " + index + ", марка: " + mark + ", тип центра: "
                    + centerType + ", оп. знак: " + sighType + "</description>\n" +
                    "<Point>\n" +
                    "<coordinates>" + longitude + "," + latitude + "</coordinates>\n" +
                    "</Point>\n" +
                    "</Placemark>\n";
            kmlPoints.append(kmlPoint);
        }

        String kml = kmlHeader + kmlPoints.toString() + kmlFooter;

        File file = new File("output.kml");
        FileWriter writer = new FileWriter(file);
        writer.write(kml);
        writer.close();
        return file;
    }

}
