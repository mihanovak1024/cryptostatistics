package oreschnix.cryptosatistics.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import oreschnix.cryptosatistics.R;
import oreschnix.cryptosatistics.model.Cryptocurrency;

/**
 * Created by miha.novak on 25/02/2018.
 */

public class CurrencyGlobalChangeView extends LinearLayout {

    private TextView mCryptocurrencyNameTV;
    private TextView mCryptocurrencyIdTV;
    private TextView mCryptocurrencyChangeTV;

    private boolean alreadyInitialized = false;

    public CurrencyGlobalChangeView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.currency_global_change_view, this);
        initViews(context);
    }

    public CurrencyGlobalChangeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public CurrencyGlobalChangeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    public CurrencyGlobalChangeView(Context context, Cryptocurrency cryptocurrency) {
        this(context);
        if (alreadyInitialized) {
            return;
        }
        alreadyInitialized = true;
        mCryptocurrencyNameTV.setText(cryptocurrency.getName());
        mCryptocurrencyIdTV.setText(cryptocurrency.getSymbol());
        double percentageChange = cryptocurrency.getPercentChangeVersusGlobal();
        mCryptocurrencyChangeTV.setText((percentageChange >= 0 ? "+ " : "- ") + String.format("%.2f", percentageChange) + "");
        mCryptocurrencyChangeTV.setBackgroundColor(getResources().getColor(percentageChange >= 0 ? R.color.positive_percentage_change : R.color.negative_percentage_change));
    }

    private void initViews(Context context) {
        mCryptocurrencyChangeTV = (TextView) this.findViewById(R.id.currency_global_change_percent_change);
        mCryptocurrencyIdTV = (TextView) this.findViewById(R.id.currency_global_change_currency_id);
        mCryptocurrencyNameTV = (TextView) this.findViewById(R.id.currency_global_change_currency_name);
    }

}
