public interface Symbol {

    void reset();

    Trade update(int price);

    boolean favourBuy();

    boolean favourSell();

    String[] genBuy();

    String[] genSell();
}
