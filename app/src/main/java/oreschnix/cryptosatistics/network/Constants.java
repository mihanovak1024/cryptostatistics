package oreschnix.cryptosatistics.network;

/**
 * Created by miha.novak on 24/02/2018.
 */

public class Constants {

    public static final String BASE_URL = "https://api.coinmarketcap.com/v1/ticker/";

    public enum Currency {
        BTC("bitcoin"),
        ETH("ethereum"),
        LTC("litecoin");

        private final String currencyID;

        Currency(String currencyID) {
            this.currencyID = currencyID;
        }

        public static Currency forValue(String value) {
            for (Currency currency : values()) {
                if (value.equals(currency.currencyID)) {
                    return currency;
                }
            }
            return null;
        }

        public String getCurrencyID() {
            return currencyID;
        }
    }

}
