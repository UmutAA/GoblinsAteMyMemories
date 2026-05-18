import java.util.*;

public class Thief extends Enemy{
    private int gemsStolen;
    private ArrayList<Card> StolenCards;
    private final Random gen = new Random();
    public Thief(){

    }
    public Thief(String name, int health, int power, ArrayList<Card> cardList, ArrayList<String> quoteList){
        super(name, health, power, cardList, quoteList);
        this.gemsStolen = 0;
        this.StolenCards = null;
    }

    public int getGemsStolen() {
        return gemsStolen;
    }

    public void setGemsStolen(int gemsStolen) {
        this.gemsStolen = gemsStolen;
    }

    public ArrayList<Card> getStolenCards() {
        return StolenCards;
    }

    public void steal(Player target){
        Card stolen = target.getCardList().get(gen.nextInt(getCardList().size()));
        target.getCardList().remove(stolen);
        Card tempCloned = null;
        if(stolen instanceof RangedCard){
            tempCloned = ((RangedCard) stolen).clone();
            this.StolenCards.add(tempCloned);
        }
        else if(stolen instanceof TroopCard){
            tempCloned = ((TroopCard) stolen).clone();
            this.StolenCards.add(tempCloned);
        }
        else{
            SpellCard temp = ((SpellCard) stolen);
            target.setGem(target.getGem() - temp.getPrice());
            this.setGemsStolen(this.getGemsStolen() + temp.getPrice());
        }
    }

    @Override
    public void getReward(Player target){
        if(super.getHealth() <= 0){
            System.out.println("You reearned your stuff which were stolen by thief!");
            target.setGem(target.getGem() + this.getGemsStolen());
            target.getCardList().addAll(this.getStolenCards());
        }
    }

    @Override
    public int attack(){
        int damage = 0;
        if(this.getStolenCards().isEmpty()){
            System.out.println("Thief must stole card before it's attack!");
            return damage;
        }
        else {
            boolean played = false;
            while (!played){
                int choice = gen.nextInt(getStolenCards().size());
                try{
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
