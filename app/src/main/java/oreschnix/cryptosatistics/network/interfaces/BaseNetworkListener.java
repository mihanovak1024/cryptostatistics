package oreschnix.cryptosatistics.network.interfaces;

/**
 * Created by miha.novak on 25/02/2018.
 */

public interface BaseNetworkListener {

    /**
     * Data receive fail callback.
     *
     * @param errorMessage - short description of error
     */
    void onFail(String errorMessage);
}
