package oreschnix.cryptosatistics.operations.listeners;

/**
 * Created by miha.novak on 25/02/2018.
 */

public interface OperationsCallback {

    /**
     * Notifies that the data has been updated (retrieved from the network).
     */
    void dataUpdated();

}
