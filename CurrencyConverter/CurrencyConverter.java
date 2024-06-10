import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CurrencyConverter {

    private static final String API_KEY = "f90fb9d149f7327cf2e4960f";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Currency Converter");

        // Currency Selection
        System.out.print("Enter base currency (e.g., USD, EUR): ");
        String baseCurrency = scanner.nextLine().toUpperCase();
        System.out.print("Enter target currency (e.g., USD, EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        // Amount Input
        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        // Fetch exchange rate and perform conversion
        double exchangeRate = fetchExchangeRate(baseCurrency, targetCurrency);
        if (exchangeRate != -1) {
            double convertedAmount = amount * exchangeRate;
            // Display Result
            System.out.println(amount + " " + baseCurrency + " = " + convertedAmount + " " + targetCurrency);
        } else {
            System.out.println("Failed to fetch exchange rate. Please try again later.");
        }

        scanner.close();
    }

    private static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        double exchangeRate = -1;
        try {
            String urlString = API_URL + API_KEY + "/latest/" + baseCurrency;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                exchangeRate = jsonResponse.getJSONObject("conversion_rates").getDouble(targetCurrency);
            } else {
                System.out.println("GET request not worked");
            }
        } catch (IOException | org.json.JSONException e) {
            e.printStackTrace();
        }

        return exchangeRate;
    }
}
