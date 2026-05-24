import java.lang.Cloneable;

/**
 * Main card mechanism class for combat system.
 */
public abstract class TroopCard extends Card{
    private int health;
    private int maxHealth;
    private int speed;

    protected TroopCard(){}
    protected TroopCard(String cardName, int health, int speed){
        super(cardName);
        this.health = health;
        this.maxHealth = health;
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setHealth(int health) {
        if (health <= 0){
            this.health = 0;
            this.setAvailable(false);
        }
        else this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Calculates and applies the damage to be taken.
     * @param damage Raw damage input to be taken
     */
    public void takeDamage(int damage){
        int totalDamage = 0;
        if (10 * damage / this.getSpeed() > 0){
            totalDamage = 10 * damage / this.getSpeed();
        }
        this.setHealth(this.getHealth() - totalDamage);
        System.out.println(this.getCardName() + " took " + totalDamage + " damage: ");
    }

    public abstract boolean equals(Object obj);
}
