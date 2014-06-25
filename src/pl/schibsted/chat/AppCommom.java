package pl.schibsted.chat;

import com.squareup.otto.Bus;

/**
 * Static services. Kind of a pre-initialized service locator
 */
public class AppCommom {
    public static Bus EventBus;
    static {
        EventBus = new Bus();
    }
}
