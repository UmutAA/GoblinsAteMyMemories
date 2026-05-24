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

    /**
     * Attack method to be used in the combat system. Takes an input for card choice and plays the card
     * @return Total damage output.
     */
    public int attack() {
        int damage = 0;
        System.out.println("Your Deck: ");
        for (int i = 0; i < this.getCardList().size(); i++) {
            System.out.printf("%d) %s\n", i, getCardList().get(i).toString());
        }
        boolean played = false;
        while (!played) {
            System.out.print("Choose your card: ");
            Scanner scanner = new Scanner(System.in);
            try {
                int choice = scanner.nextInt();
                Card chosen = getCardList().get(choice);

                if (chosen instanceof TroopCard) {
                    // Önce currentCard'ı güncelle, sonra oyna
                    setCurrentCard(chosen);
                    damage = chosen.playCard();

                } else if (chosen instanceof SpellCard sc) {
                    damage = chosen.playCard() * 3;
                    // Spell'i listeden kaldır
                    this.getCardList().remove(choice);
                    // Mevcut ilk TroopCard'ı currentCard yap
                    boolean found = false;
                    for (Card c : this.getCardList()) {
                        if (c instanceof TroopCard tc && tc.isAvailable()) {
                            setCurrentCard(c);
                            found = true;
                            break; // ← ESKİDE BU YOKTU, hep son karta yazıyordu
                        }
                    }
                    if (!found && !getCardList().isEmpty()) {
                        setCurrentCard(getCardList().getFirst());
                    }
                }

                played = true;

            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
            } catch (UnavailableCardException e) {
                System.out.println("Can't play this card: " + e.getMessage());
                System.out.println("Please play an available card!");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Can't play this card: Invalid card number!");
                System.out.println("Please play a valid card!");
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Can't play this card: " + e.getMessage());
            }
        }
        return (damage * this.getPower());
    }

    @Override
    public String toString(){
        return String.format("[Player: %s, Gem: %d, Health: %d, Power: %d]",getName(),getGem(),getHealth(),getPower());
    }

    public void talk(){
        if (emoteList.isEmpty()){
            System.out.println("There are no emotes to talk.");
            return;
        }
        for (int i = 0; i < emoteList.size(); i++) {
            System.out.printf("%d. %s\n", i, emoteList.get(i).getEmoteMessage());
        }
        System.out.print("Please enter emote index: ");
        boolean talked = false;
        while (!talked){
            Scanner input = new Scanner(System.in);
            try{
                int choice = input.nextInt();
                Emote emote = emoteList.get(choice);
                System.out.println("\n" + this.getName() + ": " + emote.getEmoteMessage());
                talked = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input:" + e.getMessage());
                System.out.println("Please enter a valid emote index: ");
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("Invalid emote index:" + e.getMessage());
                System.out.println("Please enter a valid emote index: ");
            } catch (Exception e) {
                input.nextLine();
                System.out.println(e.getMessage());
            }
        }
    }

    public void addCard(Card card){
        if (card instanceof SpellCard c){
            try{
                c.buy(this);
                this.getCardList().add(c);
                this.setGem(this.getGem() - c.getPrice());
                System.out.println("Spell bought successfully");
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
                this.getCardList().add(card);
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
                        if (getCardList().isEmpty()){
                            System.out.println("There are no cards to show.");
                        }
                        for (Card card : getCardList()) {
                            System.out.println(card.toString());
                        }
                        break;
                    case 2:
                        if (getEmoteList().isEmpty()){
                            System.out.println("There are no emotes to show.");
                        }
                        for (Emote emote: getEmoteList()) {
                            System.out.println(emote.toString());
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid choice. Please try again.");
            } catch (NullPointerException e) {
                System.out.println("No such emote exists!");
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        System.out.println("======================");
    }
}
