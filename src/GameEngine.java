import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameEngine {
    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    public void shop(Player target){
        System.out.println("Welcome to the Royal Market!");
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
            scanner.nextLine();
            choice = scanner.nextInt();
            switch (choice)
            {
                case 0:
                    System.out.println("Good Bye!");
                    break;

                case 1:
                    System.out.println("Available Emotes:");
                    for (int i = 0; i < emotes.size(); i++){
                        System.out.printf("%d) %s",i , emotes.get(i).toString());
                    }
                    System.out.print("Which one do you like: ");
                    scanner.nextLine();
                    choice = scanner.nextInt();
                    target.addEmote(emotes.get(choice));
                    target.setGem(target.getGem() - emotes.get(choice).getPrice());
                    System.out.println("You bought: " +  emotes.get(choice).getEmoteMessage() +
                            " for: " + emotes.get(choice).getPrice());
                    choice = 1;
                    break;

                case 2:
                    System.out.println("Available Spell Cards:");
                    for (int i = 0; i < cards.size(); i++){
                        System.out.printf("%d) %s",i , cards.get(i).toString());
                    }
                    System.out.print("Which one do you like: ");
                    scanner.nextLine();
                    choice = scanner.nextInt();
                    target.addCard(cards.get(choice));
                    target.setGem(target.getGem() - ((SpellCard)cards.get(choice)).getPrice());
                    System.out.println("You bought: " +  cards.get(choice).getCardName() +
                            " for: " + ((SpellCard)cards.get(choice)).getPrice());
                    choice = 1;
                    break;

                default:
                    System.out.println("Unknown choice. Please choose again.");
                    choice = 1;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the Game Engine");
    }
}
