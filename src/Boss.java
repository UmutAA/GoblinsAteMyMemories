import java.util.*;

public class Boss extends Enemy implements Talkative{
    private int healthCoef;
    private int powerCoef;
    private final Random gen = new Random();

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
            ArrayList<Card> temp = super.getCardList();
            Card tempCard = temp.get(gen.nextInt(temp.size()));
            Card tempCloned = null;
            boolean condition = true;
            while(condition){
                if(tempCard instanceof RangedCard){
                    tempCloned = ((RangedCard) tempCard).clone();
                    condition = false;
                }
                else if(tempCard instanceof MeleeCard){
                    tempCloned = ((MeleeCard) tempCard).clone();
                    condition = false;
                }
                else{
                    tempCard = temp.get(gen.nextInt(temp.size())); //spell card condition
                }
            }
            target.addCard(tempCloned);
        }
    }

    @Override
    public int attack() {
        int damage = 0;
        boolean played = false;
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
