package pl.schibsted.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import pl.schibsted.chat.auth.SPiDAccountAuthenticator;

/**
 * @author krzysztof.kosobudzki
 */
public class SPiDAuthenticationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new SPiDAccountAuthenticator().getIBinder();
    }
}
