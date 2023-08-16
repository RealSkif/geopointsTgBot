package geo.geopointsTgBot;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Menu {
    private boolean isGgs = true;
    private boolean isGns = true;
    private String radius = "10";

    public Menu() {
    }

    ReplyKeyboardMarkup menu = new ReplyKeyboardMarkup();

    public SendMessage replyMenu(long messageId) {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton mainMenu = new KeyboardButton("Настройки");
        row.add(mainMenu);
        rows.add(row);
        menu.setKeyboard(rows);
        menu.setResizeKeyboard(true);
        SendMessage messageConfig = new SendMessage();
        messageConfig.setChatId(messageId);
        messageConfig.setText("Для изменения параметров поиска нажмите на кнопку настроек внизу чата");
        messageConfig.setReplyMarkup(menu);
        return messageConfig;
    }

    public SendMessage inlineMenu(long messageId, boolean isGgs, boolean isGns, String radius) {
        InlineKeyboardButton option1Button = new InlineKeyboardButton("✅ пункты ГГС");
        InlineKeyboardButton option2Button = new InlineKeyboardButton("✅ пункты ГНС");
        InlineKeyboardButton option0Button = new InlineKeyboardButton("Укажите радиус поиска");
        InlineKeyboardButton option3Button = new InlineKeyboardButton("5 км");
        InlineKeyboardButton option4Button = new InlineKeyboardButton("✅ 10 км");
        InlineKeyboardButton option5Button = new InlineKeyboardButton("15 км");
        InlineKeyboardButton option6Button = new InlineKeyboardButton("20 км");

        option3Button.setText("5 км");
        option4Button.setText("10 км");
        option5Button.setText("15 км");
        option6Button.setText("20 км");

        if (isGgs) option1Button.setText("✅ пункты ГГС");
        else option1Button.setText("❌ пункты ГГС");
        option1Button.setCallbackData("option1");

        if (isGns) option2Button.setText("✅ пункты ГНС");
        else option2Button.setText("❌ пункты ГНС");
        option2Button.setCallbackData("option2");

        option0Button.setText("Укажите радиус поиска");
        option0Button.setCallbackData("option0");

        switch (radius) {
            case ("5") -> option3Button.setText("✅ 5 км");
            case ("10") -> option4Button.setText("✅ 10 км");
            case ("15") -> option5Button.setText("✅ 15 км");
            case ("20") -> option6Button.setText("✅ 20 км");

        }
        option3Button.setCallbackData("option3");
        option4Button.setCallbackData("option4");
        option5Button.setCallbackData("option5");
        option6Button.setCallbackData("option6");
        List<List<InlineKeyboardButton>> keyboardRowList = new ArrayList<>();
        List<InlineKeyboardButton> subMenuRow1 = new ArrayList<>();
        List<InlineKeyboardButton> subMenuRow2 = new ArrayList<>();
        List<InlineKeyboardButton> subMenuRow3 = new ArrayList<>();
        List<InlineKeyboardButton> subMenuRow4 = new ArrayList<>();
        subMenuRow1.add(option1Button);
        subMenuRow2.add(option2Button);
        subMenuRow3.add(option3Button);
        subMenuRow3.add(option4Button);
        subMenuRow3.add(option5Button);
        subMenuRow3.add(option6Button);
        subMenuRow4.add(option0Button);
        keyboardRowList.add(subMenuRow1);
        keyboardRowList.add(subMenuRow2);
        keyboardRowList.add(subMenuRow4);
        keyboardRowList.add(subMenuRow3);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(keyboardRowList);
        SendMessage messageConfig = new SendMessage();
        messageConfig.setChatId(messageId);
        messageConfig.setText("Измените параметры поиска:");
        messageConfig.setReplyMarkup(keyboardMarkup);
        return messageConfig;
    }

    public void handleCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        switch (callbackData) {
            case ("option1") -> isGgs = !isGgs;
            case ("option2") -> isGns = !isGns;
            case ("option3") -> radius = "5";
            case ("option4") -> radius = "10";
            case ("option5") -> radius = "15";
            case ("option6") -> radius = "20";
        }
    }
}