package geo.geopointsTgBot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonToKmlTelegramSender {

    public static File sendJsonListAsKmlFileInTelegram(JSONArray jsonList) throws IOException {
        String kmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n";
        String kmlFooter = "</kml>";

        StringBuilder kmlPoints = new StringBuilder();

        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject json = jsonList.getJSONObject(i);

            double longitude = json.getJSONArray("coordinates").getDouble(0);
            double latitude = json.getJSONArray("coordinates").getDouble(1);
            String name = (json.getString("name") != null) ? json.getString("name") : "Нет данных";
            String index = (json.getString("index") != null) ? json.getString("index") : "Нет данных";
            String mark = (json.getString("mark") != null) ? json.getString("mark") : "Нет данных";
            String centerType = (json.getString("centerType") != null) ? json.getString("centerType") : "Нет данных";
            String sighType = (json.getString("sighType") != null) ? json.getString("sighType") : "Нет данных";

            String kmlPoint = "<Placemark>\n" +
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
