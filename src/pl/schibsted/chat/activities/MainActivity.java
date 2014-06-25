package pl.schibsted.chat.activities;

import android.accounts.*;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import pl.schibsted.chat.AppConfig;
import pl.schibsted.chat.R;
import pl.schibsted.chat.fragments.ArticleFragment;
import pl.schibsted.chat.fragments.ArticlesFragment;
import pl.schibsted.chat.listeners.OnArticlesListener;
import pl.schibsted.chat.model.Article;
import pl.schibsted.chat.model.MyProfile;
import pl.schibsted.chat.model.api.HalArticleList;
import pl.schibsted.chat.model.requests.FetchArticleRequest;
import pl.schibsted.chat.model.requests.FetchArticlesRequest;
import pl.schibsted.chat.service.RetrofitSpiceService;
import pl.schibsted.chat.utils.SimpleLog;

import java.io.IOException;

/**
 * @author krzysztof.kosobudzki
 */
public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<MyProfile>, OnArticlesListener {
    private static final String CONTENT_FRAGMENT_TAG = "CONTENT_FRAGMENT_TAG";

    private AccountManager mAccountManager;

    private String mAccountType;
    private String mAuthTokenType;

    private boolean isAddingAccount;

    private final SpiceManager mSpiceManager = new SpiceManager(RetrofitSpiceService.class);

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

    @Override
    protected void onStart() {
        mSpiceManager.start(this);

        super.onStart();
    }

    @Override
    protected void onStop() {
        mSpiceManager.shouldStop();

        super.onStop();
    }

    private void initView() {
        swapFragment(ArticlesFragment.class, null);
    }

    private void swapFragment(Class<? extends Fragment> listClass, Bundle arguments) {
        Fragment f = getFragmentManager().findFragmentByTag(CONTENT_FRAGMENT_TAG);

        if (f != null && listClass.equals(((Object) f).getClass())) {
            // there is no need to swap the same fragments
            return;
        }

        try {
            Fragment fragment = listClass.newInstance();
            fragment.setArguments(arguments);

            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_container, fragment, CONTENT_FRAGMENT_TAG)
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

    @Override
    public void onArticleSelected(long articleId) {
        Bundle bundle = new Bundle();
        bundle.putLong(ArticleFragment.ID_KEY, articleId);

        swapFragment(ArticleFragment.class, bundle);
    }

    @Override
    public void onArticleLoadSelected(long articleId, RequestListener<Article> articleRequestListener) {
        mSpiceManager.execute(new FetchArticleRequest(articleId), articleRequestListener);
    }

    @Override
    public void onArticlesShouldLoad(int offset, int limit, RequestListener<HalArticleList> articleListRequestListener) {
        mSpiceManager.execute(new FetchArticlesRequest(limit, offset), articleListRequestListener);
    }

    private final RequestListener<Article> mArticleRequestListener = new RequestListener<Article>() {
        @Override
        public void onRequestFailure(SpiceException e) {
            Toast.makeText(getApplicationContext(), R.string.toast_unknown_error, Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }

        @Override
        public void onRequestSuccess(Article article) {
            Bundle bundle = new Bundle();
//            bundle.putString();
        }
    };
}
