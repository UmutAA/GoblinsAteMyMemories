public abstract class TroopCard extends Card {
    private int health;
    private int speed;

    protected TroopCard(){}
    protected TroopCard(String cardName, int health, int speed){
        super(cardName);
        this.health = health;
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void takeDamage(int damage){
        this.setHealth(this.getHealth() - damage);
    }

    public abstract boolean equals(TroopCard target);
}
