package geo.geopointsTgBot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonToKmlTelegramSender {

    public static void sendJsonListAsKmlFileInTelegram(JSONArray jsonList, String botToken, String chatId) throws IOException {
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
                    "<description>Index: " + json.getString("index") + ", Mark: " + json.getString("mark") + "</description>\n" +
                    "<Point>\n" +
                    "<coordinates>" + longitude + "," + latitude + "</coordinates>\n" +
                    "</Point>\n" +
                    "</Placemark>\n";

            kmlPoints.append(kmlPoint);
        }

        String kml = kmlHeader + kmlPoints.toString() + kmlFooter;

        // Create a temporary file to hold the KML data
//        File tempFile = File.createTempFile("output", ".kml");
//        FileWriter writer = new FileWriter(tempFile);
//        writer.write(kml);
//        writer.close();
        File file = new File("output.kml");
        FileWriter writer = new FileWriter(file);
        writer.write(kml);
        writer.close();

        // Send the file as a Telegram document
        RestTemplate restTemplate = new RestTemplate();
        SendDocument request = new SendDocument(chatId, new InputFile(file));
        restTemplate.postForObject("https://api.telegram.org/bot" + botToken + "/sendDocument", request, String.class);

        // Delete the temporary file
        file.delete();
    }

}
