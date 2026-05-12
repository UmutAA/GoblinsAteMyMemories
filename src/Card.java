public abstract class Card {
    private String cardName;
    private int price;

    protected Card(){

    }
    protected Card(String cardName, int price) {
        this.cardName = cardName;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getCardName() {
        return cardName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public abstract String toString();

    public abstract int playCard() throws InsufficientCardException;
}
