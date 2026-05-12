import java.util.ArrayList;
import java.util.Scanner;

public class Player extends Character implements Talkative{
    private int gem;
    private ArrayList<Emote> emoteList = new ArrayList<Emote>();

    public Player() {
        emoteList = new ArrayList<Emote>();
    }

    public Player(String name, int health, int power, ArrayList<Card> cardList) {
        super(name, health, power, cardList);
        this.gem = 0;
        this.emoteList = new ArrayList<Emote>();
    }

    public int getGem() {
        return gem;
    }

    public void setGem(int gem) {
        if (gem > 0) {
            this.gem = gem;
        }
        else{
            System.out.println("Invalid Gem");
        }
    }

    public int attack(){
        int damage = 0;
        return damage;
    }

    @Override
    public String toString(){
        return String.format("[Player: %s, Gem: %d, Health: %d, Power: %d]",getName(),getGem(),getHealth(),getPower());
    }

    public void talk(){
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < emoteList.size(); i++) {
            System.out.printf("%d. %s", i++, emoteList.get(i).getEmoteMessage());
        }
        System.out.print("Please enter emote index: ");
        int choice = input.nextInt() - 1;
        Emote emote = emoteList.get(choice);
        System.out.println(emote.getEmoteMessage());
    }

    public boolean addCard(Card card){
        if (getCardList().contains(card)){
            System.out.println("Card already exists");
            return false;
        }
        else{
            getCardList().add(card);
            return true;
        }
    }

    public boolean addEmote(Emote emote){
        if (emoteList.contains(emote)){
            System.out.println("Emote already exists");
            return false;
        }
        else{
            emoteList.add(emote);
            return true;
        }
    }
}
