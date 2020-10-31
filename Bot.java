import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Bot {
    public static void main(String[] args) {
        Configuration config = new Configuration(false);
        try
        {
            Socket skt = new Socket(config.exchange_name(), config.port());
            BufferedReader from_exchange = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            PrintWriter to_exchange = new PrintWriter(skt.getOutputStream(), true);

            to_exchange.println(("HELLO " + config.team_name).toUpperCase());

            Exchange exchange = new Exchange(to_exchange);

            boolean keepGoing = true;
            boolean sentFirst = false;
            while (true) {
                String[] message = from_exchange.readLine().trim().split(" ");

                if (!sentFirst) {
                    exchange.addBuy("BOND", 1000, 1);
                    sentFirst = true;
                }
                keepGoing = exchange.parse(message);
                if (!keepGoing) {
                    to_exchange.println(("HELLO " + config.team_name).toUpperCase());
                    exchange = new Exchange(to_exchange);
                    Thread.sleep(1000);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }

    static class Configuration {
        String exchange_name;
        int    exchange_port;
        final Integer test_exchange_kind = 0;
        final String  team_name          = "SPOODERMAN";

        Configuration(Boolean test_mode) {
            if(!test_mode) {
                exchange_port = 20000;
                exchange_name = "production";
            } else {
                exchange_port = 20000 + test_exchange_kind;
                exchange_name = "test-exch-" + this.team_name;
            }
        }

        String  exchange_name() { return exchange_name; }
        Integer port()          { return exchange_port; }
    }
}