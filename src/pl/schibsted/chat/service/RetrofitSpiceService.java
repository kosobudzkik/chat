package pl.schibsted.chat.service;

import com.google.gson.Gson;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import pl.schibsted.chat.R;
import pl.schibsted.chat.model.api.Chat;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @author krzysztof.kosobudzki
 */
public class RetrofitSpiceService extends RetrofitGsonSpiceService {

    @Override
    public void onCreate() {
        super.onCreate();

        addRetrofitInterface(Chat.class);
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        final RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/hal+json");
            }
        };

        return super.createRestAdapterBuilder()
                .setConverter(new GsonConverter(new Gson()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(requestInterceptor);
//                .setClient(new ApacheClient(new SslHttpClient()));
    }

    @Override
    protected String getServerUrl() {
        return getString(R.string.rest_server_url);
    }
}
