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
import oreschnix.cryptosatistics.network.interfaces.DataProviderListener;

/**
 * Created by miha.novak on 23/02/2018.
 */

public class VolleyDataProvider extends BaseDataProvider implements Response.Listener<String>, Response.ErrorListener {

    private Map<Constants.Currency, Cryptocurrency> responseMap;
    private DataProviderListener dataProviderListener;
    private int requestNumber = 0;

    public VolleyDataProvider() {
    }

    /**
     * Loops through cryptocurrency id list and
     * makes requests with {@link RequestQueue}.
     *
     * @param context              - application context
     * @param cryptocurrencyIds    - list of ids, for desired cryptocurrency data to be fetched
     * @param dataProviderListener - listener for the responseMap callback
     */
    @Override
    void executeRequest(Context context,
                        List<Constants.Currency> cryptocurrencyIds,
                        DataProviderListener dataProviderListener) {
        // Setup
        this.dataProviderListener = dataProviderListener;
        String baseUrl = Constants.BASE_URL;
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
     * The onFail() call is sent via the {@link DataProviderListener}.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        Log.d("onErrorResponse", "error");
        if (dataProviderListener != null) {
            dataProviderListener.onFail("onErrorResponse");
        }
    }

    /**
     * Volley's success callback.
     * The response is transformed into a {@link Cryptocurrency} object
     * and added into {@link HashMap}.
     * When the requestNumber reaches 0, we reached the final response,
     * thus send the onReceive(responseMap) call via the {@link DataProviderListener}.
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
            if (dataProviderListener != null) {
                dataProviderListener.onReceive(responseMap);
            } else {
                Log.d("onResponse", "listenerNull");
            }
        }
    }
}
