import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class Main {
    private static String lol;
    private static String urlToRead = "http://asymcryptwebservice.appspot.com/znp/serverKey";
    private static String urlToReadroot = "http://asymcryptwebservice.appspot.com/znp/challenge";

    public static void main(String[] args) throws Exception {
        int step = 0;
        BigInteger n, t, y, z;

        while(true) {
            step++;
            n = new BigInteger(getKey(urlToRead), 16);
            System.out.println("Key(n):\n" + n);

            t = getRandomBigInteger(n);
            System.out.println("Random(t):\n" + t);

            y = t.multiply(t).mod(n);
            String temp = y.toString(16);
            System.out.println("y:\n" + y);

            z = new BigInteger(getY(urlToReadroot, temp), 16);
            System.out.println("z:\n" + z + "\n");

            if (!t.equals(z) && !t.equals(n.subtract(z))) {
                System.out.println("Step:" + step + "\n");
                System.out.println("p:\n" + n.gcd(t.add(z)) + "\nq:\n" + n.divide(n.gcd(t.add(z))) + "\n");
                break;
            }
        }

    }

    public static String getKey(String urlToRead) throws Exception {

        URL url = new URL(urlToRead);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        lol = conn.getHeaderFields().get("Set-Cookie").get(0);
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString().substring(12, result.toString().length()-2);
    }

    public static String getY(String urlToRead, String y) throws Exception {

        String newUrl = urlToRead + "?y=" + y;
        URL url = new URL(newUrl);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Cookie", lol);
        conn.setRequestMethod("GET");

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line;
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        rd.close();
        conn.disconnect();

        return result.toString().substring(9, result.toString().length()-2);
    }

    public static BigInteger getRandomBigInteger(BigInteger n) {
        Random rand = new Random();
        BigInteger res;
        do {
            res = new BigInteger(n.bitLength(), rand);
        } while (res.max(n).equals(res));
        return res;
    }
}


