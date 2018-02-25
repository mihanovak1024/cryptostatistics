package oreschnix.cryptosatistics.network;

import android.content.Context;
import android.os.Handler;
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
import oreschnix.cryptosatistics.model.GlobalMarketData;
import oreschnix.cryptosatistics.network.interfaces.CryptocurrencyProviderListener;
import oreschnix.cryptosatistics.network.interfaces.GlobalDataProviderListener;
import oreschnix.cryptosatistics.util.HandlerFactory;

/**
 * Created by miha.novak on 23/02/2018.
 */

public class HttpDataProvider extends BaseDataProvider {

    private Map<Constants.Currency, Cryptocurrency> mResponseMap;
    private Handler mHandler;
    private Handler mUiHandler;

    public HttpDataProvider(Handler uiHandler) {
        this.mUiHandler = uiHandler;
        mHandler = HandlerFactory.createHandler(this.getClass().getSimpleName());
    }

    /**
     * Loops through cryptocurrency id list and
     * makes http requests with {@link HttpURLConnection}
     * saving the responses to Cryptocurrency HashMap.
     *
     * @param context                        - application context
     * @param cryptocurrencyIds              - list of ids, for desired cryptocurrency data to be fetched
     * @param cryptocurrencyProviderListener - listener for the mResponseMap callback
     */
    @Override
    void executeRequest(Context context,
                        List<Constants.Currency> cryptocurrencyIds,
                        final CryptocurrencyProviderListener cryptocurrencyProviderListener) {
        mResponseMap = new HashMap<>();
        final String baseUrl = Constants.BASE_CRYPTO_URL;
        final Gson gson = new Gson();
        for (final Constants.Currency currency : cryptocurrencyIds) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection urlConnection = null;
                    try {
                        URL url = new URL(baseUrl + currency.getCurrencyID());
                        urlConnection = (HttpURLConnection) url.openConnection();
                        InputStream is = urlConnection.getInputStream();
                        JsonParser jsonParser = new JsonParser();
                        Cryptocurrency cryptocurrency = ((List<Cryptocurrency>) gson.fromJson(jsonParser.parse(new InputStreamReader(is)),
                                new TypeToken<List<Cryptocurrency>>() {
                                }.getType())).get(0);
                        Constants.Currency currencyElement = Constants.Currency.forValue(cryptocurrency.getId());
                        mResponseMap.put(currencyElement, cryptocurrency);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (cryptocurrencyProviderListener != null) {
                                    cryptocurrencyProviderListener.onFail("response exception");
                                }
                            }
                        });
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }
                }
            });
        }
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d("onResponse", "fetching done, callback");
                if (cryptocurrencyProviderListener != null) {
                    cryptocurrencyProviderListener.onReceive(mResponseMap);
                } else {
                    Log.d("onResponse", "listenerNull");
                }
            }
        });
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
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(Constants.MARKET_DATA_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream is = urlConnection.getInputStream();
                    JsonParser jsonParser = new JsonParser();
                    final GlobalMarketData globalMarketData = gson.fromJson(jsonParser.parse(new InputStreamReader(is)), new TypeToken<GlobalMarketData>() {
                    }.getType());
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("onResponse", "fetching done, callback");
                            if (globalDataProviderListener != null) {
                                globalDataProviderListener.onReceive(globalMarketData);
                            } else {
                                Log.d("onResponse", "listenerNull");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    mUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (globalDataProviderListener != null) {
                                globalDataProviderListener.onFail("response exception");
                            }
                        }
                    });
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });
    }
}
