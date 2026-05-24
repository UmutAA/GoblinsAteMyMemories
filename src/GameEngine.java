import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Main class in which game's running
 */
public class GameEngine {
    public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    public static void main(String[] args) {
        Random gen = new Random();
        // Initialising enemies
        ArrayList<Card> fig1_cards = new ArrayList<Card>();
        fig1_cards.add(new MeleeCard("Jake the Runner", 5, 9, 5)); //4 base damage

        ArrayList<Card> boss1_cards = new ArrayList<Card>();
        boss1_cards.add(new MeleeCard("Mighty Rose", 15, 8, 7)); // 5 base damage
        boss1_cards.add(new MeleeCard("Muscular Pristine", 15, 6, 9)); // 5 base damage
        boss1_cards.add(new SpellCard("Guilt", 50, 4)); // 4 damage

        ArrayList<Card> boss2_cards = new ArrayList<Card>();
        boss2_cards.add(new RangedCard("Sarah the Archer", 12, 9, 9)); // 8 base damage
        boss2_cards.add(new RangedCard("Simon the Archer", 10, 10, 10));   // 10 base damage
        boss2_cards.add(new SpellCard("Curse of the Eternal Lie",  70, 5)); // 5 damage

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
        Boss midBoss = new Boss("Scar Face", 2, 1, boss1_cards, quotes,  1, 2);
        Boss finalBoss = new Boss("Goblin Lord", 2, 2, boss2_cards, quotes,  2, 2);

        enemies.add(fig1);
        enemies.add(fig2);
        enemies.add(midBoss);
        enemies.add(thief);
        enemies.add(finalBoss);

        Scanner input = new Scanner(System.in);

        // INTRO
        System.out.println("===========================\n" + "  GOBLINS ATE MY MEMORIES\n" +
                "===========================\n");
        Scene.slowPrint("----Chapter I: CARDS----\n");
        try{
            String story = Files.readString(Path.of("story.txt"));
            Scene.slowPrint(story, 30);
        } catch (FileNotFoundException e){
            System.out.println("Story file not found." + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading story file." + e.getMessage());
        } catch (NullPointerException e){
            System.out.println("Story file path not found." + e.getMessage());
        }
        Scene.pause(500);

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
        playerDeck.add(new MeleeCard("Giant", 7, 4, 13)); // 5 base damage
        playerDeck.add(new RangedCard("Archer",3 , 6, 9)); // 5 base damage
        Player player = new Player(playerName, 2, 2, playerDeck);
        player.setGem(150);

        Scene.slowPrint("You quickly grab your old card deck:");
        for (Card c: player.getCardList()) {
            System.out.println(c.toString());
        }
        Scene.pause(500);

        // ENCOUNTER 1: FIGURANT: AGGRESSIVE
        Scene.slowPrint("As you grabbed your cards you remember how this world works.");
        Scene.slowPrint("All you need to do is to use the cards in your deck and cards fight for you.\n");
        if(!Scene.combat(player, enemies.getFirst(), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            System.out.println("Your stats: " + player.toString());
            return;
        }
        Scene.slowPrint(enemies.getFirst().getName() + ": I won't let you forget!");
        Scene.slowPrint(enemies.getFirst().getName() + " jumps through the window and escapes with your ***.\n");

        Scene.shop(player);

        // ENCOUNTER 2: FIGURANT: NOT AGGRESSIVE
        Scene.slowPrint("----Chapter II: HE IS HARMLESS----\n");
        Scene.slowPrint("As you were still searching for your ***, you heard a voice coming from The Deep Woods");
        Scene.slowPrint("You decided to take a look into it, thus you followed the voice");
        Scene.slowPrint("Suddenly you saw the source of the voice. IT'S A GOBLIN");
        Scene.slowPrint("But it seemed... upset.\n");
        if(!Scene.combat(player, enemies.get(1), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            System.out.println("Your stats: " + player.toString());
            return;
        }
        Scene.slowPrint(enemies.get(1).getName() + ": Please, help me. I just want my father back :(.");
        Scene.slowPrint(enemies.get(1).getName() + ": I know what happened your memories. I will tell you if you help me.");
        Scene.slowPrint(enemies.get(1).getName() + ": They are kept in the castle. That's why you can't remember anything.");
        Scene.slowPrint(enemies.get(1).getName() + " suddenly realized something and started running away.\n");
        Scene.slowPrint("You went after him but couldn't keep up.");
        Scene.slowPrint("You've decided to keep walking, searching for the castle.");

        Scene.shop(player);

        int health = player.getHealth();
        // ENCOUNTER 3: MIDBOSS
        Scene.slowPrint("----Chapter III: Right Arm----\n");
        Scene.slowPrint("The deeper you walk into the woods, the quieter the world becomes.");
        Scene.slowPrint("Even the wind avoids this place.");
        Scene.slowPrint("You suddenly notice broken cards hanging from tree branches.");
        Scene.slowPrint("Each one carries fragments of faded memories.");
        Scene.slowPrint("A laugh echoes through the forest.");
        Scene.slowPrint("Something huge steps out of the shadows.\n");
        Scene.slowPrint(enemies.get(2).getName() + ": So, you were the one who has defeated my weaklings.");
        if(!Scene.combat(player, enemies.get(2), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            System.out.println("Your stats: " + player.toString());
            return;
        }
        Scene.slowPrint(enemies.get(2).getName() + ": So... you are still alive.");
        Scene.slowPrint(enemies.get(2).getName() + ": The Goblin Lord said your memories would never return.");
        Scene.slowPrint(enemies.get(2).getName() + ": But maybe... he was afraid of you.");
        Scene.slowPrint(enemies.get(2).getName() + " drops a strange card glowing with pale light.\n");
        player.setHealth(health);
        Scene.slowPrint("Your stats are restored.");
        System.out.println(player.toString());
        player.resetDeck();

        Scene.shop(player);

        // ENCOUNTER 4: THIEF
        Scene.slowPrint("----Chapter IV: WHERE IS MY CARD?----\n");
        Scene.slowPrint("As you inspect the glowing card, you feel something missing.");
        Scene.slowPrint("One of your cards is gone.");
        Scene.slowPrint("You hear tiny footsteps circling around you.");
        Scene.slowPrint("A small goblin jumps between the trees, laughing.");
        Scene.slowPrint("In his hands... your stolen card.\n");
        if(!Scene.combat(player, enemies.get(3), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            System.out.println("Your stats: " + player.toString());
            return;
        }
        Scene.slowPrint(enemies.get(3).getName() + ": WAIT WAIT WAIT!");
        Scene.slowPrint(enemies.get(3).getName() + ": I only steal things because they stole from me first!");
        Scene.slowPrint(enemies.get(3).getName() + ": The Goblin Lord keeps everyone's memories in the castle underground.");
        Scene.slowPrint(enemies.get(3).getName() + " disappears into the darkness before you can ask anything else.\n");

        Scene.shop(player);

        // ENCOUNTER 5: FINAL BOSS
        Scene.slowPrint("----Chapter V: MEMORIES----\n");
        Scene.slowPrint("At the end of the forest stands a massive stone gate.");
        Scene.slowPrint("The air itself feels heavy.");
        Scene.slowPrint("As you step forward, forgotten memories begin flashing before your eyes.");
        Scene.slowPrint("A home.");
        Scene.slowPrint("A fire.");
        Scene.slowPrint("A scream.");
        Scene.slowPrint("The gates slowly open.");
        Scene.slowPrint("Someone is waiting for you inside.\n");

        if(!Scene.combat(player, enemies.get(4), input)){
            System.out.println("=============");
            System.out.println("  GAME OVER  ");
            System.out.println("=============");
            System.out.println("Your stats: " + player.toString());
            return;
        }
        Scene.slowPrint(enemies.get(4).getName() + ": Impossible...");
        Scene.slowPrint(enemies.get(4).getName() + ": No human was supposed to survive the forgetting...");
        Scene.slowPrint("The Goblin Lord falls to the ground as thousands of glowing memories burst into the air.");
        Scene.slowPrint("You finally remember what was stolen from you.");
        Scene.slowPrint("It was never just a memory.");
        Scene.slowPrint("It was your family.");
        Scene.slowPrint("For all these years you've keeping forgetting about your friends, your comrades and your family");
        Scene.slowPrint("That's why you invented The Cards");
        Scene.slowPrint("Cards are made out of memory fragrances about of people, from the mind of the person who utilises their power.");
        Scene.slowPrint("You wanted to forget the fact you invented this cursed power.");
        Scene.slowPrint("That's the reason you created the most powerful card that ever existed.");
        Scene.slowPrint("Card of the Truth");
        Scene.slowPrint("The goblin inside your house earlier, was trying to make you remember about this all.");
        Scene.slowPrint("That's why he made you follow him.");
        Scene.slowPrint("You now remember it all.");
        Scene.slowPrint("Simon and Sarah, archer siblings... They always used to convince you to take a archery course");
        Scene.slowPrint("Pristine and Rose... They were the ones who cheered you up when you were afraid of the dark.");
        Scene.slowPrint("And finally...");
        Scene.slowPrint("Jake...");
        Scene.slowPrint("He was your best friend... That's the card that goblin stole from you.");
        Scene.slowPrint("Because he knew you would come for him.");
        Scene.slowPrint("Just as you remember, you fall on your knees and start to cry.");
        Scene.slowPrint("Just then you hear a voice...");
        Scene.slowPrint("Ofpj: Bj fwj mjwj. It sty lnaj zu. Bj fwj bfnynsl ktw dtz yt xfaj zx rd tqi kwnjsi."); //Caesar 5
        Scene.slowPrint("\nTHE END");
        Scene.slowPrint("OR IS IT?\n");

        System.out.println("=============");
        System.out.println("  YOU WON  ");
        System.out.println("=============");

        int score = player.getGem();
        for (Card c : player.getCardList()){
            if (c instanceof SpellCard sc){
                score += sc.getPrice() / 2;
            }
        }
        for (Emote e: player.getEmoteList()){
            score += e.getPrice() / 2;
        }
        System.out.println("Your score: " + score);
        System.out.println("Your stats: " + player.toString());
        Scene.slowPrint("Thanks For Playing!");
        input.close();
    }
}