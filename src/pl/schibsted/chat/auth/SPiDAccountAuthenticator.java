package pl.schibsted.chat.auth;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import pl.schibsted.chat.App;
import pl.schibsted.chat.R;
import pl.schibsted.chat.activities.LoginActivity;
import pl.schibsted.chat.model.api.SPiD;
import pl.schibsted.chat.model.api.SPiDOneTimeResponse;
import pl.schibsted.chat.utils.SimpleLog;
import com.google.api.client.http.HttpStatusCodes;
import com.google.gson.Gson;
import com.schibsted.android.sdk.SPiDClient;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;
import retrofit.converter.GsonConverter;

/**
 * @author krzysztof.kosobudzki
 */
public class SPiDAccountAuthenticator extends AbstractAccountAuthenticator {
    private static final int BAD_REQUEST_CODE = 400;

    private SPiD mSpidService;

    public SPiDAccountAuthenticator() {
        super(App.getInstance().getApplicationContext());
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String accountType) {
        SimpleLog.d("SPiDAccountAuthenticator editProperties");

        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        SimpleLog.d("SPiDAccountAuthenticator addAccount");

        final Context context = App.getInstance().getApplicationContext();
        final AccountManager accountManager = AccountManager.get(context);
        final Account[] accounts = accountManager.getAccountsByType(accountType);

        final Bundle bundle = new Bundle();
        final Intent intent = new Intent(context, LoginActivity.class);

        if (accounts != null && accounts.length > 0) {
            SimpleLog.d("SPiDAccountAuthenticator addAccount ACCOUNT EXISTS");

            intent.putExtra(LoginActivity.SINGLE_ACCOUNT_ALLOWED_MSG, true);
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

            bundle.putParcelable(AccountManager.KEY_INTENT, intent);

            return bundle;
        }

        intent.putExtra(LoginActivity.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(LoginActivity.KEY_AUTH_TYPE, authTokenType);
        intent.putExtra(LoginActivity.KEY_IS_ADDING_NEW_ACCOUNT, true);

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle options) throws NetworkErrorException {
        SimpleLog.d("SPiDAccountAuthenticator confirmCredentials");

        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        SimpleLog.d("SPiDAccountAuthenticator getAuthToken");

        final Context context = App.getInstance().getApplicationContext();
        final AccountManager accountManager = AccountManager.get(context);
        String authToken = accountManager.peekAuthToken(account, authTokenType);

        if (TextUtils.isEmpty(authToken)) {
            if (mSpidService == null) {
                mSpidService = prepareSpidService();
            }

            try {
                if (SPiDClient.getInstance().getAccessToken() == null) {
                    return getBundleWithLoginActivity(response, account.type, authTokenType);
                }

                SPiDOneTimeResponse oneTimeResponse = mSpidService.getOneTimeCode(
                        SPiDClient.getInstance().getConfig().getServerClientID(),
                        SPiDClient.getInstance().getAccessToken().getAccessToken(),
                        "code"
                );

                authToken = oneTimeResponse.getData().getCode();

                accountManager.setAuthToken(account, authTokenType, authToken);
            } catch (Exception e) {
                e.printStackTrace();

                if (e instanceof RetrofitError) {
                    RetrofitError re = (RetrofitError) e;
                    String errorMsg = context.getString(R.string.toast_unknown_error);

                    if (re.getResponse() != null && re.getResponse().getStatus() == BAD_REQUEST_CODE) {
                        return getBundleWithLoginActivity(response, account.type, authTokenType);
                    }

                    if (re.getResponse() != null && re.getResponse().getStatus() == HttpStatusCodes.STATUS_CODE_NOT_FOUND) {
                        errorMsg = context.getString(R.string.toast_no_access_to_that_product);
                    }

                    return getBundleWithLoginActivity(response, account.type, authTokenType, errorMsg);
                }

                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);

            return result;
        }

        // We couldn't access the user's password therefore we create an
        // intent to ask user for their credentials.

        return getBundleWithLoginActivity(response, account.type, authTokenType);
    }

    private SPiD prepareSpidService() {
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SPiDClient.getInstance().getConfig().getServerURL())
                .setConverter(new GsonConverter(new Gson()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        return restAdapter.create(SPiD.class);
    }

    private Bundle getBundleWithLoginActivity(AccountAuthenticatorResponse response, String accountType, String authTokenType) {
        return getBundleWithLoginActivity(response, accountType, authTokenType, null);
    }

    private Bundle getBundleWithLoginActivity(AccountAuthenticatorResponse response, String accountType, String authTokenType, String errorMsg) {
        final Intent intent = new Intent(App.getInstance().getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(LoginActivity.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(LoginActivity.KEY_AUTH_TYPE, authTokenType);
        intent.putExtra(LoginActivity.ERROR_MSG, errorMsg);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        SimpleLog.d("SPiDAccountAuthenticator getAuthTokenLabel");

        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        SimpleLog.d("SPiDAccountAuthenticator updateCredentials");

        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] features) throws NetworkErrorException {
        SimpleLog.d("SPiDAccountAuthenticator hasFeatures");

        return null;
    }

    @Override
    public Bundle getAccountRemovalAllowed(AccountAuthenticatorResponse response, Account account) throws NetworkErrorException {
        final Bundle result = super.getAccountRemovalAllowed(response, account);

        // TODO

        return result;
    }
}
