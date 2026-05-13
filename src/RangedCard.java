public class RangedCard extends TroopCard {
    private int accuracy;

    public RangedCard(){}
    public RangedCard(String cardName, int health, int speed, int accuracy){
        super(cardName, health, speed);
        this.accuracy = accuracy;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public int playCard() throws UnavailableCardException{
        if(!super.isAvailable()){
            throw new UnavailableCardException("This card has been already played!");
        }
        else{
            //burayi sonra yapak
            return 0;
        }
    }

    @Override
    public String toString(){
        return String.format("[Ranged Card: %s, Health: %d, Speed: %d, Accuracy: %d, Availability: %b]", super.getCardName()
                ,super.getHealth(), super.getSpeed(), this.getAccuracy(), super.isAvailable());
    }

    @Override
    public boolean equals(TroopCard target){

    }
}
