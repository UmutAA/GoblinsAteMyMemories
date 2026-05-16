import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.Random;
import java.util.TimerTask;

public class Boss extends Enemy implements Talkative{
    private int healtCoef;
    private int powerCoef;
    private final Random gen = new Random();

    public Boss(){}
    public Boss(String name, int health, int power, ArrayList<Card> cardList, ArrayList<String> quoteList,
                int healthCoef, int powerCoef){
        super(name, health, power, cardList, quoteList);
        this.healtCoef = healthCoef;
        this.powerCoef = powerCoef;
    }

    public int getHealthCoef() {
        return this.healtCoef;
    }

    public int getPowerCoef() {
        return powerCoef;
    }

    public void getReward(Player target){
        //TODO
    }

    @Override
    public int attack(){
        //TODO
        return 0;
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
