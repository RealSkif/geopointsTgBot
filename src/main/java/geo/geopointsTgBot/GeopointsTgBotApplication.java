package geo.geopointsTgBot;

import geopoints.Geopoints;
import geopoints.GeopointsServiceGrpc;
import io.grpc.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class GeopointsTgBotApplication {
    public static void main(String[] args) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot(new Json());
        botsApi.registerBot(bot);
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8082")
                .usePlaintext().build();
        GeopointsServiceGrpc.GeopointsServiceBlockingStub stub = GeopointsServiceGrpc.newBlockingStub(channel);
        Geopoints.GgsRequest request = Geopoints.GgsRequest.newBuilder().setLatitude(44.32324F)
                .setLongitude(56.346F).setRadius(10F).build();
        Iterator<Geopoints.GgsResponse> response = stub.ggs(request);
        while (response.hasNext()) {
            System.out.println();
            channel.shutdown();
        }
    }
    }