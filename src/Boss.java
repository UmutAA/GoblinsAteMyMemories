import java.util.*;

public class Boss extends Enemy implements Talkative{
    private int healthCoef;
    private int powerCoef;

    public Boss(){}
    public Boss(String name, int health, int power, ArrayList<Card> cardList, ArrayList<String> quoteList,
                int healthCoef, int powerCoef){
        super(name, health * healthCoef, power, cardList, quoteList);
        this.healthCoef = healthCoef;
        this.powerCoef = powerCoef;
        for (Card c: super.getCardList()){
            if (c instanceof TroopCard tc){
                tc.setHealth(health * healthCoef);
            }
        }
    }

    public int getHealthCoef() {
        return this.healthCoef;
    }

    public int getPowerCoef() {
        return powerCoef;
    }

    public void getReward(Player target){
        if(super.getHealth() <= 0){
            Random gen = new Random();
            boolean condition = true;
            while(condition){
                Card tempCard = super.getCardList().get(gen.nextInt(super.getCardList().size()));
                if(tempCard instanceof Cloneable<?>){
                    target.addCard(tempCard);
                    condition = false;
                }
                else{
                    continue;
                }
            }
            target.setGem(target.getGem() + gen.nextInt(31) + 25);
        }
    }

    @Override
    public int attack() {
        int damage = 0;
        boolean played = false;
        Random gen = new Random();
        while (!played){
            try{
                int choice = gen.nextInt(getCardList().size());
                if (getCardList().get(choice) instanceof TroopCard tc){
                    if (tc.isAvailable()){
                        setCurrentCard(getCardList().get(choice));
                    }
                }
                else if (getCardList().get(choice) instanceof SpellCard sc) {
                    for (Card c :  this.getCardList()) {
                        if (c.isAvailable() && c instanceof TroopCard tc) {
                            setCurrentCard(tc);
                            break;
                        }
                    }
                }
                damage = super.getCardList().get(choice).playCard();
                System.out.println(super.getName() + " played " + super.getCardList().get(choice).getCardName());
                played = true;
            } catch (Exception e){
                continue;
            }
        }
        damage *= super.getPower();
        if (gen.nextBoolean()){
            damage *= this.getPowerCoef();
        }
        return damage;
    }

    @Override
    public String toString(){
        return String.format("[Boss: %s, Health: %d, Power: %d, HealthCoef: %d, PowerCoef: %d]", super.getName()
                ,super.getHealth(), super.getPower(), this.getHealthCoef(), this.getPowerCoef());
    }

    @Override
    public void talk(){
        Random gen = new Random();
        if (!quoteList.isEmpty()) {
            int index = gen.nextInt(quoteList.size());
            System.out.println(super.getName() + ": " + quoteList.get(index));
        }
    }
}
