package oreschnix.cryptosatistics.network;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import oreschnix.cryptosatistics.network.interfaces.DataProvider;
import oreschnix.cryptosatistics.network.interfaces.DataProviderListener;

/**
 * Created by miha.novak on 24/02/2018.
 */

abstract class BaseDataProvider implements DataProvider {

    @Override
    public void getCryptocurrencyInfo(Context context,
                                      Constants.Currency cryptocurrencyId,
                                      DataProviderListener dataProviderListener) {
        List cryptocurrencyIdList = new ArrayList();
        cryptocurrencyIdList.add(cryptocurrencyId);
        executeRequest(context, cryptocurrencyIdList, dataProviderListener);
    }

    @Override
    public void getCryptocurrencyInfoList(Context context, List<Constants.Currency> cryptocurrencyIds,
                                          DataProviderListener dataProviderListener) {
        executeRequest(context, cryptocurrencyIds, dataProviderListener);
    }


    abstract void executeRequest(Context context,
                                 List<Constants.Currency> cryptocurrencyIds,
                                 DataProviderListener dataProviderListener);
}
