import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameEngine {
    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    // Slow Print tool for atmosphere
    public static void slowPrint(String text, int delayMs) {
        for (char c : text.toCharArray()) {
            System.out.print(c);
            try { TimeUnit.MILLISECONDS.sleep(delayMs); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
        System.out.println();
    }

    public static void slowPrint(String text) { slowPrint(text, 30); }

    public static void pause(int ms) {
        try { TimeUnit.MILLISECONDS.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
    // Shop Scene
    public static void shop(Player target){
        System.out.println("You saw a silhouette in the woods!");
        while(true){
            System.out.print("Would you like to take a closer look(Y/N)? ");
            try {
                Scanner input = new Scanner(System.in);
                String answer = input.nextLine();
                if(answer.equalsIgnoreCase("N")){
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
            }
        }
        System.out.println("Wandering Trader: Welcome to the Royal Market!");
        ArrayList<Emote> emotes = new ArrayList<Emote>();
        ArrayList<Card> cards = new ArrayList<Card>();
        Random gen = new Random();
        SpellCard zap = new SpellCard("Zap", gen.nextInt(11) + 10,
                gen.nextInt(6) + 10);
        SpellCard fireball = new SpellCard("Fireball", gen.nextInt(11) + 30,
                gen.nextInt(21) + 30);
        SpellCard poison = new SpellCard("Poison", gen.nextInt(17) + 20,
                gen.nextInt(16) + 15);
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
                        System.out.print("Which one do you like: ");
                        choice = scanner.nextInt();
                        target.addEmote(emotes.get(choice));
                        choice = 1;
                        break;

                    case 2:
                        System.out.println("Available Spell Cards:");
                        for (int i = 0; i < cards.size(); i++) {
                            System.out.printf("%d) %s\n", i, cards.get(i).toString());
                        }
                        System.out.print("Which one do you like: ");
                        choice = scanner.nextInt();
                        target.addCard(cards.get(choice));
                        choice = 1;
                        break;

                    default:
                        System.out.println("Unknown choice. Please choose again.");
                        choice = 1;
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        try{
            String story = Files.readString(Path.of("story.txt"));
            for (char c : story.toCharArray()) {
                System.out.print(c);
                try {
                    TimeUnit.MILLISECONDS.sleep(75);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println();
        } catch (FileNotFoundException e){
            System.out.println("Story file not found." + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading story file." + e.getMessage());
        } catch (NullPointerException e){
            System.out.println("Story file path not found." + e.getMessage());
        }

        String name;

        while(true){
            System.out.println("What is your name: ");
            try{
                name = input.nextLine();
                System.out.println("Right, your name is " + name);
                break;
            } catch (InputMismatchException e){
                System.out.println("Invalid input. Try again." + e.getMessage());
            }
        }


    }
}
