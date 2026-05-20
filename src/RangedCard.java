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
            return (this.getAccuracy() * this.getSpeed() / 10);
        }
    }

    @Override
    public String toString(){
        return String.format("[Ranged Card: %s, Health: %d, Speed: %d, Accuracy: %d, Availability: %b]", super.getCardName()
                ,super.getHealth(), super.getSpeed(), this.getAccuracy(), super.isAvailable());
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof RangedCard card)
        {
            if (this == obj)
            {
                return true;
            }

            else
            {
                return (getCardName().equals(card.getCardName()) && getHealth() == card.getHealth()
                        && getSpeed() == card.getSpeed() && getAccuracy() == card.getAccuracy());
            }
        }
        else{
            return false;
        }
    }

    @Override
    public Card clone(){
        try{
            RangedCard clone = (RangedCard) super.clone();
            clone.setAccuracy(this.getAccuracy());
            clone.setHealth(this.getHealth());
            clone.setSpeed(this.getSpeed());
            clone.setCardName(this.getCardName());
            return clone;
        }

        catch(CloneNotSupportedException e){
            System.out.println("Card cannot be cloned");
            return null;
        }
    }
}
