import java.util.ArrayList;
import java.util.InputMismatchException;
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

    public ArrayList<Emote> getEmoteList() {return this.emoteList;}

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

    public int attack() {
        int damage = 0;
        System.out.println("Your Deck: ");
        for (int i = 0; i < this.getCardList().size(); i++) {
            System.out.printf("%d) %s\n", i + 1, getCardList().get(i).toString());
        }
        boolean played = false;
        while (!played){
            System.out.print("Choose your card: ");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            try {
                damage = getCardList().get(choice - 1).playCard();
                played = true;
            } catch (UnavailableCardException e) {
                System.out.println("Can't play this card: " + e.getMessage());
                System.out.println("Please play an available card!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Can't play this card: Invalid card number!");
                System.out.println("Please play an invalid card!");
            } catch (Exception e){
                System.out.println("Can't play this card: " +  e.getMessage());
            }
        }
        return (damage * this.getPower());
    }

    @Override
    public String toString(){
        return String.format("[Player: %s, Gem: %d, Health: %d, Power: %d]",getName(),getGem(),getHealth(),getPower());
    }

    public void talk(){
        Scanner input = new Scanner(System.in);
        if (emoteList.isEmpty()){
            System.out.println("There are no emotes to talk.");
            return;
        }
        for (int i = 0; i < emoteList.size(); i++) {
            System.out.printf("%d. %s", i, emoteList.get(i).getEmoteMessage());
        }
        System.out.print("Please enter emote index: ");
        boolean talked = false;
        while (!talked){
            try{
                int choice = input.nextInt();
                Emote emote = emoteList.get(choice);
                System.out.println(emote.getEmoteMessage());
                talked = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid emote index:" + e.getMessage());
                System.out.print("Please enter a valid emote index: ");
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("Invalid emote index:" + e.getMessage());
                System.out.println("Please enter a valid emote index: ");
            }
        }
    }

    public void addCard(Card card){
        if (card instanceof SpellCard c){
            try{
                c.buy(this);
                this.getCardList().add(c);
                this.setGem(this.getGem() - c.getPrice());
                System.out.println("Card bought successfully");
            } catch (InsufficientMoneyException e) {
                System.out.println("Insufficient Money. " + e.getMessage());
                System.out.println("Purchase failed.");
            }
            catch (DuplicateException e) {
                System.out.println(e.getMessage());
                System.out.println("Purchase failed.");
            }
            catch (NullPointerException e) {
                System.out.println("No such card exists!");
                System.out.println("Purchase failed.");
            }
        }

        else{
            if (getCardList().contains(card)){
                System.out.println("You already own this card!");
            }

            else{
                System.out.println("You earned a new card: " + card.toString());
            }
        }
    }

    public void addEmote(Emote emote){
        try{
            emote.buy(this);
            this.getEmoteList().add(emote);
            this.setGem(this.getGem() - emote.getPrice());
            System.out.println("Emote bought successfully");
        } catch (InsufficientMoneyException e) {
            System.out.println("Insufficient Money. " + e.getMessage());
            System.out.println("Purchase failed.");
        }
        catch (DuplicateException e) {
            System.out.println(e.getMessage());
            System.out.println("Purchase failed.");
        } catch (NullPointerException e) {
            System.out.println("No such emote exists!");
            System.out.println("Purchase failed.");
        }
    }

    public void showInventory(){
        System.out.println("======================");
        System.out.println(getName() + "'s Inventory:");
        System.out.println("Health: " + getHealth() + " Power: " + getPower());
        System.out.println("Gems: " + getGem());
        System.out.println("Cards: " + getCardList().size());
        System.out.println("Emotes: " + emoteList.size());
        System.out.println("(0) Close inventory.");
        System.out.println("(1) See all cards.");
        System.out.println("(2) See all emotes.");
        int choice = 1;
        while (choice != 0)
        {
            try{
                System.out.print("Your choice: ");
                Scanner scanner = new Scanner(System.in);
                choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        break;

                    case 1:
                        for (Card card : getCardList()) {
                            System.out.println(card.toString());
                        }
                        break;
                    case 2:
                        for (Emote emote: this.emoteList) {
                            System.out.println(emote.toString());
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println("======================");
    }
}
