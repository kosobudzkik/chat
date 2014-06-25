package pl.schibsted.chat;

/**
 * @author krzysztof.kosobudzki
 */
public class AppConfig {
    public static final String TAG = "chat";

    public class Loader {
        public static final int PROFILE = 1;
    }

    public class SPiD {
        public static final String CLIENT_ID = "spid_client_id";
        public static final String SERVER_CLIENT_ID = "spid_server_client_id";
        public static final String CLIENT_SECRET = "spid_client_secret";
        public static final String SIGN_SECRET = "spid_sign_secret";
        public static final String APP_URL_SCHEME = "spid_app_url_scheme";
        public static final String SERVER_URL = "spid_server_url";
    }
}
