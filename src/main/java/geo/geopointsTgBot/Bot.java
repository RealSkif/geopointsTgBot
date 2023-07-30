package geo.geopointsTgBot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private Json json;
    Menu menu;
    UserInputHandler userInputHandler;

    private final String wrongInput = "Неправильно введен запрос. Убедитесь, что он соотвествует следующему формату:\n" +
            "\"широта, долгота (например '55.168949, 61.212220')";
    private final String outOfRange = "Введенные координаты выходят за границы Российской Федерации." +
            " Для РФ диапазон широт от 41 до 82 градусов, долгот от 19 до 180";

    public Bot(Json json) {
        this.json = json;
        this.menu = new Menu();
        this.userInputHandler = new UserInputHandler();
    }

    @Override
    public String getBotUsername() {
        return "GGS_Points_bot";
    }

    @Override
    public String getBotToken() {
        return "???";
    }

    public void onUpdateReceived(Update update) {
        long chatId;
        String msg;
        try {
            if (update.hasCallbackQuery()) {
                chatId = update.getCallbackQuery().getMessage().getChatId();
                menu.handleCallback(update);
                execute(menu.inlineMenu(chatId, menu.isGgs(),
                        menu.isGns(), menu.getRadius()));
            } else {
                msg = update.getMessage().getText();
                chatId = update.getMessage().getChatId();

                execute(menu.replyMenu(chatId));
                if (msg.equals("Настройки"))
                    execute(menu.inlineMenu(chatId, menu.isGgs(), menu.isGns(), menu.getRadius()));
                if (!userInputHandler.validateInput(msg) && (!msg.equals("Настройки") && !msg.equals("/start"))) {
                    sendText(chatId, wrongInput);
                    if (!userInputHandler.validateCoords(msg))
                        sendText(chatId, outOfRange);

                } else {
                    userInputHandler.handleUserInput(msg, chatId);
                }
            }
        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}