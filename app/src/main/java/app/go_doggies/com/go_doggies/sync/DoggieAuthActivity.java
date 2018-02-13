package app.go_doggies.com.go_doggies.sync;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.List;

import app.go_doggies.com.go_doggies.R;

/**
 * Created by anto004 on 10/13/17.
 */

public class DoggieAuthActivity extends AccountAuthenticatorActivity {
    /* Authenticator sends response to AuthenticatorAcitivity and
    *  AuthenticatorActivity sends results back to Authenticator
    *  by setting setAccountAuthenticatorResult on finish
    */
    public static final String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String ARG_AUTH_TYPE = "AUTH_TYPE";
    public static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
    public static final String PARAM_USER_PASS = "USER_PASS";

    public static final String LOG_TAG = DoggieAuthActivity.class.getSimpleName();

    private AccountManager mAccountManager;
    private String mAuthTokenType;
    private Context mContext;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.i(LOG_TAG, "Doggie Authenticator Activity, called");

        mContext = getBaseContext();

        //No Action Bar

        setContentView(R.layout.activity_login);

        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);

        if(mAuthTokenType == null){
            // Type of AuthToken
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE;
        }
        // if account exists
        if(accountName != null){
            ((EditText) findViewById(R.id.username_text)).setText(accountName);
        }

        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.account_login_layout);
        //Remove cursor highlight from EditText
        //Issue: Keyboard dosn't hide on single click but double click
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.requestFocus();
                linearLayout.clearFocus();
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
            }
        });

        findViewById(R.id.sign_in_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = ((EditText)findViewById(R.id.username_text)).getText().toString();
                        String password = ((EditText)findViewById(R.id.password_text)).getText().toString();
                        String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);

                        String[] credentials = {username, password, accountType};

                        LoginTask loginTask = new LoginTask();
                        loginTask.execute(credentials);
                    }
                });
    }


    @Override
    public void finish() {
        super.finish();
    }

    private class LoginTask extends AsyncTask<String, Void, Intent> {
        final String ERROR = "Your Email and Password combination is not valid, please try again !";

        @Override
        protected Intent doInBackground(String... params) {
            //Testing
            final String username = "test@go-doggies.com";
            final String password = "2016";
//            String username = params[0];
//            String password = params[1];
            String accountType = params[2];
            Log.v(LOG_TAG, "username: "+username + "password: "+password+ " accountType: "+accountType);

            ServerAuthenticate serverAuthenticate = new ServerAuthenticate(mContext);
            String response = serverAuthenticate.signIn(username, password);

            //Or just return the authToken as a response
            CookieManager cookieManager = (CookieManager) CookieHandler.getDefault();
            List<HttpCookie> cookie = cookieManager.getCookieStore().getCookies();
            String authToken = TextUtils.join(";", cookie);


            Bundle data = new Bundle();
            data.putString(AccountManager.KEY_ACCOUNT_NAME, username);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
            data.putString(PARAM_USER_PASS, password);
            data.putString(AccountManager.KEY_AUTHTOKEN, authToken);

            // We keep the user's object id as an extra data on the account.
            // It's used later for determine ACL for the data we send to the Parse.com service
//            Bundle userData = new Bundle();
//            userData.putString(USERDATA_USER_OBJ_ID, user.getObjectId());
//            data.putBundle(AccountManager.KEY_USERDATA, userData);

            // add userData in the bundle

            if(response == null || response.contains("not valid") ||
                    response.equals(ERROR)){
                data.putString(KEY_ERROR_MESSAGE, response);
            }

            Intent result = new Intent();
            result.putExtras(data); // Bundle as extras in Intent

            return result;
        }

        @Override
        protected void onPostExecute(Intent intent) {
            if(intent.hasExtra(KEY_ERROR_MESSAGE)){
                Toast.makeText(mContext, intent.getStringExtra(KEY_ERROR_MESSAGE),
                        Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(mContext, "Login Success", Toast.LENGTH_SHORT).show();
                finishLogin(intent);
            }

        }
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountType = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);

        Account account = new Account(accountName, accountType);

        if(getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false) == true){
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            Log.v(LOG_TAG, "creating new account with authToken: "+authtoken);

            // Creating an account and setting the auth token,
            // not setting it will cause the framework to make another authentication
            // add userdata Bundle in Bundle param
            mAccountManager.addAccountExplicitly(account, accountPassword, new Bundle());
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        }
        else{
            mAccountManager.setPassword(account, accountPassword);
        }
        //This result will be sent as the result of the request from AccountAbstractAuthenticator when this activity finishes.
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
