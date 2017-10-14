package app.go_doggies.com.go_doggies.sync;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import app.go_doggies.com.go_doggies.R;

/**
 * Created by anto004 on 10/13/17.
 */

public class DoggieAuthAcitivity extends AccountAuthenticatorActivity {
    /* Authenticator sends response to AuthenticatorAcitivity and
    *  AuthenticatorActivity sends results back to Authenticator
    *  by setting AccountAuthenticatorResult on finish
    */
    public static final String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String ARG_AUTH_TYPE = "AUTH_TYPE";
    public static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";
    public static final String PARAM_USER_PASS = "USER_PASS";

    public static final String LOG_TAG = DoggieAuthAcitivity.class.getSimpleName();

    private AccountManager mAccountManager;
    private String mAuthTokenType;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mContext = getBaseContext();

        //No Action Bar

        setContentView(R.layout.account_login);
        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);

        if(mAuthTokenType == null){
            mAuthTokenType = getBaseContext().getString(R.string.authtoken_type_full_access);
        }
        // if account exists
        if(accountName != null){
            ((TextView) findViewById(R.id.username_text))
                    .setText(accountName);
        }

        findViewById(R.id.login_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = ((TextView)findViewById(R.id.username_text)).getText().toString();
                        String password = ((TextView)findViewById(R.id.password_text)).getText().toString();
                        String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE)''
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
//            final String username = "test@go-doggies.com";
//            final String password = "2016";
            String username = params[0];
            String password = params[1];
            String accountType = params[2];
            Log.v(LOG_TAG, "username: "+username + "password: "+password+ " accountType: "+accountType);

            Bundle data = new Bundle();

            String response = ServerAuthenticate.signIn(username, password);

            data.putString(AccountManager.KEY_ACCOUNT_NAME, username);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
            //data.putString(AccountManager.KEY_AUTHTOKEN, user.getSessionToken());
            data.putString(PARAM_USER_PASS, password);

            // add userData in the bundle

            if(response == null || response.contains("not valid") ||
                    response.equals(ERROR)){
                data.putString(KEY_ERROR_MESSAGE, response);

            }


            Intent result = new Intent();
            result.putExtras(data);

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
        Log.v(LOG_TAG, "finishLogin");
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        Account account = new Account(accountName,
                intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if(getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)){
            Log.v(LOG_TAG, "creating new account");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            //Creating an account and setting the auth token,
            // not setting it will cause the framework to make another authentication

            // add userdata Bundle in Bundle param
            mAccountManager.addAccountExplicitly(account, accountPassword, new Bundle());
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        }
        else{
            mAccountManager.setPassword(account, accountPassword);
        }
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }
}
