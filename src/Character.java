import java.util.ArrayList;

public abstract class Character {
    private String name;
    private int health;
    private int power;
    private ArrayList<Card> cardList = new ArrayList<Card>();

    protected Character() {
        cardList = new ArrayList<Card>();
    }

    protected Character(String name, int health, int power, ArrayList<Card> cardList) {
        this.name = name;
        this.health = health;
        this.power = power;
        this.cardList = cardList;
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

    public void setHealth(int health) {
        if (health > 0 && health <= 100) {
            this.health = health;
        }
    }

    public void setPower(int power) {
        this.power = power;
    }

    public abstract int attack();

    public abstract String toString();

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
        }
    }
}
