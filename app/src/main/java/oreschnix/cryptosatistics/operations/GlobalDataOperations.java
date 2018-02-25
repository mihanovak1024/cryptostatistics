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
import oreschnix.cryptosatistics.network.HttpDataProvider;
import oreschnix.cryptosatistics.network.VolleyDataProvider;
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

    public GlobalDataOperations(Handler uiHandler, OperationsCallback operationsCallback) {
        this.mOperationsCallback = operationsCallback;

        mDataCurrencyProvider = new VolleyDataProvider(); // or HttpDataProvider(uiHandler);
        mGlobalMarketDataProvider = new HttpDataProvider(uiHandler); // or VolleyDataProvider();
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

    public Map<Constants.Currency, Cryptocurrency> calculateCurrencyChangeOnGlobalScale() {
        for (Cryptocurrency cryptocurrency : mCryptocurrencyMap.values()) {
            double cryptocurrencyCurrentPrice = Double.parseDouble(cryptocurrency.getPriceUsd());
            double cryptocurrencyPreviousPrice = Double.parseDouble("10"); // TODO: 25/02/2018 Implement logic for previous data
            double cryptocurrenyChangePercentage = (cryptocurrencyCurrentPrice - cryptocurrencyPreviousPrice) / cryptocurrencyCurrentPrice;
            cryptocurrency.setPercenteChangeVersusGlobal(cryptocurrenyChangePercentage - getMarketChangePercentage());
        }
        return mCryptocurrencyMap;
    }

    public double getMarketChangePercentage() {
        double marketCurrentPrice = Double.parseDouble(mGlobalMarketData.getTotalMarketCapUsd());
        double marketPreviousPrice = Double.parseDouble("201241796675"); // TODO: 25/02/2018 Implement logic for previous data
        double marketPriceChangePercentage = (marketCurrentPrice - marketPreviousPrice) / marketCurrentPrice;
        return marketPriceChangePercentage;
    }

    public double getMarketUsdValue() {
        return Double.parseDouble(mGlobalMarketData.getTotalMarketCapUsd());
    }

    @UiThread
    @Override
    public void onReceive(Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap) {
        Log.d("onReceive", "CryptocurrencyProviderListener - done");
        this.mCryptocurrencyMap = cryptocurrencyMap;
        if (dataUpdatedCheck.setFirstAndCheck(true)) {
            mOperationsCallback.dataUpdated();
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
            mOperationsCallback.dataUpdated();
        }
    }
}
