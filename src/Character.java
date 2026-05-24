import java.util.ArrayList;

/**
 * Main class for all the enemies and playable characters.
 */
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
            if (card instanceof TroopCard tc) {
                this.currentCard = tc;
                break;
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
    /**
     * Calculates a damage output using played card's and object's data fields
     * @return total damage output
     */
    public abstract int attack();

    public abstract String toString();

    /**
     * Checks the deck for available troop cards.
     * @return True: if found one. Else: Otherwise.
     */
    public boolean checkDeck(){
        for (Card card : this.getCardList()){
            if (card instanceof TroopCard tc)
            {
                if(tc.isAvailable()) return true;
            }
        }
        return false;
    }

    /**
     * Calculates and checks for exceptions and either played card or the character takes damage.
     * If no available card exist character takes 1 damage. Otherwise available card takes the calculated damage.
     * @param damage input damage to be taken
     */
    public void takeDamage(int damage) {
        boolean damageTaken = false;
        if (!checkDeck()) {
            this.setHealth(this.getHealth() - 1);
            System.out.println(this.getName() + " has no available card left. And took a damage");
            System.out.println(this.toString());
            damageTaken = true;
        }
        else{
            if(getCurrentCard() instanceof TroopCard tc){
                if (!tc.isAvailable()){
                    for (Card card : this.getCardList()) {
                        if (card instanceof  TroopCard tc2 && tc2.isAvailable()) {
                            this.setCurrentCard(tc2);
                            break;
                        }
                    }
                }
                tc.takeDamage(damage);
                System.out.println(tc.toString());
                if (tc.getHealth() <= 0) {
                    tc.setHealth(0);
                    System.out.println(tc.getCardName() + " is down.");
                    if (!checkDeck()) {
                        this.setHealth(this.getHealth() - 1);
                        System.out.println(this.getName() + " has no available card left. And took a damage");
                        System.out.println(this.toString());
                        damageTaken = true;
                    }
                    else{
                        for (Card card : this.getCardList()) {
                            if (card instanceof  TroopCard tc2 && tc2.isAvailable()) {
                                this.setCurrentCard(tc2);
                            }
                        }
                    }
                }
            }
        }

        if (damageTaken && this.getHealth() > 0) {
            this.resetDeck();
            System.out.println(this.getName() + " has renewed his deck. He's coming for another round.");
        }
    }

    /**
     * Sets all the troop cards as available
     */
    public void resetDeck(){
        for (Card card : getCardList()) {
            if (card instanceof TroopCard tc) {
                tc.setAvailable(true);
                tc.setHealth(tc.getMaxHealth());
            }
        }
    }
}
