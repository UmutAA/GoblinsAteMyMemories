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
            target.setGem(target.getGem() + gen.nextInt(21) + 10);
            if(gen.nextBoolean()){
                target.setPower(target.getPower() + gen.nextInt(2) + 1);
            }
            else{
                target.setHealth(target.getHealth() + 1);
            }
            System.out.println("You earned some gem and leveled up!");
            System.out.println("Your new stats: " + target.toString());
        }
    }

    @Override
    public int attack(){
        if(this.isAggressive()){
            boolean played = false;
            int damage = 0;
            while (!played){
                try{
                    setCurrentCard(getCardList().getFirst());
                    damage = this.getCardList().getFirst().playCard();
                    System.out.println(this.getName() + " played " + this.getCardList().getFirst().getCardName());
                    played = true;
                }
                catch (UnavailableCardException e){
                    return 0;
                }
            }
            return (damage * super.getPower());
        }
        else{
            System.out.println(this.getName() + " is not hostile, you pass this round safely.");
            return 0;
        }
    }

    @Override
    public String toString(){
        return String.format("[Figurant: %s, Health: %d, Power: %d, Aggressiveness: %b]", super.getName()
                ,super.getHealth(), super.getPower(), this.isAggressive());
    }
}
