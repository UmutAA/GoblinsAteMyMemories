public abstract class Card{
    private String cardName;
    private boolean isAvailable;

    protected Card(){

    }
    protected Card(String cardName) {
        this.cardName = cardName;
        this.isAvailable = true;
    }

    public String getCardName() {
        return cardName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public abstract String toString();

    public abstract int playCard() throws UnavailableCardException;
}
