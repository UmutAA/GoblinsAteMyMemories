public interface Buyable {
    public void buy(Player target) throws InsufficientMoneyException, DuplicateException;
    public int getPrice();
}
