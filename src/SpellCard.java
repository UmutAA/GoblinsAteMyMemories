public class SpellCard extends Card implements Buyable{
    private int damage;
    private int price;

    public SpellCard(){}

    public SpellCard(String cardName, int price, int damage){
        super(cardName);
        this.price = price;
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return String.format("[Spell: %s, Price: %d, Damage: %d, Availability: %b]", super.getCardName()
                ,this.getPrice(), this.getDamage(), super.isAvailable());
    }

    /**
     * Calculates and returns a raw damage.
     * @return Raw damage of the played card.
     * @exception UnavailableCardException thrown if this card is unavailable
     */
    @Override
    public int playCard() throws UnavailableCardException{
        if(!super.isAvailable()){
            throw new UnavailableCardException("This card has been already played!");
        }
        else{
            return this.getDamage();
        }
    }
    /**
     * Checks and confirms whether the player can buy this object. If so, adds this Spell Card to target's deck.
     * @param target player to whom this object will be given
     */
    @Override
    public void buy(Player target) throws InsufficientMoneyException, DuplicateException {
        if(target.getGem() < this.getPrice()){
            throw new InsufficientMoneyException("To purchase this spell, you need to have "
                +  (this.getPrice() - target.getGem()) + " more gems!");
        }
        else if(target.getCardList().contains(this)){
            throw new DuplicateException("You already own this card!");
        }
        else{
            System.out.println("Buying Spell...");
        }
    }

    public boolean equals(Object obj){
        if (obj instanceof SpellCard card)
        {
            if (this == obj)
            {
                return true;
            }

            else
            {
                return (getCardName().equals(card.getCardName()) && getDamage() == card.getDamage());
            }
        }
        else{
            return false;
        }
    }
}
