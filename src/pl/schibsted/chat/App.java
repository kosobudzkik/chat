package pl.schibsted.chat;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.SparseArray;
import pl.schibsted.chat.utils.AssetsUtils;
import com.schibsted.android.sdk.SPiDClient;
import com.schibsted.android.sdk.configuration.SPiDConfiguration;
import com.schibsted.android.sdk.configuration.SPiDConfigurationBuilder;

import java.io.IOException;

/**
 * @author krzysztof.kosobudzki
 */
public class App extends Application {
    private static final int ASSETS_IMAGE_E = 123;
    private static final int ASSETS_IMAGE_B = 243;

    public static final SparseArray<String> FONTS = new SparseArray<String>();

    private SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>();

    static {
        FONTS.put(Typeface.NORMAL, "Lato-Light.ttf");
        FONTS.put(Typeface.BOLD, "Lato-Regular.ttf");
        FONTS.put(Typeface.BOLD_ITALIC, "Lato-BoldItalic.ttf");
        FONTS.put(Typeface.ITALIC, "Lato-Italic.ttf");
    }

    private static App obj;

    @Override
    public void onCreate() {
        super.onCreate();

        obj = this;

        configureSPiD();
        configureStrictMode();
        /*configureParseDotCom();*/
    }

    public Typeface getTypeface(int style) {
        if (mTypefaces.get(style) == null) {
            mTypefaces.put(style, Typeface.createFromAsset(getAssets(), FONTS.get(style)));
        }

        return mTypefaces.get(style);
    }

    public static App getInstance() {
        return obj;
    }

    private void configureStrictMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());

            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    private void configureSPiD() {
        try {
            AssetsUtils.initAssets(getApplicationContext(), "stp_logo.png", ASSETS_IMAGE_E, ASSETS_IMAGE_B);
        } catch (IOException e) {
            throw new RuntimeException("Could not init assets!");
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SPiDConfiguration config = new SPiDConfigurationBuilder()
                .clientID(prefs.getString(AppConfig.SPiD.CLIENT_ID, ""))
                .clientSecret(prefs.getString(AppConfig.SPiD.CLIENT_SECRET, ""))
                .appURLScheme(prefs.getString(AppConfig.SPiD.APP_URL_SCHEME, ""))
                .serverRedirectUri("login")
                .serverURL(prefs.getString(AppConfig.SPiD.SERVER_URL, ""))
                .signSecret(prefs.getString(AppConfig.SPiD.SIGN_SECRET, ""))
                .serverClientID(prefs.getString(AppConfig.SPiD.SERVER_CLIENT_ID, ""))
                .context(this)
                .build();

        config.setDebugMode(true);
        SPiDClient.getInstance().configure(config);
    }
}
