package geo.geopointsTgBot;

import geopoints.Geopoints;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class KmlHandler {
    public static File createKML(Iterator<Geopoints.GgsResponse> ggsList,
                                 Iterator<Geopoints.GnsResponse> gnsList) throws IOException {
        String kmlHeader = """
                <?xml version="1.0" encoding="UTF-8"?>
                <kml xmlns="http://www.opengis.net/kml/2.2">
                """;
        String kmlFooter = "</kml>";
        StringBuilder kmlPoints = new StringBuilder();
        String kmlPoint;
        if (ggsList != null)
            while (ggsList.hasNext()) {
                Geopoints.GgsResponse response = ggsList.next();
                double longitude = response.getLongitude();
                double latitude = response.getLatitude();
                String name = ((response.getName() != null) ? response.getName() : "Нет данных");
                String index = ((response.getIndex() != null) ? response.getIndex() : "Нет данных");
                String mark = ((response.getMark() != null) ? response.getMark() : "Нет данных");
                String centerType = ((response.getCenterType() != null) ? response.getCenterType() : "Нет данных");
                String sighType = ((response.getSighType() != null) ? response.getSighType() : "Нет данных");
                kmlPoint = "<Placemark>\n" +
                        "<name>" + name + "</name>\n" +
                        "<description>Индекс: " + index + ", марка: " + mark + ", тип центра: "
                        + centerType + ", оп. знак: " + sighType + "</description>\n" +
                        "<Point>\n" +
                        "<coordinates>" + latitude + "," + longitude + "</coordinates>\n" +
                        "</Point>\n" +
                        "</Placemark>\n";
                kmlPoints.append(kmlPoint);
            }
        if (gnsList != null)
            while (gnsList.hasNext()) {
                Geopoints.GnsResponse response = gnsList.next();
                double longitude = response.getLongitude();
                double latitude = response.getLatitude();
                String name = ((response.getName() != null) ? response.getName() : "Нет данных");
                String index = ((response.getIndex() != null) ? response.getIndex() : "Нет данных");
                String mark = ((response.getMark() != null) ? response.getMark() : "Нет данных");
                String centerType = ((response.getCenterType() != null) ? response.getCenterType() : "Нет данных");
                String sighType = ((response.getSighType() != null) ? response.getSighType() : "Нет данных");
                String maingeographyfeature = (response.getMainGeographyFeature() != null)
                        ? response.getMainGeographyFeature() : "Нет данных";
                kmlPoint = "<Placemark>\n" +
                        "<name>" + name + "</name>\n" +
                        "<description>Индекс: " + index + ", марка: " + mark + ", тип центра: "
                        + centerType + ", оп. знак: " + sighType + ", кроки: " + maingeographyfeature + "</description>\n" +
                        "<Point>\n" +
                        "<coordinates>" + latitude + "," + longitude + "</coordinates>\n" +
                        "</Point>\n" +
                        "</Placemark>\n";
                kmlPoints.append(kmlPoint);
            }
        String kml = kmlHeader + kmlPoints + kmlFooter;

        File file = new File("output.kml");
        FileWriter writer = new FileWriter(file);
        writer.write(kml);
        writer.close();

        return file;
    }
}
