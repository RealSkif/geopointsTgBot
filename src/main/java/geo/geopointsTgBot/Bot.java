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
        return "";
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
                userInputHandler.handleUserMessage(msg, chatId, menu, json);
            }
        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}