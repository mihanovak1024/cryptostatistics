package oreschnix.cryptosatistics.network.interfaces;

import android.content.Context;

import java.util.List;

import oreschnix.cryptosatistics.network.Constants;

/**
 * Created by miha.novak on 23/02/2018.
 */

/**
 * Interface for fetching cryptocurrency data.
 */
public interface DataProvider {

    /**
     * Executes the request for a single cryptocurrency data
     *
     * @param context
     * @param cryptocurrencyID     - "BTC", "ETH",...
     * @param dataProviderListener - listener for data received callback
     */
    void getCryptocurrencyInfo(Context context,
                               Constants.Currency cryptocurrencyID,
                               DataProviderListener dataProviderListener);

    /**
     * Executes the request for the list of cryptocurrency data
     * for specific cryptocurrencies
     *
     * @param context
     * @param cryptocurrencyIDs    - "BTC", "ETH",...
     * @param dataProviderListener - listener for data received callback
     */
    void getCryptocurrencyInfoList(Context context,
                                   List<Constants.Currency> cryptocurrencyIDs,
                                   DataProviderListener dataProviderListener);

}
