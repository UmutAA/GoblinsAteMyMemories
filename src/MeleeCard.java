public class MeleeCard extends TroopCard{
    private int power;

    public MeleeCard(){}
    public MeleeCard(String cardName, int health, int speed, int power){
        super(cardName,health,speed);
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public int playCard() throws UnavailableCardException {
        if(!super.isAvailable()){
            throw new UnavailableCardException("This card has been already played!");
        }
        else{
            //burayi sonra yapak
        }
    }

    @Override
    public String toString(){
        return String.format("[Melee Card: %s, Health: %d, Speed: %d, Power: %d, Availability: %b]", super.getCardName()
                ,super.getHealth(), super.getSpeed(), this.getPower(), super.isAvailable());
    }

    @Override
    public boolean equals(TroopCard target){

    }
}
