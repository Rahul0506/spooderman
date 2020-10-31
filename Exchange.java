import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Exchange {

    private static int idCounter = 0;
    private final Map<String, Integer> priceMap;

    private final Map<String, Boolean> firstMap;

    private final Map<String, Symbol> symbolMap;
    private final PrintWriter to_exchange;

    private int[] valbzPrice = new int[20];
    private int valbzIndex = 0;
    private int valbzAve = 0;

    private int[] valePrice = new int[5];
    private int valeIndex = 0;
    private int valeAve = 0;
    private int valHeld = 0;

    private int[] etfBasket = new int[3];
    private int etfEstimate = 300;
    private int etfHeld = 0;

    private int[] msTwentyAverage = new int[20];
    private int[] msFiveAverage = new int[5];
    private int msTwentyIndex = 0;
    private int msFiveIndex = 0;
    private int msHeld = 0;
    private int msavg = 0;
    private int msSmallAvg = 0;

    public Exchange(PrintWriter to_exchange){
        this.to_exchange = to_exchange;
        symbolMap = new HashMap<>(7);
        symbolMap.put("BOND", new Bond());

        priceMap = new HashMap<>(7);
        priceMap.put("BOND", 1000);
        firstMap = new HashMap<>(7);
        firstMap.put("BOND", true);
        firstMap.put("VALBZ", false);
        firstMap.put("VALE", false);
        firstMap.put("GS", false);
        firstMap.put("MS", false);
        firstMap.put("WFC", false);
        firstMap.put("XLF", false);
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
            int price = Integer.parseInt(message[2]);

            // Update averages - arbitrage
            if (symbol.equals("VALBZ")) {
                valbzAve -= valbzPrice[valbzIndex] / 20;
                valbzAve += price / 20;
                valbzPrice[valbzIndex] = price;
                valbzIndex = (valbzIndex + 1) % 20;
            } else if (symbol.equals("VALE")) {
                valeAve -= valePrice[valeIndex] / 5;
                valeAve += price / 5;
                valePrice[valeIndex] = price;
                valeIndex = (valeIndex + 1) % 5;
            }

            // Update basket
            else if (symbol.equals("GS")) {
                etfEstimate -= etfBasket[0] * 2 / 10;
                etfEstimate += price * 2 / 10;
                etfBasket[0] = price;
            } else if (symbol.equals("MS")) {
                etfEstimate -= etfBasket[1] * 3 / 10;
                etfEstimate += price * 3 / 10;
                etfBasket[1] = price;

                msavg -= msTwentyAverage[msTwentyIndex] / 20;
                msSmallAvg -= msFiveAverage[msFiveIndex] / 5;
                msavg += price / 20;
                msSmallAvg += price / 5;
                msTwentyAverage[msTwentyIndex] = price;
                msFiveAverage[msFiveIndex] = price;
                msFiveIndex = (msFiveIndex + 1) % 5;
                msTwentyIndex = (msTwentyIndex + 1) % 20;

                if(msavg - msSmallAvg > 5){
                    addBuy(symbol, price,1);
                } else if (msSmallAvg - msavg > 3){
                    if(msHeld > 0) {
                        addSell(symbol, price, msHeld);
                    }
                }


            } else if (symbol.equals("WFC")) {
                etfEstimate -= etfBasket[2] * 2 / 10;
                etfEstimate += price * 2 / 10;
                etfBasket[2] = price;
            }

            // Check buy/sell
            if (symbol.equals("VALE") || symbol.equals("VALBZ")) {
                if (valbzAve - valeAve > 5) {   // buy vale
                    addBuy("VALE", Math.round(valeAve), 1);
                } else if (valeAve - valbzAve > 3) {    // sell vale
                    if (valHeld > 0) {
                        addSell("VALE", Math.round(valeAve), valHeld);
                    }
                }
            }
            // ETF
            else if (symbol.equals("XLF")) {
                if (etfEstimate - price > 8 && etfHeld < 50) {
                    addBuy("XLF", price, 1);
                } else if (price - etfEstimate > 3) {
                    if (etfHeld > 0) {
                        addSell("XLF", price, etfHeld);
                    }
                }
            }

        } else if (message[0].equals("FILL")) {

            System.out.println(Arrays.deepToString(message));
            String symbol = message[2];

            if (message[3].equals("BUY")) {
                if (symbol.equals("VALE")) {
                    valHeld += Integer.parseInt(message[5]);
                } else if (symbol.equals("XLF")) {
                    etfHeld += Integer.parseInt(message[5]);
                } else if (symbol.equals("BOND")) {
                    addSell(symbol, 1001, 1);
                } else if (symbol.equals("MS")) {
                    msHeld += Integer.parseInt(message[5]);
                }
            } else {
                if (symbol.equals("VALE")) {
                    valHeld -= Integer.parseInt(message[5]);
                } else if (symbol.equals("XLF")) {
                    etfHeld -= Integer.parseInt(message[5]);
                } else if (symbol.equals("BOND")) {
                    addBuy(symbol, 999, 1);
                } else if (symbol.equals("MS")){
                    msHeld -= Integer.parseInt(message[5]);
                }
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
