import java.util.ArrayList;

public abstract class Character {
    private String name;
    private int health;
    private int power;
    private ArrayList<Card> cardList = new ArrayList<Card>();
    private Card currentCard;

    protected Character() {
        cardList = new ArrayList<Card>();
    }

    protected Character(String name, int health, int power, ArrayList<Card> cardList) {
        this.name = name;
        this.health = health;
        this.power = power;
        this.cardList = cardList;
        for  (Card card : cardList) {
            if (card instanceof TroopCard) {
                currentCard = card;
            }
        }
    }

    public int getHealth() {
        return health;
    }

    public int getPower() {
        return power;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getCardList() {
        return cardList;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public  void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public void setHealth(int health) {
        this.health = health;
        if (health < 0) {
            this.health = 0;
        }
    }

    public void setPower(int power) {
        this.power = power;
    }

    public abstract int attack();

    public abstract String toString();

    public boolean checkDeck(){
        for (Card card : this.getCardList()){
            if (card.isAvailable())
            {
                return true;
            }
        }
        return false;
    }

    public void takeDamage(int damage) {
        if (!checkDeck()) {
            this.setHealth(this.getHealth() - 1);
            System.out.println(this.getName() + " has no available card left. And took a damage");
        }
        else{
            if(getCurrentCard() instanceof TroopCard tc){
                tc.takeDamage(damage);
                System.out.println(tc.getCardName() + " took " + damage + " damage.");
                if (tc.getHealth() <= 0) {
                    tc.setHealth(0);
                    System.out.println(tc.getCardName() + " is down.");
                    if (!checkDeck()) {
                        this.setHealth(this.getHealth() - 1);
                        System.out.println(this.getName() + " has no available card left. And took a damage");
                    }
                }
            }
        }
    }

    public void resetDeck(){
        for (Card card : getCardList()) {
            if (card instanceof TroopCard tc) {
                tc.setAvailable(true);
                tc.setHealth(tc.getMaxHealth());
            }
        }
    }
}
