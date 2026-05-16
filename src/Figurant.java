import java.util.ArrayList;

public class Figurant extends Enemy implements Cloneable<Figurant>{
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
        //TODO
    }

    @Override
    public int attack(){
        //TODO
        return 0;
    }

    @Override
    public String toString(){
        return String.format("[Figurant: %s, Health: %d, Power: %d, Aggressiveness: %b]", super.getName()
                ,super.getHealth(), super.getPower(), this.isAggressive());
    }

    @Override
    public Figurant clone(){
        ArrayList<Card> cards = new ArrayList<Card>(super.getCardList());
        ArrayList<String> quotes = new ArrayList<String>(super.getQuoteList());
        return new Figurant(getName(), super.getHealth(), super.getPower(),
                cards, quotes, this.isAggressive());
    }



}
