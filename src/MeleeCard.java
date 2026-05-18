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
            return this.getPower() * this.getSpeed() / 10;
        }
    }

    @Override
    public String toString(){
        return String.format("[Melee Card: %s, Health: %d, Speed: %d, Power: %d, Availability: %b]", super.getCardName()
                ,super.getHealth(), super.getSpeed(), this.getPower(), super.isAvailable());
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof MeleeCard card)
        {
            if (this == obj)
            {
                return true;
            }

            else
            {
                return (getCardName().equals(card.getCardName()) && getHealth() == card.getHealth()
                        && getSpeed() == card.getSpeed() && getPower() == card.getPower());
            }
        }
        else{
            return false;
        }
    }

    @Override
    public Card clone(){
        return new RangedCard(super.getCardName(),super.getHealth(),super.getSpeed(),this.getPower());
    }
}
