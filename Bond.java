public class Bond implements Symbol {
    public int price;
    public boolean first;

    public Bond() {
        price = 0;
        first = false;
    }

    @Override
    public void reset() {
        price = 0;
        first = false;
    }

    @Override
    public void update(int price) {

    }
}
