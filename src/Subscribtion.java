import org.telegram.telegrambots.meta.api.objects.Message;

public class Subscribtion {

    public String  id;
    public String message;

    public Subscribtion(String  id, String message) {
       this.id = id;
//       message = update.getMessage();
        this.message = message;
    }

    @Override
    public String toString(){
            return String.format("ID: %s| Message: %s" );

    }
}
