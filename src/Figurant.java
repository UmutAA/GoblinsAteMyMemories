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
            Random gen = new Random();
            target.setGem(target.getGem() + gen.nextInt(11) + 5);
        }
    }

    @Override
    public int attack(){
        if(this.isAggressive()){
            boolean played = false;
            int damage = 0;
            while (!played){
                try{
                    damage = this.getCardList().getFirst().playCard();
                    played = true;
                }
                catch (UnavailableCardException e){
                    continue;
                }
            }
            return (damage * super.getPower());
        }
        else{
            System.out.println("He seems peaceful...");
            return 0;
        }
    }

    @Override
    public String toString(){
        return String.format("[Figurant: %s, Health: %d, Power: %d, Aggressiveness: %b]", super.getName()
                ,super.getHealth(), super.getPower(), this.isAggressive());
    }
}
