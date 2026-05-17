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

    @Override
    public void buy(Player target) throws InsufficientMoneyException, DuplicateException {
        if (target.getGem() < this.getPrice()) {
            throw new InsufficientMoneyException("To purchase this emote, you need to have "
                    +  (this.getPrice() - target.getGem()) + " more gems!");
        } else if (target.getEmoteList().contains(this)) {
            throw new DuplicateException("You already own this emote!");
        } else  {
            System.out.println("Buying Emote...");
        }
    }
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Emote emote)
        {
            if (this == obj)
            {
                return true;
            }

            else
            {
                return getEmoteMessage().equals(emote.getEmoteMessage());
            }
        }

        else
        {
            return false;
        }
    }
}
