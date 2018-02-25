package oreschnix.cryptosatistics.network;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import oreschnix.cryptosatistics.network.interfaces.CryptocurrencyProviderListener;
import oreschnix.cryptosatistics.network.interfaces.DataProvider;
import oreschnix.cryptosatistics.network.interfaces.GlobalDataProviderListener;

/**
 * Created by miha.novak on 24/02/2018.
 */

abstract class BaseDataProvider implements DataProvider {

    @Override
    public void getCryptocurrencyInfo(Context context,
                                      Constants.Currency cryptocurrencyId,
                                      CryptocurrencyProviderListener cryptocurrencyProviderListener) {
        List cryptocurrencyIdList = new ArrayList();
        cryptocurrencyIdList.add(cryptocurrencyId);
        executeRequest(context, cryptocurrencyIdList, cryptocurrencyProviderListener);
    }

    @Override
    public void getCryptocurrencyInfoList(Context context, List<Constants.Currency> cryptocurrencyIds,
                                          CryptocurrencyProviderListener cryptocurrencyProviderListener) {
        executeRequest(context, cryptocurrencyIds, cryptocurrencyProviderListener);
    }


    abstract void executeRequest(Context context,
                                 List<Constants.Currency> cryptocurrencyIds,
                                 CryptocurrencyProviderListener cryptocurrencyProviderListener);

    @Override
    public abstract void getGlobalInfo(Context context, GlobalDataProviderListener globalDataProviderListener);
}
