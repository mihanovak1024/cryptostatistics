package oreschnix.cryptosatistics.operations;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.model.GlobalMarketData;
import oreschnix.cryptosatistics.network.Constants;
import oreschnix.cryptosatistics.network.interfaces.CryptocurrencyProviderListener;
import oreschnix.cryptosatistics.network.interfaces.DataProvider;
import oreschnix.cryptosatistics.network.interfaces.GlobalDataProviderListener;
import oreschnix.cryptosatistics.operations.listeners.OperationsCallback;
import oreschnix.cryptosatistics.util.PairCheck;

import static oreschnix.cryptosatistics.network.Constants.Currency.BTC;
import static oreschnix.cryptosatistics.network.Constants.Currency.ETH;
import static oreschnix.cryptosatistics.network.Constants.Currency.LTC;

/**
 * Created by miha.novak on 25/02/2018.
 */

public class GlobalDataOperations implements CryptocurrencyProviderListener, GlobalDataProviderListener {

    private DataProvider mDataCurrencyProvider;
    private DataProvider mGlobalMarketDataProvider;
    private OperationsCallback mOperationsCallback;

    private Map<Constants.Currency, Cryptocurrency> mCryptocurrencyMap;
    private GlobalMarketData mGlobalMarketData;
    private PairCheck dataUpdatedCheck;

    public GlobalDataOperations(OperationsCallback operationsCallback, DataProvider globalMarketDataProvider, DataProvider dataCurrencyProvider) {
        this.mOperationsCallback = operationsCallback;
        this.mGlobalMarketDataProvider = globalMarketDataProvider;
        this.mDataCurrencyProvider = dataCurrencyProvider;
    }

    /**
     * Retrieves the latest BTC, ETH and LTC cryptocurrency and
     * global market data from network.
     *
     * @param context
     */
    public void updateData(Context context) {
        dataUpdatedCheck = new PairCheck();
        List<Constants.Currency> currencyList = new ArrayList<>();
        currencyList.add(BTC);
        currencyList.add(ETH);
        currencyList.add(LTC);
        mDataCurrencyProvider.getCryptocurrencyInfoList(context, currencyList, this);
        mGlobalMarketDataProvider.getGlobalInfo(context, this);
    }

    @UiThread
    @Override
    public void onReceive(Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap) {
        Log.d("onReceive", "CryptocurrencyProviderListener - done");
        this.mCryptocurrencyMap = cryptocurrencyMap;
        if (dataUpdatedCheck.setFirstAndCheck(true)) {
            mOperationsCallback.dataUpdated(cryptocurrencyMap, mGlobalMarketData);
        }
    }

    @UiThread
    @Override
    public void onFail(String errorMessage) {
        Log.d("onFail", "fail");
    }

    @UiThread
    @Override
    public void onReceive(GlobalMarketData globalMarketData) {
        Log.d("onReceive", "GlobalDataProviderListener - done");
        this.mGlobalMarketData = globalMarketData;
        if (dataUpdatedCheck.setSecondAndCheck(true)) {
            mOperationsCallback.dataUpdated(mCryptocurrencyMap, globalMarketData);
        }
    }
}
