import java.util.ArrayList;

public abstract class Enemy extends Character{
    protected ArrayList<String> quoteList;

    protected Enemy(){

    }

    protected Enemy(String name, int health, int power, ArrayList<Card> cardList, ArrayList<String> quoteList){
        super(name,health,power,cardList);
        this.quoteList = quoteList;
    }

    public ArrayList<String> getQuoteList(){
        return quoteList;
    }

    public abstract void getReward(Player target);
}
