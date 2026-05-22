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

    public static void slowPrint(String text) { slowPrint(text, 5); }//30

    public static void pause(int ms) {
        try { TimeUnit.MILLISECONDS.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

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
        int wave = 1;
        int round = 1;
        while(enemy.getHealth() > 0 && player.getHealth() > 0){
            // Player's turn
            player.showInventory();
            int choice = 0;
            while(choice != -1){
                if (!player.checkDeck()){
                    System.out.println("You don't have any card left!");
                    break;
                }
                try {
                    System.out.println("\n=====================");
                    System.out.println("  Round " + round + " | " + player.toString());
                    System.out.println("  " + enemy.getName() + " HP: " + enemy.getHealth());
                    System.out.println("=====================");
                    System.out.println("(0) Attack (1) Emote");
                    System.out.print("Your choice: ");
                    choice = input.nextInt();
                    switch (choice) {
                        case 0:
                            enemy.takeDamage(player.attack());
                            if (enemy.getCurrentCard() != null) {
                                System.out.println(enemy.getCurrentCard().toString());
                            }
                            if (enemy.getHealth() <= 0) choice = -1;
                            break;
                        case 1:
                            player.talk();
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid choice. Try again: ");
                }
                pause(300);
                if(enemy.getHealth() <= 0){
                    break;
                }
                // Enemy's turn
                player.takeDamage(enemy.attack());
                System.out.println(player.getCurrentCard().toString());
                round++;
            }
            wave++;
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
        fig1_cards.add(new MeleeCard("Goblin Machine", 5, 9, 5)); //4 base damage

        ArrayList<Card> boss1_cards = new ArrayList<Card>();
        boss1_cards.add(new MeleeCard("Mega Goblin", 15, 8, 10)); // 8 base damage
        boss1_cards.add(new MeleeCard("Goblinstein", 20, 6, 10)); // 6 base damage
        boss1_cards.add(new SpellCard("Goblin Barrel", 50, 4)); // 4 damage

        ArrayList<Card> boss2_cards = new ArrayList<Card>();
        boss2_cards.add(new RangedCard("Baby Dragon", 12, 10, 10)); // 10 base damage
        boss2_cards.add(new RangedCard("Minions", 10, 12, 10));   // 12 base damage
        boss2_cards.add(new SpellCard("Goblin Curse",  70, 6)); // 6 damage

        ArrayList<String> quotes = new ArrayList<String>();
        quotes.add("Your bones will decorate my throne!");
        quotes.add("I have crushed stronger fools than you!");
        quotes.add("Do you really believe that you can beat me?");
        quotes.add("I'm gonna show you why they call me the Boss around here.");
        quotes.add("You are in my domain now!");
        quotes.add("You fool, your existence is nothing beyond compared to me.");
        quotes.add("HEHEHEHE!");

        Figurant fig1 = new Figurant("Zog", 1, 1, fig1_cards,new ArrayList<>(), true);
        Figurant fig2 = new Figurant("Grack", 1, 2, new ArrayList<>(),new ArrayList<>(), false);
        Thief thief = new Thief("Quixle", 1, 2, new ArrayList<Card>(), new ArrayList<>());
        Boss midBoss = new Boss("Scar Face", 2, 2, boss1_cards, quotes,  1, 2);
        Boss finalBoss = new Boss("Goblin Lord", 2, 3, boss2_cards, quotes,  2, 3);

        enemies.add(fig1);
        enemies.add(fig2);
        enemies.add(midBoss);
        enemies.add(thief);
        enemies.add(finalBoss);

        Scanner input = new Scanner(System.in);

        // INTRO
        System.out.println("===========================\n" + "  GOBLINS ATE MY MEMORIES\n" +
                "===========================\n");
        slowPrint("----Chapter I: CARDS----\n");
        try{
            String story = Files.readString(Path.of("story.txt"));
            for (char c : story.toCharArray()) {
                System.out.print(c);
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
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

        // Initialising player
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

        ArrayList<Card> playerDeck = new ArrayList<Card>();
        playerDeck.add(new MeleeCard("Giant", 7, 3, 10)); // 3 base damage
        playerDeck.add(new RangedCard("Archer",3 , 6, 9)); // 5 base damage
        Player player = new Player(playerName, 1, 1, playerDeck);
        player.setGem(100);

        slowPrint("You quickly grab your old card deck:");
        for (Card c: player.getCardList()) {
            System.out.println(c.toString());
        }
        pause(500);

        // ENCOUNTER 1: FIGURANT: AGGRESSIVE
        slowPrint("As you grab your cards you remember how this world works.");
        slowPrint("All you need to do is to use the cards in your deck and cards fight for you.\n");
        if(!combat(player, enemies.getFirst(), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            return;
        }
        slowPrint(enemies.getFirst().getName() + ": I won't let you take your *** back!");
        slowPrint(enemies.getFirst().getName() + " throws your *** through window.\n");

        shop(player);

        // ENCOUNTER 2: FIGURANT: RANDOM AGGRESSIVENESS
        slowPrint("----Chapter II: HE IS HARMLESS----\n");
        slowPrint("As you are still searching for your ***, you hear a voice coming from The Deep Woods");
        slowPrint("You decide to take a look into it, thus you follow the voice");
        slowPrint("Suddenly you see the source of the voice. IT'S A GOBLIN");
        slowPrint("But it seems... upset.\n");
        if(!combat(player, enemies.get(1), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            return;
        }
        slowPrint(enemies.get(1).getName() + ": Please, help me. I just want my father back :(.");
        slowPrint(enemies.get(1).getName() + ": I know what happened your memories. I will tell you if you help me.");
        slowPrint(enemies.get(1).getName() + ": They are kept in the ***. That's why you can't remember anything.");
        slowPrint(enemies.get(1).getName() + " suddenly realizes something and starts running away.\n");

        shop(player);

        // ENCOUNTER 3: MIDBOSS
        slowPrint("----Chapter III: Right Arm----\n");
        slowPrint("");
        slowPrint("");
        slowPrint("");
        slowPrint("\n");
        if(!combat(player, enemies.get(2), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            return;
        }
        slowPrint(enemies.get(2).getName() + ": ");
        slowPrint(enemies.get(2).getName() + ": ");
        slowPrint(enemies.get(2).getName() + ": ");
        slowPrint(enemies.get(2).getName() + " \n");

        shop(player);

        // ENCOUNTER 4: THIEF
        slowPrint("----Chapter IV: WHERE IS MY CARD?----\n");
        slowPrint("");
        slowPrint("");
        slowPrint("");
        slowPrint("\n");
        if(!combat(player, enemies.get(3), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            return;
        }
        slowPrint(enemies.get(3).getName() + ": ");
        slowPrint(enemies.get(3).getName() + ": ");
        slowPrint(enemies.get(3).getName() + ": ");
        slowPrint(enemies.get(3).getName() + " \n");

        shop(player);

        // ENCOUNTER 5: FINAL BOSS
        slowPrint("----Chapter V: MEMORIES----\n");
        slowPrint("");
        slowPrint("");
        slowPrint("");
        slowPrint("\n");
        if(!combat(player, enemies.get(4), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            return;
        }
        slowPrint(enemies.get(4).getName() + ": ");
        slowPrint(enemies.get(4).getName() + ": ");
        slowPrint(enemies.get(4).getName() + ": ");
        slowPrint(enemies.get(4).getName() + " \n");
    }
}
