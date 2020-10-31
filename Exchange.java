import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Exchange {

    private static int idCounter = 0;
    private final Map<String, Integer> priceMap;
    private final PrintWriter to_exchange;

    public Exchange(PrintWriter to_exchange){
        this.to_exchange = to_exchange;
        priceMap = new HashMap<>(7);
        priceMap.put("BOND", 1000);
    }

    public void addBuy(String symb, int price, int size) {
        idCounter += 1;
        System.out.println(String.format("ADD %d %s BUY %d %d\n", idCounter, symb, price, size));
        to_exchange.println(String.format("ADD %d %s BUY %d %d", idCounter, symb, price, size));
    }

    public void addSell(String symb, int price, int size) {
        idCounter += 1;
        System.out.println(String.format("ADD %d %s SELL %d %d\n", idCounter, symb, price, size));
        to_exchange.println(String.format("ADD %d %s SELL %d %d", idCounter, symb, price, size));
    }

    public boolean parse(String[] message) {
        if (message[0].equals("BOOK")){
            // ignore
        } else if (message[0].equals("TRADE")) {
            String symbol = message[1];
            if (symbol.equals("BOND")) {
                return true;
            }
            int price = Integer.parseInt(message[2]);
            // int size = Integer.parseInt(message[3]);
            priceMap.put(symbol, price);
            System.out.printf("Updated %s to %d%n", symbol, price);
        } else if (message[0].equals("FILL")) {
            System.out.println(Arrays.deepToString(message));
            String symbol = message[2];
            if (message[3].equals("BUY")) {
                addSell(symbol, priceMap.get(symbol) - 1, 1);
            } else {
                addBuy(symbol, priceMap.get(symbol) + 1, 1);
            }
        } else if (message[0].equals("REJECT")) {
            idCounter -= 1;
        } else if (message[0].equals("CLOSE")) {
            System.out.println("Market closed");
            return false;
        } else if (message[0].equals("HELLO")) {
            System.out.println(Arrays.deepToString(message));
        }
        return true;
    }


}
