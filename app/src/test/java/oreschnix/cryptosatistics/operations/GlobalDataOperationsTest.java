package oreschnix.cryptosatistics.operations;

import android.content.Context;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import oreschnix.cryptosatistics.model.Cryptocurrency;
import oreschnix.cryptosatistics.model.GlobalMarketData;
import oreschnix.cryptosatistics.network.Constants;
import oreschnix.cryptosatistics.network.interfaces.CryptocurrencyProviderListener;
import oreschnix.cryptosatistics.network.interfaces.DataProvider;
import oreschnix.cryptosatistics.network.interfaces.GlobalDataProviderListener;
import oreschnix.cryptosatistics.operations.listeners.OperationsCallback;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by miha.novak on 26/02/2018.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class GlobalDataOperationsTest {

    // Instance of GlobalDataOperations for test
    private GlobalDataOperations globalDataOperations;

    @Mock
    private OperationsCallback mockOperationsCallback;

    @Mock
    private DataProvider mockGlobalMarketDataProvider;

    @Mock
    private DataProvider mockDataCurrencyProvider;

    @Mock
    private Context mockContext;

    @Mock
    private CryptocurrencyProviderListener mockCryptocurrencyProviderListener;

    @Mock
    private GlobalDataProviderListener mockGlobalDataProviderListener;

    @Before
    public void setup() {
        globalDataOperations = new GlobalDataOperations(mockOperationsCallback, mockGlobalMarketDataProvider, mockDataCurrencyProvider);
    }

    @Before
    public void setupLogger() {
        // mock the log method of Logger class to redirect output to console
        PowerMockito.mockStatic(Log.class);
        PowerMockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                String tag = invocation.getArgument(0);
                String message = invocation.getArgument(1);
                System.out.println(tag + ": " + message);
                return null;
            }
        }).when(Log.class);

        Log.d(anyString(), anyString());
    }

    @After
    public void cleanup() {
    }

    /**
     * - updateData
     * <p>
     * - CryptocurrencyProviderListener called onReceive
     * - GlobalDataProviderListener called onReceive
     * - Operationscallback called dataUpdated
     */
    @Test
    public void updateData_marketDataAndCryptoMapOnReceive_dataUpdated() {
        // Prepare data
        final Map<Constants.Currency, Cryptocurrency> cryptocurrencyMap = new HashMap<>();
        Cryptocurrency cryptocurrency = new Cryptocurrency();
        cryptocurrency.setPriceUsd("500");
        cryptocurrencyMap.put(Constants.Currency.BTC, cryptocurrency);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                globalDataOperations.onReceive(cryptocurrencyMap);
                return null;
            }
        }).when(mockDataCurrencyProvider).getCryptocurrencyInfoList(eq(mockContext), Matchers.anyListOf(Constants.Currency.class), any(CryptocurrencyProviderListener.class));
        final GlobalMarketData globalMarketData = new GlobalMarketData();
        globalMarketData.setTotalMarketCapUsd("200");
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                globalDataOperations.onReceive(globalMarketData);
                return null;
            }
        }).when(mockGlobalMarketDataProvider).getGlobalInfo(eq(mockContext), any(GlobalDataProviderListener.class));

        // Test
        globalDataOperations.updateData(mockContext);

        // Assert result
        verify(mockOperationsCallback, times(1)).dataUpdated(cryptocurrencyMap, globalMarketData);
    }

}
