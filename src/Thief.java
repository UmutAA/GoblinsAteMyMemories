import java.util.*;

public class Thief extends Enemy{
    private int gemsStolen;
    private ArrayList<Card> stolenCards;

    public Thief(){
        this.gemsStolen = 0;
        this.stolenCards = new ArrayList<Card>();
    }
    public Thief(String name, int health, int power, ArrayList<Card> cardList, ArrayList<String> quoteList){
        super(name, health, power, cardList, quoteList);
        this.gemsStolen = 0;
        this.stolenCards = new ArrayList<Card>();
    }

    public int getGemsStolen() {
        return gemsStolen;
    }

    public void setGemsStolen(int gemsStolen) {
        this.gemsStolen = gemsStolen;
    }

    public ArrayList<Card> getStolenCards() {
        return stolenCards;
    }

    /**
     * Removes a random card from target'S deck and adds to Thief's deck.
     * @param target Player from whom a card will be stolen
     */
    public void steal(Player target){
        Random gen = new Random();
        while (true){
            Card stolen = target.getCardList().get(gen.nextInt(target.getCardList().size()));
            if(stolen instanceof TroopCard tc){
                this.stolenCards.add(tc);
                super.getCardList().add(tc);
                target.getCardList().remove(stolen);
                System.out.println(getName() + " stole one of your cards: " + stolen.toString());
                break;
            }
            else{
                continue;
            }
        }

        int random = gen.nextInt(11);
        if (random < 5){ // 50% chance
            this.setGemsStolen((target.getGem() / 25) + this.getGemsStolen());
        }
        else if(random < 9){ // 30% chance
            this.setGemsStolen((target.getGem() / 50) + this.getGemsStolen());
        }
        else{ // 20% chance
            this.setGemsStolen((target.getGem() / 75) + this.getGemsStolen());
        }
        target.setGem(target.getGem() - this.getGemsStolen());
        System.out.println(getName() + " stole " + getGemsStolen() + " gems from you");

    }

    /**
     * Gives the target stolen card back and stolen gems back.
     * @param target Player who shall receive the reward.
     */
    @Override
    public void getReward(Player target){
        if(super.getHealth() <= 0){
            System.out.println("You reearned your stuff which were stolen by " + getName());

            if (getGemsStolen() > 0){
                target.setGem(target.getGem() + this.getGemsStolen());
                target.getCardList().addAll(this.getStolenCards());
            }

            if(!getStolenCards().isEmpty()){
                target.getCardList().addAll(this.getStolenCards());
            }
        }
    }

    /**
     * Attack method to be used in the combat system. Plays the stolen card.
     * @return Total damage output.
     */
    @Override
    public int attack(){
        int damage = 0;
        if(this.getStolenCards().isEmpty()){
            System.out.println("Thief must steal a card before it attacks!");
            return damage;
        }

        else {
            boolean played = false;
            Random gen = new Random();
            while (!played){
                int choice = gen.nextInt(getStolenCards().size());
                try{
                    if (getStolenCards().get(choice) instanceof TroopCard){
                        setCurrentCard(getStolenCards().get(choice));
                    }
                    damage = this.getStolenCards().get(choice).playCard();
                    played = true;
                }
                catch (UnavailableCardException e){
                    continue;
                }
            }
            return (damage * super.getPower());
        }
    }

    @Override
    public String toString(){
        return String.format("[Thief: %s, Health: %d, Power: %d]", super.getName(),super.getHealth(), super.getPower());
    }
}
