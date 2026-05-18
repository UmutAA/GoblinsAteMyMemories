import java.util.*;

public class Figurant extends Enemy{
    private boolean isAggressive;

    public Figurant(){}
    public Figurant(String name, int health, int power, ArrayList<Card> cardList, ArrayList<String> quoteList,
                    boolean isAggressive){
        super(name, health, power, cardList, quoteList);
        this.isAggressive = isAggressive;
    }

    public boolean isAggressive() {
        return isAggressive;
    }

    @Override
    public void getReward(Player target){
        if(super.getHealth() <= 0){
            ArrayList<Card> temp = super.getCardList();
            Card tempCard = temp.getFirst();
            Card tempCloned = null;
            if(tempCard instanceof RangedCard){
                tempCloned = ((RangedCard) tempCard).clone();
            }
            else if(tempCard instanceof MeleeCard){
                tempCloned = ((MeleeCard) tempCard).clone();
            }
            target.addCard(tempCloned);
        }
    }

    @Override
    public int attack(){
        int damage = 0;
        if(this.isAggressive()){
            try{
                damage = super.getCardList().getFirst().playCard();
            }
            catch (UnavailableCardException _){

            }
            return damage * super.getPower();
        }
        else{
            return damage;
        }
    }

    @Override
    public String toString(){
        return String.format("[Figurant: %s, Health: %d, Power: %d, Aggressiveness: %b]", super.getName()
                ,super.getHealth(), super.getPower(), this.isAggressive());
    }
}
