package oreschnix.cryptosatistics.operations;

import java.text.DecimalFormat;
import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.model.GlobalMarketData;
import oreschnix.cryptosatistics.network.Constants;

/**
 * Created by miha.novak on 27/02/2018.
 */

public class DataCalculationLogic {

    /**
     * Calculates the price difference between the price change of
     * the desired cryptocurrency and the price change of the global market.
     *
     * @param cryptocurrencyMap - map of desired cryptocurrencies
     * @param globalMarketData  - global market data
     * @return
     */
    public static Map<Constants.Currency, Cryptocurrency> calculateCurrencyChangeOnGlobalScale(Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap,
                                                                                               GlobalMarketData globalMarketData) {
        for (Cryptocurrency cryptocurrency : cryptocurrencyMap.values()) {
            double cryptocurrencyCurrentPrice = Double.parseDouble(cryptocurrency.getPriceUsd());
            double cryptocurrencyPreviousPrice = Double.parseDouble("10"); // TODO: 25/02/2018 Implement logic for previous data
            double cryptocurrenyChangePercentage = (cryptocurrencyCurrentPrice - cryptocurrencyPreviousPrice) / cryptocurrencyPreviousPrice;
            cryptocurrency.setPercentChangeVersusGlobal(decimalFormatter(4, cryptocurrenyChangePercentage - getMarketChangePercentage(globalMarketData)));
        }
        return cryptocurrencyMap;
    }

    /**
     * Calculates the global market price change.
     *
     * @param globalMarketData
     * @return
     */
    public static double getMarketChangePercentage(GlobalMarketData globalMarketData) {
        double marketCurrentPrice = Double.parseDouble(globalMarketData.getTotalMarketCapUsd());
        double marketPreviousPrice = Double.parseDouble("201241796675"); // TODO: 25/02/2018 Implement logic for previous data
        double marketPriceChangePercentage = decimalFormatter(4, (marketCurrentPrice - marketPreviousPrice) / marketPreviousPrice);
        return marketPriceChangePercentage;
    }

    public static double getMarketUsdValue(GlobalMarketData globalMarketData) {
        return decimalFormatter(4, Double.parseDouble(globalMarketData.getTotalMarketCapUsd()));
    }

    /**
     * Trims the double decimal places to the desired number.
     *
     * @param decimalPlaces - desired number of decimal places
     * @param number        - number to be formatted
     * @return
     */
    private static double decimalFormatter(int decimalPlaces, double number) {
        String decimalFormatString = "#.";
        while (decimalPlaces-- > 0) {
            decimalFormatString += "#";
        }
        DecimalFormat df = new DecimalFormat(decimalFormatString);
        return Double.parseDouble(df.format(number));
    }

}
