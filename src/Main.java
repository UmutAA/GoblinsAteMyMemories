import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {
    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    // ─────────────────────────────────────────────────────────────
    //  Utility: slow-print for atmosphere
    // ─────────────────────────────────────────────────────────────
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

    // ─────────────────────────────────────────────────────────────
    //  Combat loop – returns true if player survives
    // ─────────────────────────────────────────────────────────────
    public static boolean combat(Player player, Enemy enemy, Scanner input) {
        slowPrint("\nENCOUNTER: " + enemy.getName());
        slowPrint(enemy.toString());
        pause(400);

        // Boss taunts at start of fight
        if (enemy instanceof Boss boss) {
            boss.talk();
            pause(1500);
        }

        int round = 1;
        while (player.getHealth() > 0 && enemy.getHealth() > 0) {

            System.out.println("\n=====================");
            System.out.println("  Round " + round + " | " + player.toString());
            System.out.println("  " + enemy.getName() + " HP: " + enemy.getHealth());
            System.out.println("=====================");
            System.out.println("(1) Attack    (2) Inventory    (3) Emote");
            System.out.print("Your choice: ");

            int choice = 0;
            try { choice = input.nextInt();
            }
            catch (InputMismatchException e) { input.nextLine(); }

            if (enemy.getHealth() <= 0) break;

            // ── Enemy turn ──
            pause(600);
            System.out.println("\n-- " + enemy.getName() + "'s turn --");

            // Thief: steals first if it hasn't yet, then attacks with stolen cards
            if (enemy instanceof Thief thief) {
                if (thief.getStolenCards().isEmpty() && !player.getCardList().isEmpty()) {
                    thief.steal(player);
                }
            }

            int enemyDmg = enemy.attack();
            player.takeDamage(enemyDmg);
            System.out.printf(">> %s attacked you for %d damage! You have %d HP left.%n",
                    enemy.getName(), enemyDmg, player.getHealth());

            round++;
            pause(300);
        }

        if (player.getHealth() <= 0) {
            slowPrint("\n You have fallen in battle. The darkness claims you...");
            return false;
        }

        slowPrint("\n Victory! " + enemy.getName() + " has been defeated!");
        enemy.getReward(player);
        System.out.println("Updated stats: " + player.toString());
        return true;
    }

    // ─────────────────────────────────────────────────────────────
    //  Shop
    // ─────────────────────────────────────────────────────────────
    public static void shop(Player target) {
        slowPrint("\nYou notice a hooded figure crouched beside a makeshift stall...");
        Scanner localInput = new Scanner(System.in);
        while (true) {
            System.out.print("Would you like to take a closer look? (Y/N): ");
            try {
                String answer = localInput.nextLine().trim();
                if (answer.equalsIgnoreCase("N")) {
                    slowPrint("You walk past. The trader shrugs.");
                    return;
                }
                if (answer.equalsIgnoreCase("Y")) break;
                System.out.println("Invalid input. Try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
            }
        }

        slowPrint("Wandering Trader: \"Welcome, weary traveller, to the Royal Market!\"");
        slowPrint("Wandering Trader: \"I accept only gems. No gems, no goods.\"");
        System.out.println("Your gems: " + target.getGem());

        ArrayList<Emote> emotes = new ArrayList<>();
        ArrayList<Card>  cards  = new ArrayList<>();
        Random gen = new Random();

        SpellCard zap      = new SpellCard("Zap",      gen.nextInt(11) + 10,  gen.nextInt(6)  + 10);
        SpellCard fireball = new SpellCard("Fireball",  gen.nextInt(11) + 30,  gen.nextInt(21) + 30);
        SpellCard poison   = new SpellCard("Poison",    gen.nextInt(17) + 20,  gen.nextInt(16) + 15);
        cards.add(zap); cards.add(fireball); cards.add(poison);

        emotes.add(new Emote("HIHIHAHA!",    150));
        emotes.add(new Emote("(^-^)/",        75));
        emotes.add(new Emote("-_(0)o(0)/",    25));

        Scanner scanner = new Scanner(System.in);
        int choice = 1;
        while (choice != 0) {
            System.out.println("\nWhat would you like?");
            System.out.println("  0) Leave the shop");
            System.out.println("  1) Browse Emotes");
            System.out.println("  2) Browse Spell Cards");
            System.out.print("Your choice: ");
            try {
                choice = scanner.nextInt();
                switch (choice) {
                    case 0 -> slowPrint("Wandering Trader: \"Safe travels!\"");
                    case 1 -> {
                        System.out.println("\nAvailable Emotes:");
                        for (int i = 0; i < emotes.size(); i++)
                            System.out.printf("  %d) %s%n", i, emotes.get(i).toString());
                        System.out.print("Which one? (index): ");
                        int idx = scanner.nextInt();
                        if (idx >= 0 && idx < emotes.size()) {
                            target.addEmote(emotes.get(idx));
                        } else {
                            System.out.println("No such item.");
                        }
                        choice = 1;
                    }
                    case 2 -> {
                        System.out.println("\nAvailable Spell Cards:");
                        for (int i = 0; i < cards.size(); i++)
                            System.out.printf("  %d) %s%n", i, cards.get(i).toString());
                        System.out.print("Which one? (index): ");
                        int idx = scanner.nextInt();
                        if (idx >= 0 && idx < cards.size()) {
                            target.addCard(cards.get(idx));
                        } else {
                            System.out.println("No such item.");
                        }
                        choice = 1;
                    }
                    default -> { System.out.println("Unknown choice."); choice = 1; }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try again.");
                scanner.nextLine();
            }
        }
    }
    //  Enemy factory helpers

    // Creates a Figurant with random aggressiveness each time it's built.
    public static Figurant buildFigurant(String name, int health, int power, boolean forceAggressive) {
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(new MeleeCard("Rusty Sword", 30, 20, 8));
        deck.add(new RangedCard("Short Bow",  20, 25, 6));

        ArrayList<String> quotes = new ArrayList<>();
        quotes.add("*growls*");
        quotes.add("You shall not pass!");
        quotes.add("Turn back now...");

        Random gen = new Random();
        // If forceAggressive is false, aggressiveness is random (50/50)
        boolean aggressive = forceAggressive || gen.nextBoolean();
        return new Figurant(name, health, power, deck, quotes, aggressive);
    }

    //Creates a Thief with a small card deck (it attacks with *stolen* cards)
    public static Thief buildThief(String name, int health, int power) {
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(new MeleeCard("Dagger", 15, 30, 5));  // only used for steal index calc

        ArrayList<String> quotes = new ArrayList<>();
        quotes.add("Heh, nice cards you have there...");
        quotes.add("You won't miss these!");
        quotes.add("Nothing personal, just business.");

        return new Thief(name, health, power, deck, quotes);
    }

    // Creates a mid-game Boss.
    public static Boss buildMidBoss() {
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(new MeleeCard("War Axe",     60, 35, 20));
        deck.add(new RangedCard("Heavy Crossbow", 50, 30, 18));
        deck.add(new SpellCard("Dark Bolt",   0,  25));   // price 0 = boss doesn't buy it

        ArrayList<String> quotes = new ArrayList<>();
        quotes.add("RAAAAH! You dare challenge the Warden?!");
        quotes.add("I have crushed stronger fools than you!");
        quotes.add("Your bones will decorate my throne!");

        return new Boss("Warden Kargoth", 40, 5, deck, quotes, 2, 2);
    }

    /** Creates the Final Boss. */
    public static Boss buildFinalBoss() {
        ArrayList<Card> deck = new ArrayList<>();
        deck.add(new MeleeCard("Soul Reaper",       80, 50, 35));
        deck.add(new RangedCard("Void Arrow",        70, 45, 30));
        deck.add(new SpellCard("Oblivion Blast",     0,  50));

        ArrayList<String> quotes = new ArrayList<>();
        quotes.add("I have waited an eternity for a worthy soul to consume...");
        quotes.add("Every step you took — I guided.");
        quotes.add("Darkness is not your enemy. I AM.");
        quotes.add("You cannot kill what was never alive!");

        return new Boss("The Eternal Shadow", 60, 8, deck, quotes, 3, 3);
    }

    //MAIN
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Random gen = new Random();

        //Intro story
        String story =
                "=============\n" + "GOBLINS ATE MY MEMORIES\n" + "=============\n";
        System.out.print(story);
        pause(500);

        // Try to load story.txt, fall back to inline story if missing
        try {
            String fileStory = Files.readString(Path.of("story.txt"));
            slowPrint(fileStory, 1);
        } catch (FileNotFoundException e) {
            slowPrint("The kingdom of Eldenmoor has fallen into darkness.");
            slowPrint("An ancient evil — the Eternal Shadow — stirs beyond the Veil.");
            slowPrint("Heroes who ventured into the cursed forest never returned.");
            slowPrint("You are the last hope. Armed with enchanted cards, you step forward...\n");
        } catch (IOException | NullPointerException e) {
            slowPrint("Error reading story file. Starting game...\n");
        }

        //Player name
        String playerName = "Hero";
        while (true) {
            System.out.print("Enter your name, brave one: ");
            try {
                playerName = input.nextLine().trim();
                if (!playerName.isEmpty()) {
                    slowPrint("So, " + playerName + " steps into the cursed forest...\n");
                    break;
                }
                System.out.println("A hero must have a name. Try again.");
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("Invalid input. Try again.");
            }
        }

        // Build player with a starting deck
        ArrayList<Card> startDeck = new ArrayList<>();
        startDeck.add(new MeleeCard("Iron Sword", 50, 30, 10));
        startDeck.add(new RangedCard("Longbow",    40, 35, 9));

        Player player = new Player(playerName, 100, 3, startDeck);

        // Give a small starting gem bonus so early shop is accessible
        player.setGem(50);

        slowPrint("Starting deck:");
        for (Card c : player.getCardList()) System.out.println("  " + c.toString());
        System.out.println();
        pause(600);

        //  ENCOUNTER 1 – Figurant (random aggressiveness)
        slowPrint("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("CHAPTER 1: The Forest Path");
        slowPrint("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("You follow a narrow dirt path through gnarled trees.");
        slowPrint("A shambling figure blocks your way...");
        pause(400);

        Figurant enc1 = buildFigurant("Forest Wanderer", 30, 2, false);
        if (!enc1.isAggressive()) {
            slowPrint("Forest Wanderer: \"...leave me alone.\" He steps aside.");
            slowPrint("The wanderer is not hostile. You pass safely.\n");
        } else {
            if (!combat(player, enc1, input)) { endGame(false, playerName); return; }
        }

        //  ENCOUNTER 2 – Thief (always triggers, random steal outcome)
        slowPrint("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("CHAPTER 2: The Stolen Crossroads");
        slowPrint("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("You rest by a ruined shrine. Your pack feels lighter...");
        slowPrint("A shadow darts between the trees — a thief!");
        pause(400);

        Thief enc2 = buildThief("Sly Rook", 35, 2);
        if (!combat(player, enc2, input)) { endGame(false, playerName); return; }

        // Shop after encounter 2
        shop(player);

        //  ENCOUNTER 3 – Figurant (random aggressiveness)
        slowPrint("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("CHAPTER 3: The Corrupted Village");
        slowPrint("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("A village — or what remains of one. Twisted villagers roam the streets.");

        Figurant enc3 = buildFigurant("Corrupted Villager", 40, 3, false);
        if (!enc3.isAggressive()) {
            slowPrint("Corrupted Villager: *stares blankly and shuffles away.*");
            slowPrint("This one is harmless... for now.\n");
        } else {
            slowPrint("Corrupted Villager: \"YOU DON'T BELONG HERE!\"");
            if (!combat(player, enc3, input)) { endGame(false, playerName); return; }
        }

        //  ENCOUNTER 4 – Thief (random 70% chance to appear)
        slowPrint("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("CHAPTER 4: The Dark Alley");
        slowPrint("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("You slip through a narrow alley. You sense eyes watching you...");

        boolean thiefAppears = gen.nextInt(10) < 7; // 70% chance
        if (thiefAppears) {
            slowPrint("A knife glints in the dark. \"Your cards or your life!\"");
            Thief enc4 = buildThief("Shadow Pickpocket", 45, 3);
            if (!combat(player, enc4, input)) { endGame(false, playerName); return; }
        } else {
            slowPrint("...Nothing. The alley was empty. Lucky.");
        }

        //Shop before boss
        shop(player);

        //  ENCOUNTER 5 – Mid-Boss: Warden Kargoth
        slowPrint("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("CHAPTER 5: The Iron Fortress Gate");
        slowPrint("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("A massive iron gate bars your path. It groans open...");
        slowPrint("A hulking armoured warrior steps forward, axe raised.");
        pause(600);

        Boss midBoss = buildMidBoss();
        midBoss.talk(); pause(1200);
        if (!combat(player, midBoss, input)) { endGame(false, playerName); return; }

        slowPrint("\nThe gate crumbles. Beyond it lies the Veil itself.");
        slowPrint("You feel the air grow cold. Something ancient stirs...");
        pause(800);

        //Final shop before final boss
        shop(player);

        //  ENCOUNTER 6 – FINAL BOSS: The Eternal Shadow
        slowPrint("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("FINAL CHAPTER: Beyond the Veil");
        slowPrint("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        slowPrint("The world dissolves into absolute darkness.");
        slowPrint("A voice — not heard, but FELT — reverberates through your bones.");
        pause(700);

        Boss finalBoss = buildFinalBoss();
        finalBoss.talk();
        pause(2000);

        slowPrint("\n\"" + playerName + "...  so you actually made it this far.\"");
        slowPrint("\"Then come. Let me show you what true oblivion feels like.\"");
        pause(800);

        if (!combat(player, finalBoss, input)) { endGame(false, playerName); return; }

        // ── Victory ───────────────────────────────────────────────
        endGame(true, playerName);
        input.close();
    }

    // ─────────────────────────────────────────────────────────────
    //  End screen
    // ─────────────────────────────────────────────────────────────
    public static void endGame(boolean won, String playerName) {
        pause(500);
        if (won) {
            slowPrint("\n╔══════════════════════════════════════════════════════╗");
            slowPrint(  "║                   ★  VICTORY  ★                     ║");
            slowPrint(  "╚══════════════════════════════════════════════════════╝");
            slowPrint("The Eternal Shadow dissolves into silence.");
            slowPrint("Light floods back into the kingdom of Eldenmoor.");
            slowPrint("Songs will be sung of " + playerName + " for a thousand years.");
            slowPrint("\n             ★  Thank you for playing!  ★\n");
        } else {
            slowPrint("\n╔══════════════════════════════════════════════════════╗");
            slowPrint(  "║                  ✦  GAME OVER  ✦                    ║");
            slowPrint(  "╚══════════════════════════════════════════════════════╝");
            slowPrint("The darkness swallows " + playerName + " whole.");
            slowPrint("Eldenmoor falls silent. The Shadow reigns eternal.");
            slowPrint("\n             Better luck next time, hero.\n");
        }
    }
}