package pl.schibsted.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import pl.schibsted.chat.AppConfig;
import pl.schibsted.chat.R;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author krzysztof.kosobudzki
 */
@SuppressWarnings("MagicNumber")
public final class AssetsUtils {
    public static void initAssets(Context context, String str, int b, int e) throws IOException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Reader reader = new InputStreamReader(context.getAssets().open(str), "UTF-8");
        String results = "";

        int read = reader.read();
        int in = 0;

        while (read != -1) {
            char c = (char) read;

            if (in >= b && in <= e) {
                results += c;
            }

            if (in > e + 1) {
                break;
            }

            in++;
            read = reader.read();
        }

        reader.close();

        byte[] a = results.substring(0, 32).getBytes();
        byte[] z = results.substring(32, 64).getBytes();
        byte[] w = results.substring(64, 92).getBytes();
        byte[] y = results.substring(92, 120).getBytes();

        String a1 = new String(Base64.decode(a, Base64.DEFAULT));
        String z1 = new String(Base64.decode(z, Base64.DEFAULT));
        String w1 = new String(Base64.decode(w, Base64.DEFAULT));
        String y1 = new String(Base64.decode(y, Base64.DEFAULT));

        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(AppConfig.SPiD.CLIENT_ID, a1);
        edit.putString(AppConfig.SPiD.SERVER_CLIENT_ID, z1);
        edit.putString(AppConfig.SPiD.CLIENT_SECRET, w1);
        edit.putString(AppConfig.SPiD.SIGN_SECRET, y1);
        edit.putString(AppConfig.SPiD.APP_URL_SCHEME, context.getString(R.string.spid_config_app_url_scheme));
        edit.putString(AppConfig.SPiD.SERVER_URL, context.getString(R.string.spid_config_server_url));
        edit.commit();
    }
}
