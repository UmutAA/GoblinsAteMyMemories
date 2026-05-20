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
            target.setGem( target.getGem() + gen.nextInt(21) + 20);
        }
    }

    @Override
    public int attack() {
        int damage = 0;
        boolean played = false;
        Random gen = new Random();
        while (!played){
            int choice = gen.nextInt(getCardList().size());
            try{
                damage = super.getCardList().get(choice).playCard();
                played = true;
            }
            catch (UnavailableCardException e){
                continue;
            }

        }
        return (damage * super.getPower() * this.getPowerCoef());
    }

    @Override
    public String toString(){
        return String.format("[Boss: %s, Health: %d, Power: %d, HealthCoef: %d, PowerCoef: %d]", super.getName()
                ,super.getHealth(), super.getPower(), this.getHealthCoef(), this.getPowerCoef());
    }

    @Override
    public void talk(){
        Random gen = new Random();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (!quoteList.isEmpty()) {
                    int index = gen.nextInt(quoteList.size());
                    System.out.println(quoteList.get(index));
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 5000); // Every 5 seconds
    }
}
