package geo.geopointsTgBot;

import geopoints.Geopoints;
import geopoints.GeopointsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserInputHandler {
    public File handleUserInput(String msg, Menu menu) throws IOException {
        String[] temp = msg.split("[,\\s]+");
        double latitude = Double.parseDouble((temp[0].trim()));
        double longitude = Double.parseDouble((temp[1].trim()));
        double radius = Double.parseDouble((menu.getRadius()));
        /*
         * Создание канала и стаба для gRPC
         * */
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8082")
                .usePlaintext().build();
        GeopointsServiceGrpc.GeopointsServiceBlockingStub stub = GeopointsServiceGrpc.newBlockingStub(channel);
        /*
         * Создание запросов
         * */
        Iterator<Geopoints.GgsResponse> ggsList = null;
        if (menu.isGgs()) {
            Geopoints.GgsRequest ggsRequest = Geopoints.GgsRequest.newBuilder().setLatitude(latitude)
                    .setLongitude(longitude).setRadius(radius).build();
            ggsList = stub.ggs(ggsRequest);
        }

        Iterator<Geopoints.GnsResponse> gnsList = null;
        if (menu.isGns()) {
            Geopoints.GnsRequest gnsRequest = Geopoints.GnsRequest.newBuilder().setLatitude(latitude)
                    .setLongitude(longitude).setRadius(radius).build();
            gnsList = stub.gns(gnsRequest);
        }

        /*
         * Создание kml на основе респонса от gRPC сервера, закрытие канала
         * */

        File file = KmlHandler.createKML(ggsList, gnsList);
        channel.shutdown();
        return file;
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
        /*
         * Крайние значение для широт и долгот для территории РФ
         * */
        double minLongitude = 19;
        double maxLongitude = 180;
        double minLatitude = 41;
        double maxLatitude = 82;
        String[] temp = input.split("[,\\s]+");
        return !(Double.parseDouble(temp[0]) < minLatitude) && !(Double.parseDouble(temp[0]) > maxLatitude)
                && !(Double.parseDouble(temp[1]) < minLongitude) && !(Double.parseDouble(temp[0]) > maxLongitude);
    }

}
