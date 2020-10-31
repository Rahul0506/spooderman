import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Exchange {

    private static int idCounter = 0;
    private Map<String, Integer> priceMap;

    public Exchange(){
        priceMap = new HashMap<>(7);
    }

    public String addBuy(String symb, int price, int size) {
        idCounter += 1;
        return String.format("ADD %d %s BUY %d %d", idCounter, symb, price, size);
    }

    public String addSell(String symb, int price, int size) {
        idCounter += 1;
        return String.format("ADD %d %s SELL %d %d", idCounter, symb, price, size);
    }

    public String buyBOND(int price, int size) {
        idCounter += 1;
        return String.format("ADD %d BOND BUY %d %d", idCounter, price, size);
    }

    public String sellBOND(int price, int size) {
        idCounter += 1;
        return String.format("ADD %d BOND SELL %d %d", idCounter, price, size);
    }

    public boolean parse(String[] message) {
        if (message[0].equals("BOOK")){
            // ignore
        } else if (message[0].equals("TRADE")) {
            String symbol = message[1];
            int price = Integer.parseInt(message[2]);
            // int size = Integer.parseInt(message[3]);
            priceMap.put(symbol, price);
            System.out.println(String.format("Updated %s to %d", symbol, price));
        } else if (message[0].equals("FILL")) {
            System.out.println(Arrays.deepToString(message));
        } else if (message[0].equals("REJECT")) {
            idCounter -= 1;
        } else if (message[0].equals("CLOSE")) {
            System.out.println("Market closed");
            return false;
        }
        return true;
    }


}
