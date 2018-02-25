package oreschnix.cryptosatistics.network.interfaces;

import oreschnix.cryptosatistics.model.GlobalMarketData;

/**
 * Created by miha.novak on 25/02/2018.
 */

public interface GlobalDataProviderListener extends BaseNetworkListener {


    /**
     * Data is received success callback.
     *
     * @param globalMarketData - info about global cryptocurrency market
     */
    void onReceive(GlobalMarketData globalMarketData);

}
