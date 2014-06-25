package pl.schibsted.chat.activities;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.schibsted.android.sdk.SPiDClient;
import com.schibsted.android.sdk.exceptions.SPiDException;
import com.schibsted.android.sdk.listener.SPiDAuthorizationListener;
import com.schibsted.android.sdk.request.SPiDUserCredentialTokenRequest;
import pl.schibsted.chat.R;
import pl.schibsted.chat.exception.AuthorizationException;
import pl.schibsted.chat.utils.SimpleLog;

import java.io.IOException;

/**
 * @author krzysztof.kosobudzki
 */
public class LoginActivity extends AccountAuthenticatorActivity implements View.OnClickListener {
    public static final String ACCOUNT_SPID_ID = "ACCOUNT_SPID_ID";
    public static final String SINGLE_ACCOUNT_ALLOWED_MSG = "SINGLE_ACCOUNT_ALLOWED_MSG";
    public static final String KEY_ACCOUNT_TYPE = "KEY_ACCOUNT_TYPE";
    public static final String KEY_AUTH_TYPE = "KEY_AUTH_TYPE";
    public static final String KEY_IS_ADDING_NEW_ACCOUNT = "KEY_IS_ADDING_NEW_ACCOUNT";
    public static final String ERROR_MSG = "ERROR_MSG";

    private AccountManager mAccountManager;

    private String mAccountType;
    private String mAuthTokenType;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private View mLoadingLayout;

    private Animation mSlideUpAnimation;
    private Animation mSlideDownAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mAccountManager = AccountManager.get(this);
        mAccountType = getString(R.string.authenticator_account_type);
        mAuthTokenType = getString(R.string.authenticator_token_type);

        initView();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        SimpleLog.d("LoginActivity onNewIntent error msg=%s", intent.getStringExtra(ERROR_MSG));

        if (intent.getStringExtra(ERROR_MSG) != null) {
            onError(new AuthorizationException(intent.getStringExtra(ERROR_MSG)));
        }
    }

    private void initView() {
        mEmailEditText = (EditText) findViewById(R.id.email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoadingLayout = findViewById(R.id.loading_layout);

        mLoginButton.setOnClickListener(this);

        mSlideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        mSlideUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mLoadingLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        mSlideDownAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        mSlideDownAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLoadingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void displayLoginForm() {
        mEmailEditText.setEnabled(true);
        mPasswordEditText.setEnabled(true);
        mLoginButton.setEnabled(true);

        if (mLoadingLayout.getVisibility() == View.VISIBLE) {
            mLoadingLayout.clearAnimation();

            mLoadingLayout.setAnimation(mSlideDownAnimation);
            mLoadingLayout.startAnimation(mSlideDownAnimation);
        }
    }

    private void onError(Exception ex) {
        SimpleLog.d("LoginActivity onError, ex=%s, class=%s", ex.getMessage(), ex.getClass());

        if (ex instanceof SPiDException) {
            SPiDException spex = (SPiDException) ex;

            Toast.makeText(getApplicationContext(), "Error: " + spex.getError(), Toast.LENGTH_SHORT).show();
        } else if (ex instanceof OperationCanceledException) {
            // nth to do here
        } else if (ex instanceof AuthorizationException) {
            AuthorizationException ae = (AuthorizationException) ex;

            Toast.makeText(getApplicationContext(), "Error: " + ae.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            ex.printStackTrace();

            Toast.makeText(getApplicationContext(), "Another Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        displayLoginForm();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                final String email = mEmailEditText.getText().toString();
                final String password = mPasswordEditText.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    displayLoading();

                    SPiDUserCredentialTokenRequest tokenRequest = new SPiDUserCredentialTokenRequest(email, password, new LoginListener(email));
                    tokenRequest.execute();
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_both_email_and_password), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void displayLoading() {
        mEmailEditText.setEnabled(false);
        mPasswordEditText.setEnabled(false);
        mLoginButton.setEnabled(false);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(mPasswordEditText.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        }

        if (mLoadingLayout.getVisibility() == View.GONE) {
            mLoadingLayout.clearAnimation();

            mLoadingLayout.setAnimation(mSlideUpAnimation);
            mLoadingLayout.startAnimation(mSlideUpAnimation);
        }
    }

    private class GetTokenCallback implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            try {
                finishAddAccount(future.getResult());
            } catch (Exception e) {
                onError(e);
            }
        }

        private void finishAddAccount(final Bundle authTokenResult) {
            setAccountAuthenticatorResult(authTokenResult);

            Account[] accounts = mAccountManager.getAccountsByType(mAccountType);

            if (accounts != null && accounts.length > 0) {
                mAccountManager.setUserData(accounts[0], ACCOUNT_SPID_ID, SPiDClient.getInstance().getAccessToken().getUserID());
            }

            final Intent resultIntent = new Intent();
            resultIntent.putExtras(authTokenResult);

            setResult(RESULT_OK, resultIntent);

            LoginActivity.this.finish();
        }
    }

    private class LoginListener implements SPiDAuthorizationListener {
        private final String mUsername;

        private LoginListener(String username) {
            mUsername = username;
        }

        @Override
        public void onComplete() {
            SimpleLog.d("LoginActivity onComplete userId=%s", SPiDClient.getInstance().getAccessToken().getUserID());

            Account account = null;
            if (getIntent().getBooleanExtra(KEY_IS_ADDING_NEW_ACCOUNT, false)) {
                SimpleLog.d("LoginActivity onComplete adding new account");

                account = new Account(mUsername, mAccountType);
                mAccountManager.addAccountExplicitly(account, null, null);
            } else {
                SimpleLog.d("LoginActivity onComplete existing account");

                Account[] accounts = mAccountManager.getAccountsByType(mAccountType);
                for (Account a : accounts) {
                    if (a.name.equals(mUsername)) {
                        account = a;
                    }
                }
            }

            invokeGetAuthToken(account);
        }

        @Override
        public void onSPiDException(SPiDException exception) {
            onError(exception);
        }

        @Override
        public void onIOException(IOException exception) {
            onError(exception);
        }

        @Override
        public void onException(Exception exception) {
            onError(exception);
        }

        private void finishAddAccount(final Bundle authTokenResult, final String userId) {
            setAccountAuthenticatorResult(authTokenResult);

            Account[] accounts = mAccountManager.getAccountsByType(mAccountType);

            if (accounts != null && accounts.length > 0) {
                mAccountManager.setUserData(accounts[0], ACCOUNT_SPID_ID, userId);
            }

            final Intent resultIntent = new Intent();
            resultIntent.putExtras(authTokenResult);

            setResult(RESULT_OK, resultIntent);

            LoginActivity.this.finish();
        }

        private void invokeGetAuthToken(Account account) {
            if (account != null) {
                mAccountManager.getAuthToken(
                        account,
                        mAuthTokenType,
                        null,
                        LoginActivity.this,
                        new GetTokenCallback(),
                        null
                );
            }
        }
    }
}
