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
    public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();

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

    // Combat Scene
    public static boolean combat(Player player, Enemy enemy){
        Scanner input = new Scanner(System.in);
        slowPrint("\n---ENCOUNTER---" + enemy.getName());
        slowPrint(enemy.toString());

        if (enemy instanceof Boss boss){
            boss.talk();
            pause(1000);
        }

        int round = 1;
        while(enemy.getHealth() > 0 && player.getHealth() > 0){
            player.showInventory();
            System.out.println("\n=====================");
            System.out.println("  Round " + round + " | " + player.toString());
            System.out.println("  " + enemy.getName() + " HP: " + enemy.getHealth());
            System.out.println("=====================");
            System.out.println("(0) Attack (1) Emote");
            System.out.print("Your choice: ");

            while(true){
                try {
                    int choice = input.nextInt();
                    switch (choice) {
                        case 0:
                            int playerDmg = player.attack();
                            if(enemy.getCurrentCard() instanceof TroopCard tc) {
                                tc.takeDamage(playerDmg);
                            }
                            break;
                        case 1:
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid choice. Try again: ");
                }
            }
        }

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
        Random gen = new Random();
        // Initialising enemies
        ArrayList<Card> fig1_cards = new ArrayList<Card>();
        fig1_cards.add(new MeleeCard("Goblin Machine", 5, 9, 5));

        ArrayList<Card> fig2_cards = new ArrayList<Card>();
        fig2_cards.add(new RangedCard("Dart Goblin", 6, 8, 8));

        ArrayList<Card> boss1_cards = new ArrayList<Card>();
        boss1_cards.add(new MeleeCard("Mega Goblin", 15, 8, 10));
        boss1_cards.add(new MeleeCard("Goblinstein", 8, 6, 10));
        boss1_cards.add(new SpellCard("Goblin Barrel", 50, 4));

        ArrayList<Card> boss2_cards = new ArrayList<Card>();
        boss2_cards.add(new RangedCard("Baby Dragon", 8, 10, 5));
        boss2_cards.add(new RangedCard("Minions", 10, 8, 5));
        boss2_cards.add(new SpellCard("Goblin Curse",  70, 6));

        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("Your bones will decorate my throne!");
        quotes.add("I have crushed stronger fools than you!");
        quotes.add("Do you really believe that you can beat me?");
        quotes.add("I'm gonna show you why they call me the Boss around here.");
        quotes.add("You are in my domain now!");
        quotes.add("You fool, your existence is nothing beyond compared to me.");
        quotes.add("HEHEHEHE!");

        Figurant fig1 = new Figurant("Zog", 1, 1, fig1_cards,new ArrayList<>(), gen.nextBoolean());
        Figurant fig2 = new Figurant("Grack", 1, 2, fig2_cards,new ArrayList<>(), gen.nextBoolean());
        Thief thief = new Thief("Quixle", 1, 2, new ArrayList<Card>(), new ArrayList<>());
        Boss midBoss = new Boss("Scar Face", 2, 2, boss1_cards, quotes,  1, 2);
        Boss finalBoss = new Boss("Goblin Lord", 2, 3, boss2_cards, quotes,  2, 3);

        enemies.add(fig1);
        enemies.add(fig2);
        enemies.add(thief);
        enemies.add(midBoss);
        enemies.add(finalBoss);

        Scanner input = new Scanner(System.in);
        // INTRO
        System.out.println("===========================\n" + "  GOBLINS ATE MY MEMORIES\n" +
                "===========================\n");
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
        pause(500);

        String playerName = "Hero";

        while(true){
            System.out.println("What was your name: ");
            try{
                playerName = input.nextLine();
                System.out.println("Right, your name was " + playerName);
                break;
            } catch (InputMismatchException e){
                System.out.println("Invalid input. Try again." + e.getMessage());
            }
        }

        // Initialising player
        ArrayList<Card> playerDeck = new ArrayList<Card>();
        playerDeck.add(new MeleeCard("Giant", 7, 3, 5));
        playerDeck.add(new RangedCard("Archer",3 , 5, 5));
        Player player = new Player(playerName, 1, 1, playerDeck);
        player.setGem(100);

        slowPrint("You quickly grab your old cards:\n");
        for (Card c: player.getCardList()) {
            System.out.println(c.toString());
        }
        pause(500);

        // ENCOUNTER 1: FIGURANT: RANDOM AGGRESSIVENESS

    }
}
