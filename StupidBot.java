import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class StupidBot {
    public static void main(String[] args)
    {
        /* The boolean passed to the Configuration constructor dictates whether or not the
           bot is connecting to the prod or test exchange. Be careful with this switch! */
        Configuration config = new Configuration(false);
        try
        {
            Socket skt = new Socket(config.exchange_name(), config.port());
            BufferedReader from_exchange = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            PrintWriter to_exchange = new PrintWriter(skt.getOutputStream(), true);

            /*
              A common mistake people make is to to_exchange.println() > 1
              time for every from_exchange.readLine() response.
              Since many write messages generate marketdata, this will cause an
              exponential explosion in pending messages. Please, don't do that!
            */
            to_exchange.println(("HELLO " + config.team_name).toUpperCase());
            String reply = from_exchange.readLine().trim();
            System.err.printf("The exchange replied: %s\n", reply);

            to_exchange.println(Exchange.buyBOND());
            boolean buy = false;
            while (true) {
                String[] message = from_exchange.readLine().trim().split(" ");

                System.out.println(Arrays.deepToString(message));
                if (message[0].equals("FILL")) {
                    if (buy) {
                        to_exchange.println(Exchange.buyBOND());
                    } else {
                        to_exchange.println(Exchange.sellBOND());
                    }
                }

                buy = !buy;

                if (message[0].equals("CLOSE")) {
                    System.out.println("The round has ended");
                    break;
                } else if (message[0].equals("REJECT")) {
                    System.out.println("Rejected");
                    break;
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
}

class Configuration {
    String exchange_name;
    int    exchange_port;
    /* 0 = prod-like
       1 = slow
       2 = empty
    */
    final Integer test_exchange_kind = 0;
    /* replace REPLACEME with your team name! */
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