public class Exchange {
    private static int idCounter = 0;

    public static String buyBOND() {
        idCounter += 1;
        return String.format("ADD %d BOND BUY 999 100", idCounter);
    }

    public static String sellBOND() {
        idCounter += 1;
        return String.format("ADD %d BOND SELL 1000 100", idCounter);
    }
}
