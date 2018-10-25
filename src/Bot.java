import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingBot {


    public static void main(String[] args) {


        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

    //    System.getProperties().put( "proxySet", "true" );
      //  System.getProperties().put("socksProxyHost", " 206.189.101.248");
        //System.getProperties().put("socksProxyPort", "1080");





        try {
            telegramBotsApi.registerBot(new Bot());
        }
        catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        if (message != null && message.hasText()){
            if(message.getText().equals("/help")){
                sendMsg(message, "Что я могу для Вас сделать?");
            }
            else
                sendMsg(message, "Я Вас не понимаю");


        }


    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
       try {
           execute(sendMessage);
        }
        catch (TelegramApiException e){
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
