package pl.schibsted.chat.utils;

import android.util.Log;
import pl.schibsted.chat.AppConfig;

/**
 * @author krzysztof.kosobudzki
 */
public class SimpleLog {
    /**
     * Simple alias for log
     *
     * @param log
     * @param args
     */
    public static void d(String log, Object... args) {
        Log.d(AppConfig.TAG, String.format(log, args));
    }
}
