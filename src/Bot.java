import javafx.concurrent.Task;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
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
import org.telegram.telegrambots.meta.logging.BotLogger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;


import static com.google.common.collect.ComparisonChain.start;
import static sun.misc.PostVMInitHook.run;

public class Bot extends TelegramLongPollingBot {
    Map<Long, Boolean> sub = new HashMap<>();

    private static void runSub() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();


                Model model = new Model();


                {
                    DbHandler handler = null;
                    try {
                        handler = DbHandler.getInstance();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    HashMap<Long, String> subscribtions = handler.getAllSubscribtions();
                    for (Map.Entry<Long, String> pair : subscribtions.entrySet()) {
                        Long chatId = pair.getKey();
                        String location = pair.getValue();

                        System.out.println(pair.getKey());
                        System.out.println(pair.getValue());

                        try {

                            sendMsg(chatId, Weather.getWeather(location, model));
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        };thread.start();
}





    public static void main(String[] args) {

        System.getProperties().put("proxySet", "true");
        System.getProperties().put("socksProxyHost", "5.56.133.122");
        System.getProperties().put("socksProxyPort", "1080");

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();


        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();


        }
        runSub();
//        Timer timer = new Timer();
//        timer.schedule(new Task(), 10000);
    }


//    TimerTask task = new TimerTask() {
//        public void run() {


    @Override
    public void onUpdateReceived(Update update) {
        Model model = new Model();


        Message message = update.getMessage();
        System.out.println(message.getMessageId());

        if (message != null && message.hasLocation()) {
            for (Long key : sub.keySet()) {
//                System.out.println(key);
            }

            if (sub.containsKey(message.getChatId())
            ) {
                try {
                    DbHandler dbHandler = DbHandler.getInstance();
                    dbHandler.addSubscribtion(new Subscribtion(message.getChatId(), message.getLocation().toString()));
                    sub.remove(message.getChatId());
//                    System.out.println("done");
                    sendMsg(message, "Вы подписаны на ежедневный прогноз погоды");


                } catch (SQLException e) {
                    e.printStackTrace();

                }
            } else try {
                sendMsg(message, Weather.getWeather(message.getLocation().toString(), model));
//                System.out.println(message.getLocation().toString());


            } catch (IOException e) {
                sendMsg(message, "Город не найден");
            }
        }


        if (message != null && message.hasText()) {
            if (message.getText().contains("/subscribe")) {

                sub.put(message.getChatId(), null);
                for (Long key : sub.keySet()) {
                    System.out.println(key);
                }

                sendMsg(message, "Отправьте геолокацию");

            }
        }
    }


/*



/*
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


//
//                    // НЕ ПОЛУЧАЕТ id ГОРОДА!!!
//                    model.getId();

                    //   System.out.println(message.getText().substring(4));
                    System.out.println(message.getChatId().toString());

                    DbHandler dbHandler = DbHandler.getInstance();
                    dbHandler.addSubscribtion(new Subscribtion(message.getChatId().toString(), message.getLocation().toString()));

                    sendMsg(message, "Вы подписаны");


//                    List<Subscribtion> subscribtions = dbHandler.getAllSubscribtions();
//                    for (Subscribtion subscribtion : subscribtions) {
//                            try {
//                                sendMsg(message, Weather.getWeather(message.getText(), model));
//
//                            }
//                            catch (Exception e){
//                                e.printStackTrace();
//                            }
//                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    sendMsg(message, "ОШИБКА!!");
                } catch (IOException e) {
                    e.printStackTrace();
                    sendMsg(message, "ОШИБКА!!");
                }


            }
            else if((message.getText().startsWith("remove"))) {


                try {

                    Weather.getWeather((Location) message.getLocation(), model);



                    System.out.println(message.getText().substring(6));
//                    System.out.println(model.toString());
                    System.out.println(message.getChatId().toString());




                    // НЕ ПОЛУЧАЕТ id ГОРОДА!!!
                    //
//
                    DbHandler dbHandler = DbHandler.getInstance();
                    dbHandler.deleteSubscribtion(message.getChatId().toString(), (Location) message.getLocation());

                } catch (SQLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    e.printStackTrace();
                    sendMsg(message, "ОШИБКА!!");
                }

            }
            else
       */


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

        keyboardFirstRow.add(new KeyboardButton("/subscribe"));
        keyboardFirstRow.add(new KeyboardButton("unsubscribe"));

// Добавить кнопки в список клавиатуры
        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);


    }

    private  void sendMsg(Long chatID, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);
//        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            //Добавить клавиатуру к отправке сообщений
            setButtons(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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