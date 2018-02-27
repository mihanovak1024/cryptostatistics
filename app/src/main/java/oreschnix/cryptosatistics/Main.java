package oreschnix.cryptosatistics;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.model.GlobalMarketData;
import oreschnix.cryptosatistics.network.Constants;
import oreschnix.cryptosatistics.network.HttpDataProvider;
import oreschnix.cryptosatistics.network.VolleyDataProvider;
import oreschnix.cryptosatistics.network.interfaces.DataProvider;
import oreschnix.cryptosatistics.operations.DataCalculationLogic;
import oreschnix.cryptosatistics.operations.GlobalDataOperations;
import oreschnix.cryptosatistics.operations.listeners.OperationsCallback;
import oreschnix.cryptosatistics.util.HandlerFactory;
import oreschnix.cryptosatistics.views.CurrencyGlobalChangeView;

/**
 * Created by miha.novak on 23/02/2018.
 */

public class Main extends AppCompatActivity implements OperationsCallback {

    private TextView mGlobalMarketChangePercentageTV;
    private TextView mGlobalMarketUsdTV;
    private LinearLayout mCryptocurrencyContainerLL;

    private Handler mUiHandler;

    private GlobalDataOperations mGlobalDataOperations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scene);
        initViews();
        this.mUiHandler = HandlerFactory.createUiHandler();

        DataProvider dataCurrencyProvider = new VolleyDataProvider(); // or HttpDataProvider(uiHandler);
        DataProvider globalMarketDataProvider = new HttpDataProvider(mUiHandler); // or VolleyDataProvider();

        mGlobalDataOperations = new GlobalDataOperations(this, globalMarketDataProvider, dataCurrencyProvider);
        mGlobalDataOperations.updateData(this);
    }

    private void initViews() {
        mGlobalMarketChangePercentageTV = (TextView) findViewById(R.id.market_change_percentage);
        mCryptocurrencyContainerLL = (LinearLayout) findViewById(R.id.cryptocurrency_change_container);
        mGlobalMarketUsdTV = (TextView) findViewById(R.id.market_usd_value);
    }

    @Override
    public void dataUpdated(final Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap,
                            final GlobalMarketData globalMarketData) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                double marketChangePercentage = DataCalculationLogic.getMarketChangePercentage(globalMarketData);
                mGlobalMarketChangePercentageTV.setText((marketChangePercentage >= 0 ? "+ " : "- ") + String.format("%.2f", marketChangePercentage) + "");
                mGlobalMarketChangePercentageTV.setBackgroundColor(getResources().getColor(
                        marketChangePercentage >= 0 ? R.color.positive_percentage_change : R.color.negative_percentage_change));
                mGlobalMarketUsdTV.setText(DataCalculationLogic.getMarketUsdValue(globalMarketData) + "");
                Map<Constants.Currency, Cryptocurrency> calculatedChangeOnGlobalScale = DataCalculationLogic.calculateCurrencyChangeOnGlobalScale(cryptocurrencyMap, globalMarketData);
                for (Cryptocurrency cryptocurrency : calculatedChangeOnGlobalScale.values()) {
                    CurrencyGlobalChangeView currencyGlobalChangeView = new CurrencyGlobalChangeView(Main.this, cryptocurrency);
                    mCryptocurrencyContainerLL.addView(currencyGlobalChangeView);
                }
            }
        });
    }
}
