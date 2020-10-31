public class Gs implements Symbol{

    public int price = 0;
    public boolean first = false;
    public int held = 0;
    private int LIMIT = 100;
    public int[] lastTwenty = new int[20];
    public int positionOfLastAdd = 0;

    public Gs(int price){
        this.price = price;
    }

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
        return new String[0];
    }

    @Override
    public String[] genSell() {
        return new String[0];
    }
}
