public class Subscribtion {

    public Long chatId;
    public String location;

    public Subscribtion(Long chatId, String location) {

        this.chatId = chatId;
        this.location = location;
//        this.idCity = idCity;

    }

    @Override
    public String toString() {
        return String.format("chatId: %s| location: %s");

    }
}
