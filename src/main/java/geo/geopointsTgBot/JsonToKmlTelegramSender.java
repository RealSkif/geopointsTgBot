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

            String kmlPoint = "<Placemark>\n" +
                    "<name>" + json.getString("name") + "</name>\n" +
                    "<description>Индекс: " + json.getString("index") + ", марка: " + json.getString("mark") + ", тип центра: " + json.getString("centerType") + ", оп. знак: " + json.getString("sighType") + "</description>\n" +
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
