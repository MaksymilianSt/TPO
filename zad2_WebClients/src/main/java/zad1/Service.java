/**
 * @author Stachnik Maksymilian S25304
 */

package zad1;


import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;


public class Service {
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final String OPEN_WEATHER_API_KEY = "12e0a8a553ff94fb20d32fadf998007e";
    private static final String OPEN_WEATHER_URL_FORMAT = "https://api.openweathermap.org/data/2.5/weather?q=%s,%s&APPID=%s";
    private static final String EXCHANGE_URL = "https://open.er-api.com/v6/latest/";
    private static final String NBP_URL_FORMAT = "http://api.nbp.pl/api/exchangerates/rates/%s/%s";
    private String country;

    public Service(String country) {
        this.country = country;
    }

    public String getWeather(String city) {
        String apiUrl = String.format(OPEN_WEATHER_URL_FORMAT, country, city, OPEN_WEATHER_API_KEY);

        return sendRequest(apiUrl)
                .map(Object::toString)
                .orElseThrow(() -> new FetchDataException("Could not receive weather data :((("));
    }

    public Double getRateFor(String currency) {
        Optional<JSONObject> response = sendRequest(EXCHANGE_URL + getCountryCurrencyCode());

        return response
                .map(resp -> resp.getJSONObject("rates").getDouble(currency))
                .orElseThrow(() -> new FetchDataException("Could not receive Rate data :((("));
    }

    public Double getNBPRate() {
        String countryCurrencyCode = getCountryCurrencyCode();
        if (countryCurrencyCode.equals("PLN")) {
            return 1d;
        }
        Double result = getNBP(countryCurrencyCode);
        return (1.0 / result);
    }

    private Double getNBP(String currencyCode) {
        for (char table = 'a'; table <= 'c'; table++) {
            String url = String.format(NBP_URL_FORMAT, table, currencyCode);
            Optional<JSONObject> jsonObject = sendRequest(url);
            if (jsonObject.isPresent()) {
                return jsonObject.get()
                        .getJSONArray("rates")
                        .getJSONObject(0)
                        .getDouble(table != 'c' ? "mid" : "bid");
            }
        }
        throw new FetchDataException("Could not receive NBP Rate data :(((");
    }

    private String getCountryCurrencyCode() {
        Locale locale = Locale.availableLocales()
                .filter(e -> country.equals(e.getDisplayCountry()))
                .findFirst()
                .orElseThrow();

        return Currency
                .getInstance(locale)
                .getCurrencyCode();
    }

    private static Optional<JSONObject> sendRequest(String source) {
        try {
            HttpRequest req = HttpRequest.newBuilder(URI.create(source)).build();
            HttpResponse<String> response = HTTP_CLIENT.send(req, HttpResponse.BodyHandlers.ofString());

            return Optional.of(
                    new JSONObject(response.body())
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
