package oreschnix.cryptosatistics.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.model.GlobalMarketData;
import oreschnix.cryptosatistics.network.interfaces.CryptocurrencyProviderListener;
import oreschnix.cryptosatistics.network.interfaces.GlobalDataProviderListener;

/**
 * Created by miha.novak on 23/02/2018.
 */

public class VolleyDataProvider extends BaseDataProvider implements Response.Listener<String>, Response.ErrorListener {

    private Map<Constants.Currency, Cryptocurrency> responseMap;
    private CryptocurrencyProviderListener cryptocurrencyProviderListener;
    private int requestNumber = 0;

    public VolleyDataProvider() {
    }

    /**
     * Loops through cryptocurrency id list and
     * makes requests with {@link RequestQueue}.
     *
     * @param context                        - application context
     * @param cryptocurrencyIds              - list of ids, for desired cryptocurrency data to be fetched
     * @param cryptocurrencyProviderListener - listener for the responseMap callback
     */
    @Override
    void executeRequest(Context context,
                        List<Constants.Currency> cryptocurrencyIds,
                        CryptocurrencyProviderListener cryptocurrencyProviderListener) {
        // Setup
        this.cryptocurrencyProviderListener = cryptocurrencyProviderListener;
        String baseUrl = Constants.BASE_CRYPTO_URL;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        responseMap = new HashMap<>();

        // Currency request loop
        for (Constants.Currency cryptocurrency : cryptocurrencyIds) {
            String fullUrl = baseUrl + cryptocurrency.getCurrencyID();
            StringRequest stringRequest = new StringRequest(fullUrl, this, this);
            requestNumber++;
            requestQueue.add(stringRequest);
        }
    }

    /**
     * Volley's error callback.
     * The onFail() call is sent via the {@link CryptocurrencyProviderListener}.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Log.d("onErrorResponse", "error");
        if (cryptocurrencyProviderListener != null) {
            cryptocurrencyProviderListener.onFail("onErrorResponse");
        }
    }

    /**
     * Volley's success callback.
     * The response is transformed into a {@link Cryptocurrency} object
     * and added into {@link HashMap}.
     * When the requestNumber reaches 0, we reached the final response,
     * thus send the onReceive(responseMap) call via the {@link CryptocurrencyProviderListener}.
     *
     * @param response
     */
    @Override
    public void onResponse(String response) {
        requestNumber--;
        Log.d("onResponse", "requestNumber = " + requestNumber);
        Log.d("onResponse", "response = " + response);
        Gson gson = new Gson();
        Cryptocurrency cryptocurrency = ((ArrayList<Cryptocurrency>) gson.fromJson(response, new TypeToken<List<Cryptocurrency>>() {
        }.getType())).get(0);
        Constants.Currency currency = Constants.Currency.forValue(cryptocurrency.getId());
        Log.d("onResponse", "currency = " + currency.name() + "-" + currency.toString());
        responseMap.put(currency, cryptocurrency);
        if (requestNumber == 0) {
            Log.d("onResponse", "fetching done, callback");
            if (cryptocurrencyProviderListener != null) {
                cryptocurrencyProviderListener.onReceive(responseMap);
            } else {
                Log.d("onResponse", "listenerNull");
            }
        }
    }

    /**
     * Requests global market data from the server
     * and returns the fail or success callback via {@link GlobalDataProviderListener}.
     *
     * @param context
     * @param globalDataProviderListener
     */
    @Override
    public void getGlobalInfo(Context context, final GlobalDataProviderListener globalDataProviderListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Constants.MARKET_DATA_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                GlobalMarketData globalMarketData = gson.fromJson(response, new TypeToken<GlobalMarketData>() {
                }.getType());
                Log.d("onResponse", "fetching done, callback");
                if (globalDataProviderListener != null) {
                    globalDataProviderListener.onReceive(globalMarketData);
                } else {
                    Log.d("onResponse", "listenerNull");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("onErrorResponse", "error");
                if (globalDataProviderListener != null) {
                    globalDataProviderListener.onFail("onErrorResponse");
                } else {
                    Log.d("onErrorResponse", "listenerNull");
                }
            }
        });
        requestQueue.add(stringRequest);
    }
}
