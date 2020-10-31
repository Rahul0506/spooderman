public class Bond implements Symbol {
    public int price = 1000;
    public boolean first = false;
    public int held = 0;

    public Bond() {}

    @Override
    public void reset() {
        price = 0;
        first = false;
        held = 0;
    }

    @Override
    public boolean update(int price) {
        return true;
    }

    @Override
    public boolean favourBuy() {
        return false;
    }

    @Override
    public boolean favourSell() {
        return false;
    }
}
