import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {


    public static void main(String[] args) {

        System.getProperties().put("proxySet", "true");
        System.getProperties().put("socksProxyHost", "74.115.25.72");
        System.getProperties().put("socksProxyPort", "1080");

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();


        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onUpdateReceived(Update update) {
        Model model = new Model();

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {

//                case "unsubscribe":
//                    sendMsg(message, "От какого города отписаться?");
//                    break;
//
//                    /*            // Удаление записи с id = 8
//            dbHandler.deleteProduct(8);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}

                case "subscribe":
                    sendMsg(message, "Введите город!");


                    //
                    try {
                        message.getText();
                        DbHandler dbHandler = DbHandler.getInstance();
                        dbHandler.addSubscribtion(new Subscribtion(message.getChatId().toString(), update.getMessage().toString()));
                            List<Subscribtion> subscribtions = dbHandler.getAllSubscribtions();
                            for(Subscribtion subscribtion : subscribtions) {
                               try {
                                   sendMsg(message, Weather.getWeather(message.getText(), model));

                               }
                               catch (Exception e){
                                   e.printStackTrace();
                               }
                            }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    break;


                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "Город не найден");
                    }
            }
        }
    }


    //Клавиатура
    private void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        //Связать клавиатуру и сообщение
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        //Подгонка клавиатуры по размеру
        replyKeyboardMarkup.setResizeKeyboard(true);
        //Скрывать клавиатуру после ввода
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
//Первый ряд кнопок
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("subscribe"));
        keyboardFirstRow.add(new KeyboardButton("unsubscribe"));
// Добавить кнопки в список клавиатуры
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);


    }


    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            //Добавить клавиатуру к отправке сообщений
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return "MyWforWBot";
    }

    @Override
    public String getBotToken() {
        return "752437064:AAF3_F_Rwd57YrFc6JY8kpg1dPCXnsdMS3w";
    }
}
