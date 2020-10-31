public class Bond implements Symbol {
    public int price = 1000;
    public boolean first = false;
    public int held = 0;
    private int LIMIT = 100;

    public Bond() {}

    @Override
    public void reset() {
        price = 0;
        first = false;
        held = 0;
    }

    @Override
    public Trade update(int price) {
        if (favourBuy() && held < LIMIT) {
            return Trade.BUY;
        } else if (favourSell() && held > 0) {
            return Trade.SELL;
        }
        return Trade.NONE;
    }

    @Override
    public boolean favourBuy() {
        return held < 100;
    }

    @Override
    public boolean favourSell() {
        return held > 0;
    }

    @Override
    public String[] genBuy() {
        return new String[] { "999", "1" };
    }

    @Override
    public String[] genSell() {
        return new String[] { "1001", "1" };
    }
}
