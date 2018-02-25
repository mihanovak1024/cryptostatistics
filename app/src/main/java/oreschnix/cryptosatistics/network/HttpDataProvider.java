package oreschnix.cryptosatistics.network;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.network.interfaces.DataProviderListener;

/**
 * Created by miha.novak on 23/02/2018.
 */

public class HttpDataProvider extends BaseDataProvider {

    private Map<Constants.Currency, Cryptocurrency> responseMap;

    public HttpDataProvider() {
    }

    /**
     * Loops through cryptocurrency id list and
     * makes http requests with {@link HttpURLConnection}
     * saving the responses to Cryptocurrency HashMap.
     *
     * @param context              - application context
     * @param cryptocurrencyIds    - list of ids, for desired cryptocurrency data to be fetched
     * @param dataProviderListener - listener for the responseMap callback
     */
    @Override
    void executeRequest(Context context,
                        List<Constants.Currency> cryptocurrencyIds,
                        DataProviderListener dataProviderListener) {
        responseMap = new HashMap<>();
        String baseUrl = Constants.BASE_URL;
        Gson gson = new Gson();
        for (Constants.Currency currency : cryptocurrencyIds) {
            try {
                URL url = new URL(baseUrl + currency.getCurrencyID());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();
                JsonParser jsonParser = new JsonParser();
                Cryptocurrency cryptocurrency = ((List<Cryptocurrency>) gson.fromJson(jsonParser.parse(new InputStreamReader(is)),
                        new TypeToken<List<Cryptocurrency>>() {
                        }.getType())).get(0);
                Constants.Currency currencyElement = Constants.Currency.forValue(cryptocurrency.getId());
                responseMap.put(currencyElement, cryptocurrency);
            } catch (Exception e) {
                e.printStackTrace();
                if (dataProviderListener != null) {
                    dataProviderListener.onFail("response exception");
                }
            }
        }
        Log.d("onResponse", "fetching done, callback");
        if (dataProviderListener != null) {
            dataProviderListener.onReceive(responseMap);
        } else {
            Log.d("onResponse", "listenerNull");
        }
    }
}
