public class Subscribtion {

    public Long chatId;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String location;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

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
