package oreschnix.cryptosatistics.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by miha.novak on 25/02/2018.
 */

public class HandlerFactory {

    /**
     * Creates a new Handler.
     *
     * @param name - The name of this handler thread. Generally should be post fixed with 'Thread'
     * @return
     */
    public static Handler createHandler(String name) {
        HandlerThread handlerThread = new HandlerThread(name);
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    /**
     * Will use the simple name of the class with 'Thread' postfixed.
     * ex. HandlerFactoryThread
     *
     * @param clazz
     * @return
     */
    public static Handler createHandler(Class clazz) {
        return createHandler(clazz.getSimpleName() + "Thread");
    }

    public static Handler createUiHandler() {
        return new Handler(Looper.getMainLooper());
    }

}
