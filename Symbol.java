public interface Symbol {

    void reset();

    boolean update(int price);

    boolean favourBuy();

    boolean favourSell();
}
