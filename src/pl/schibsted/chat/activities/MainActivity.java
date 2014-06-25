package pl.schibsted.chat.activities;

import android.accounts.*;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ViewFlipper;
import pl.schibsted.chat.AppConfig;
import pl.schibsted.chat.R;
import pl.schibsted.chat.fragments.ArticlesFragment;
import pl.schibsted.chat.model.MyProfile;
import pl.schibsted.chat.utils.SimpleLog;

import java.io.IOException;

/**
 * @author krzysztof.kosobudzki
 */
public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<MyProfile> {
    private static final String LIST_FRAGMENT_TAG = "LIST_FRAGMENT_TAG";
    private static final String DETAILS_FRAGMENT_TAG = "DETAILS_FRAGMENT_TAG";

    private AccountManager mAccountManager;

    private String mAccountType;
    private String mAuthTokenType;

    private boolean isAddingAccount;

    private ViewFlipper mViewFilpper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        mAccountManager = AccountManager.get(this);
        mAccountType = getString(R.string.authenticator_account_type);
        mAuthTokenType = getString(R.string.authenticator_token_type);

        if (hasAccountAndIsLoggedIn()) {
            readSPiDAccount();
        } else {
            invokeAddAccount();
        }

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!hasAccountAndIsLoggedIn() && !isAddingAccount) {
            invokeAddAccount();
        }
    }

    private void initView() {
        mViewFilpper = (ViewFlipper) findViewById(R.id.view_flipper);

        swapFragment(ArticlesFragment.class, null);
    }

    private void swapFragment(Class<? extends Fragment> clazz, Bundle arguments) {
        Fragment f = getFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);

        if (clazz.equals(((Object) f).getClass())) {
            // there is no need to swap the same fragments
            return;
        }

        try {
            Fragment fragment = clazz.newInstance();
            fragment.setArguments(arguments);

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.list_container, fragment, LIST_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if user has an active account.
     *
     * @return
     */
    private boolean hasAccountAndIsLoggedIn() {
        SimpleLog.d("MainActivity hasAccoundAndIsLoggedIn");
        Account[] accounts = mAccountManager.getAccountsByType(mAccountType);

        if (accounts == null || accounts.length == 0) {
            return false;
        }

        if (TextUtils.isEmpty(mAccountManager.peekAuthToken(accounts[0], mAuthTokenType))) {
            return false;
        }

        if (mAccountManager.getUserData(accounts[0], LoginActivity.ACCOUNT_SPID_ID) == null) {
            return false;
        }

        return true;
    }

    private void readSPiDAccount() {
        SimpleLog.d("MainActivity readSPiDAccount");

        Account[] accounts = mAccountManager.getAccountsByType(getString(R.string.authenticator_account_type));
        Account account = accounts[0];

        Bundle args = new Bundle();
        args.putLong(
                LoginActivity.ACCOUNT_SPID_ID,
                Long.valueOf(mAccountManager.getUserData(account, LoginActivity.ACCOUNT_SPID_ID))
        );

        if (getLoaderManager().getLoader(AppConfig.Loader.PROFILE) == null) {
            getLoaderManager().initLoader(AppConfig.Loader.PROFILE, args, MainActivity.this);
        } else {
            getLoaderManager().restartLoader(AppConfig.Loader.PROFILE, args, MainActivity.this);
        }
    }

    private void invokeAddAccount() {
        SimpleLog.d("MainActivity invokeAddAccount");
        isAddingAccount = true;

        mAccountManager.addAccount(
                mAccountType,
                getString(R.string.authenticator_token_type),
                null,
                null,
                MainActivity.this,
                mAddAccountCallback,
                null
        );
    }

    @Override
    public Loader<MyProfile> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case AppConfig.Loader.PROFILE:
                // TODO
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<MyProfile> loader, MyProfile profile) {
        switch (loader.getId()) {
            case AppConfig.Loader.PROFILE:
                try {
                    // TODO
                } catch (IllegalStateException ex) {
                    // TODO (profile is null)
                    ex.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<MyProfile> loader) {
        switch (loader.getId()) {
            case AppConfig.Loader.PROFILE:
                break;
        }
    }

    private final AccountManagerCallback<Bundle> mAddAccountCallback = new AccountManagerCallback<Bundle>() {
        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            try {
                future.getResult();

                readSPiDAccount();
            } catch (OperationCanceledException e) {
                SimpleLog.d("MainActivity, mAddAccountCallback, OperationCanceledException %s ", e);

                // We really need an account...

                finish();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            }

        }
    };
}
