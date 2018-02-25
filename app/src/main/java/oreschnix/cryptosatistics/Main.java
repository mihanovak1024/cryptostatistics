package oreschnix.cryptosatistics;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import oreschnix.cryptosatistics.operations.GlobalDataOperations;
import oreschnix.cryptosatistics.operations.listeners.OperationsCallback;
import oreschnix.cryptosatistics.util.HandlerFactory;

/**
 * Created by miha.novak on 23/02/2018.
 */

public class Main extends AppCompatActivity implements OperationsCallback {

    private TextView mCurrencyTypeTV;
    private TextView mCurrencyPriceTV;
    private TextView mGlobalCapTV;
    private TextView mGlobalDailyVolumeTV;

    private Handler mUiHandler;

    private GlobalDataOperations mGlobalDataOperations;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_scene);
        initViews();
        this.mUiHandler = HandlerFactory.createUiHandler();

        mGlobalDataOperations = new GlobalDataOperations(mUiHandler, this);
        mGlobalDataOperations.updateData(this);
    }

    private void initViews() {
        mCurrencyPriceTV = (TextView) findViewById(R.id.currency_price);
        mCurrencyTypeTV = (TextView) findViewById(R.id.currency_type);
        mGlobalCapTV = (TextView) findViewById(R.id.global_total_cap);
        mGlobalDailyVolumeTV = (TextView) findViewById(R.id.global_daily_volume);
    }

    @Override
    public void dataUpdated() {
        List<String> calculatedChangeOnGlobalScale = mGlobalDataOperations.calculateCurrencyChangeOnGlobalScale();
    }
}
