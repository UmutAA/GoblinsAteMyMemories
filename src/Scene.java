import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * A static class used for the scenes such as combat system and shop mechanism.
 * Also contains print methods for a better atmosphere.
 */
public class Scene {

    /**
     * A helpful tool function for storytelling which slows the print operation.
     * @param text: String to print.
     * @param delayMs: delay parameter for slowing down printing operation.
     */
    public static void slowPrint(String text, int delayMs) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try { TimeUnit.MILLISECONDS.sleep(delayMs); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        System.out.println();
    }

    /**
     * Specialised version of slowPrint with given delay parameter.
     * @param text String to print.
     */
    public static void slowPrint(String text) { slowPrint(text, 30); }

    /**
     * A helpful tool function for better storytelling. This functions makes the program stop for given time period.
     *  @param ms: pause time
     */
    public static void pause(int ms) {
        try { TimeUnit.MILLISECONDS.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    /**
     * Combat method made to handle the combat system.
     * @param player Player
     * @param enemy The enemy with whom player's having a combat
     * @param input Scanner object(Given as parameter for better performance)
     * @return Returns the result of the combat. If player wins, returns True, else returns False.
     */
    // Combat Scene
    public static boolean combat(Player player, Enemy enemy, Scanner input){
        slowPrint("---ENCOUNTER---");
        slowPrint(enemy.toString());

        if (enemy instanceof Boss boss){
            boss.talk();
            pause(1000);
        }

        if (enemy instanceof Thief thief) {
            if (thief.getStolenCards().isEmpty() && !player.getCardList().isEmpty()) {
                thief.steal(player);
                thief.setCurrentCard(thief.getStolenCards().getFirst());
            }
        }

        int round = 1;
        while(enemy.getHealth() > 0 && player.getHealth() > 0){
            // Player's turn
            player.showInventory();
            int choice = 0;
            boolean combat = true;
            while(combat){
                if (!player.checkDeck()){
                    System.out.println("You don't have any card left!");
                    combat = false;
                }
                boolean attacked = false;
                while (!attacked) {
                    try {
                        System.out.println("\n=====================");
                        System.out.println("  Round " + round + " | " + player.toString() + " vs " + enemy.toString());
                        System.out.println("  " + enemy.getName() + " HP: " + enemy.getHealth());
                        System.out.println("=====================");
                        System.out.println("(0) Attack (1) Emote");
                        System.out.print("Your choice: ");
                        choice = input.nextInt();
                        switch (choice) {
                            case 0:
                                enemy.takeDamage(player.attack());
                                if (enemy.getHealth() <= 0) combat = false;
                                attacked = true;
                                break;
                            case 1:
                                player.talk();
                                break;
                            default:
                                input.nextLine();
                                System.out.println("Invalid choice.");
                        }
                    } catch (InputMismatchException e) {
                        input.nextLine();
                        System.out.println("Invalid input. Try again: ");
                    } catch (Exception e) {
                        input.nextLine();
                        System.out.println(e.getMessage());
                    }
                }

                pause(300);
                if(enemy.getHealth() <= 0){
                    combat = false;
                    break;
                }
                // Enemy's turn
                player.takeDamage(enemy.attack());
                if(player.getHealth() <= 0){
                    combat = false;
                }
                round++;
            }
            pause(300);
        }
        if (enemy.getHealth() <= 0){
            slowPrint("\n Victory! " + enemy.getName() + " has been defeated!");
            enemy.getReward(player);
            player.resetDeck();
            return true;
        }

        else{
            slowPrint("\n You have fallen in battle. The darkness claims you...");
            return false;
        }
    }

    /**
     * Shop method made to handle the shop system.
     * @param target Player interacting with the shop.
     */
    // Shop Scene
    public static void shop(Player target){
        slowPrint("You saw a silhouette in the woods!");
        Scanner input = new Scanner(System.in);
        while(true){
            slowPrint("Would you like to take a closer look(Y/N)? ");
            try {
                String answer = input.nextLine();
                if(answer.equalsIgnoreCase("N")){
                    slowPrint("You ignored the silhouette and kept going.");
                    return;
                }
                else if(answer.equalsIgnoreCase("Y")){
                    break;
                }
                else{
                    System.out.println("Invalid input. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
            } catch (Exception e) {
                input.nextLine();
                System.out.println(e.getMessage());
            }
        }
        slowPrint("Wandering Trader: Welcome to the Royal Market!");
        ArrayList<Emote> emotes = new ArrayList<Emote>();
        ArrayList<Card> cards = new ArrayList<Card>();
        Random gen = new Random();
        SpellCard zap = new SpellCard("Zap", gen.nextInt(11) + 10,
                gen.nextInt(4) + 3);
        SpellCard fireball = new SpellCard("Fireball", gen.nextInt(11) + 30,
                gen.nextInt(6) + 5);
        SpellCard poison = new SpellCard("Poison", gen.nextInt(17) + 20,
                gen.nextInt(4) + 4);
        cards.add(zap);
        cards.add(fireball);
        cards.add(poison);

        Emote laugh = new Emote("HIHIHAHA!", 150);
        Emote greeting = new Emote("(^-^)/", 75);
        Emote shocked = new Emote("-_(0)o(0)/", 25);
        emotes.add(laugh);
        emotes.add(greeting);
        emotes.add(shocked);
        int choice = 1;
        Scanner scanner = new Scanner(System.in);
        while (choice != 0)
        {
            System.out.println("What would you like to purchase?");
            System.out.println("0) Exit");
            System.out.println("1) Emote");
            System.out.println("2) Spell Card");
            System.out.print("Your choice: ");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 0:
                        System.out.println("Good Bye!");
                        break;

                    case 1:
                        System.out.println("Available Emotes:");
                        for (int i = 0; i < emotes.size(); i++) {
                            System.out.printf("%d) %s\n", i, emotes.get(i).toString());
                        }
                        while (true) {
                            try {
                                System.out.print("Which one do you like: ");
                                choice = scanner.nextInt();
                                target.addEmote(emotes.get(choice));
                                choice = 1;
                                break;
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("Invalid emote. Try again.");
                            } catch (InputMismatchException e) {
                                scanner.nextLine();
                                System.out.println("Invalid input. Try again.");
                            } catch (Exception e) {
                                scanner.nextLine();
                                System.out.println(e.getMessage());
                            }
                        }
                        break;

                    case 2:
                        System.out.println("Available Spell Cards:");
                        for (int i = 0; i < cards.size(); i++) {
                            System.out.printf("%d) %s\n", i, cards.get(i).toString());
                        }
                        while (true) {
                            try {
                                System.out.print("Which one do you like: ");
                                choice = scanner.nextInt();
                                target.addCard(cards.get(choice));
                                choice = 1;
                                break;
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println("Invalid card. Try again.");
                            } catch (InputMismatchException e) {
                                scanner.nextLine();
                                System.out.println("Invalid input. Try again.");
                            } catch (Exception e){
                                scanner.nextLine();
                                System.out.println(e.getMessage());
                            }
                        }
                        break;

                    default:
                        System.out.println("Unknown choice. Please choose again.");
                        choice = 1;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Try again.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
