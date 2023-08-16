package geo.geopointsTgBot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;

@Getter
@Setter
public class Bot extends TelegramLongPollingBot {
    Menu menu;
    UserInputHandler userInputHandler;
    @Value("${token}")
    private String token;
    public Bot() {
        this.menu = new Menu();
        this.userInputHandler = new UserInputHandler();
    }

    @Override
    public String getBotUsername() {
        return "GGS_Points_bot";
    }

    @Override
    public String getBotToken() {
        return token;
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

                handleUserMessage(msg, chatId, menu);
            }
        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleUserMessage(String msg, long chatId, Menu menu) throws TelegramApiException, IOException {
        String outOfRange = "Введенные координаты выходят за границы Российской Федерации." + " Для РФ диапазон широт от 41 до 82 градусов, долгот от 19 до 180";
        String wrongInput = "Неправильно введен запрос. Убедитесь, что он соотвествует следующему формату:\n" + "\"широта, долгота (например '55.168949, 61.212220')";
        execute(menu.replyMenu(chatId));
        if (msg.equals("Настройки"))
            execute(menu.inlineMenu(chatId, menu.isGgs(), menu.isGns(), menu.getRadius()));
        if (!userInputHandler.validateInput(msg) && (!msg.equals("Настройки") && !msg.equals("/start"))) {
            sendText(chatId, wrongInput);
            if (!userInputHandler.validateCoords(msg)) sendText(chatId, outOfRange);
        } else {
            sendFile(chatId, userInputHandler.handleUserInput(msg, menu));
        }
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder().chatId(who.toString()).text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFile(Long chatId, File file) {
        SendDocument sendDocumentRequest = new SendDocument();
        sendDocumentRequest.setChatId(chatId.toString());
        sendDocumentRequest.setDocument(new InputFile(file));
        try {
            execute(sendDocumentRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}