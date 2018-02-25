package oreschnix.cryptosatistics.network.interfaces;

import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.network.Constants;

/**
 * Created by miha.novak on 24/02/2018.
 */

public interface DataProviderListener {

    /**
     * Data is received success callback.
     *
     * @param cryptocurrencyMap - list of the cryptocurrency data specified in request
     */
    void onReceive(Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap);

    /**
     * Data receive fail callback.
     *
     * @param errorMessage - short description of error
     */
    void onFail(String errorMessage);

}
