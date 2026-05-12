public class Emote implements Buyable{
    private String emoteMessage;
    private int price;

    public Emote() {

    }

    public Emote(String emoteMessage, int price) {
        this.emoteMessage = emoteMessage;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
    public String getEmoteMessage() {
        return emoteMessage;
    }

    @Override
    public String toString() {
        return String.format("[Emote: %s, Price: %d]", getEmoteMessage(), getPrice());
    }

    public void buy(Player target) throws InsufficientMoneyException {

    }
}
