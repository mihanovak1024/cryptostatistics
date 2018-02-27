package oreschnix.cryptosatistics.operations;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.model.GlobalMarketData;
import oreschnix.cryptosatistics.network.Constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by miha.novak on 27/02/2018.
 */

public class DataCalculationLogicTest {

    // Instance for testing
    private DataCalculationLogic dataCalculationLogic;

    @Before
    public void setup() {
        dataCalculationLogic = new DataCalculationLogic();
    }

    @After
    public void cleanup() {

    }

    /**
     * - one cryptocurrency data
     * <p>
     * - return the correct change of one cryptocurrency on global scale
     */
    @Test
    public void calculateCurrencyChangeOnGlobalScale_oneCryptocurrency_resultOK() {
        // Prepare the data
        Constants.Currency desiredCurrency = Constants.Currency.BTC; // We test on BTC currency (doesn't really matter)
        Cryptocurrency cryptocurrency = new Cryptocurrency();
        cryptocurrency.setPriceUsd("15"); // 15 instead 10 -> 0.5

        Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap = new HashMap<>();
        cryptocurrencyMap.put(desiredCurrency, cryptocurrency);

        // Mock GlobalMarketData object
        GlobalMarketData mockGlobalMarketData = mock(GlobalMarketData.class);
        when(mockGlobalMarketData.getTotalMarketCapUsd()).thenReturn("251241796675"); // 251241796675 instead 201241796675 -> 0.2485

        // Test
        Map<Constants.Currency, Cryptocurrency> returnedCryptocurrencyMap = dataCalculationLogic.calculateCurrencyChangeOnGlobalScale(cryptocurrencyMap, mockGlobalMarketData);

        // Assert results
        assertThat(returnedCryptocurrencyMap.get(desiredCurrency).getPercentChangeVersusGlobal(), equalTo(0.2515));
        verify(mockGlobalMarketData, times(1)).getTotalMarketCapUsd();
    }

    /**
     * - two cryptocurrency data
     * <p>
     * - return the correct change of both cryptocurrency on global scale
     */
    @Test
    public void calculateCurrencyChangeOnGlobalScale_twoCryptocurrencies_resultOK() {
        // Prepare the data
        Constants.Currency firstDesiredCurrency = Constants.Currency.ETH; // We test on ETH currency (doesn't really matter)
        Cryptocurrency firstDesiredCryptocurrency = new Cryptocurrency();
        firstDesiredCryptocurrency.setPriceUsd("5"); // 5 instead 10 -> -0.5

        Constants.Currency secondDesiredCurrency = Constants.Currency.BTC; // We test on BTC currency (doesn't really matter)
        Cryptocurrency secondDesiredCryptocurrency = new Cryptocurrency();
        secondDesiredCryptocurrency.setPriceUsd("15"); // 15 instead 10 -> 0.5

        Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap = new HashMap<>();
        cryptocurrencyMap.put(firstDesiredCurrency, firstDesiredCryptocurrency);
        cryptocurrencyMap.put(secondDesiredCurrency, secondDesiredCryptocurrency);

        // Mock GlobalMarketData object
        GlobalMarketData mockGlobalMarketData = mock(GlobalMarketData.class);
        when(mockGlobalMarketData.getTotalMarketCapUsd()).thenReturn("251241796675"); // 251241796675 instead 201241796675 -> 0.2485

        // Test
        Map<Constants.Currency, Cryptocurrency> returnedCryptocurrencyMap = dataCalculationLogic.calculateCurrencyChangeOnGlobalScale(cryptocurrencyMap, mockGlobalMarketData);

        // Assert results
        assertThat(returnedCryptocurrencyMap.get(firstDesiredCurrency).getPercentChangeVersusGlobal(), equalTo(-0.7485));
        assertThat(returnedCryptocurrencyMap.get(secondDesiredCurrency).getPercentChangeVersusGlobal(), equalTo(0.2515));
        verify(mockGlobalMarketData, times(2)).getTotalMarketCapUsd();
    }

}
