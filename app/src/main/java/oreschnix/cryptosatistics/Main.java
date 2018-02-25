package oreschnix.cryptosatistics;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.model.GlobalMarketData;
import oreschnix.cryptosatistics.network.Constants;
import oreschnix.cryptosatistics.network.HttpDataProvider;
import oreschnix.cryptosatistics.network.VolleyDataProvider;
import oreschnix.cryptosatistics.network.interfaces.CryptocurrencyProviderListener;
import oreschnix.cryptosatistics.network.interfaces.DataProvider;
import oreschnix.cryptosatistics.network.interfaces.GlobalDataProviderListener;
import oreschnix.cryptosatistics.util.HandlerFactory;

/**
 * Created by miha.novak on 23/02/2018.
 */

public class Main extends AppCompatActivity implements CryptocurrencyProviderListener, GlobalDataProviderListener {

    private TextView mCurrencyTypeTV;
    private TextView mCurrencyPriceTV;
    private TextView mGlobalCapTV;
    private TextView mGlobalDailyVolumeTV;

    private Handler mUiHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scene);
        initViews();
        this.mUiHandler = HandlerFactory.createUiHandler();

        DataProvider dataCurrencyProvider = new VolleyDataProvider(); // or HttpDataProvider();
        dataCurrencyProvider.getCryptocurrencyInfo(this, Constants.Currency.BTC, this);

        DataProvider globalMarketDataProvider = new HttpDataProvider(mUiHandler);
        globalMarketDataProvider.getGlobalInfo(this, this);
    }

    private void initViews() {
        mCurrencyPriceTV = (TextView) findViewById(R.id.currency_price);
        mCurrencyTypeTV = (TextView) findViewById(R.id.currency_type);
        mGlobalCapTV = (TextView) findViewById(R.id.global_total_cap);
        mGlobalDailyVolumeTV = (TextView) findViewById(R.id.global_daily_volume);
    }

    @UiThread
    @Override
    public void onReceive(Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap) {
        Cryptocurrency cryptocurrency = cryptocurrencyMap.get(Constants.Currency.BTC);
        mCurrencyTypeTV.setText(cryptocurrency.getName());
        mCurrencyPriceTV.setText(cryptocurrency.getPriceUsd());
        Log.d("onReceive", "CryptocurrencyProviderListener - done");
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
        mGlobalCapTV.setText(globalMarketData.getTotalMarketCapUsd());
        mGlobalDailyVolumeTV.setText(globalMarketData.getTotalDailyVolumeUsd());
    }
}
