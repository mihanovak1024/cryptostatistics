package oreschnix.cryptosatistics.operations.listeners;

import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.model.GlobalMarketData;
import oreschnix.cryptosatistics.network.Constants;

/**
 * Created by miha.novak on 25/02/2018.
 */

public interface OperationsCallback {

    /**
     * Notifies that the data has been updated (retrieved from the network).
     */
    void dataUpdated(Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap, GlobalMarketData globalMarketData);

}
