package app.go_doggies.com.go_doggies;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import app.go_doggies.com.go_doggies.sync.AccountGeneral;
import app.go_doggies.com.go_doggies.sync.MyCookieStore;

public class LaucherActivity extends AppCompatActivity {
    public static final String LOG_TAG = LauncherActivity.class.getSimpleName();
    private AccountManager mAccountManager;
    private Account mAccount;
    public static final int LAUNCHER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Toolbar toolbar = (Toolbar) findViewById(R.id.go_doggie_toolbar);
        setSupportActionBar(toolbar);

        final String accountType = getString(R.string.accountType);
        final String authType = AccountGeneral.AUTHTOKEN_TYPE;
        String authToken = null;

        mAccountManager = AccountManager.get(getApplicationContext());
        Account [] accounts = mAccountManager.getAccountsByType(accountType);
        //In case of multiple accounts show a pop up
        //onCreateDialog(savedInstanceState, names).show();
        mAccount = accounts[0];
        if(mAccount != null) {
            authToken = mAccountManager.peekAuthToken(mAccount, authType);
        }

        CookieManager cookieManager = new CookieManager(new MyCookieStore(this), CookiePolicy.ACCEPT_ALL);
        //Gives null pointer on urlConnection writeOutputStream
//        CookieHandler.setDefault(cookieManager);
        //saved cookie is removed, invalidate current token
        if(cookieManager.getCookieStore().getCookies().size() == 0 && authToken != null){
            mAccountManager.invalidateAuthToken(accountType, authToken);
            authToken = null;
        }

        if(mAccount != null && authToken != null) {
            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            startActivityForResult(intent, LAUNCHER_REQUEST_CODE);
        }
        else{
            Button signIn = (Button) findViewById(R.id.sign_in_launcher_button);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getAuthTokenForAccount(accountType, authType);
                }
            });
        }

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Log.v(LOG_TAG, "Metrics: "+ metrics.toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Launcher", "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop() called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG, "onRestart() called");
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(LOG_TAG, "finish() called");

    }
    //called before onResume()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LAUNCHER_REQUEST_CODE){
            if(resultCode == LauncherActivity.RESULT_CANCELED){
                //Close the activity after returning from Dashboard
                //Sign in is not required
                finish();

            }

        }

    }

    public void getAuthTokenForAccount(final String accountType, final String authType){
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authType, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
                        try {
                            Bundle bundle = accountManagerFuture.getResult();
                            String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                            if(authToken != null){
                                String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                                mAccount = new Account(accountName, accountType);

                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                startActivityForResult(intent, LAUNCHER_REQUEST_CODE);
                            }
                            showMessage(authToken != null ? "Welcome!" : "Please Login!");
                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
    }

    private void showMessage(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Dialog onCreateDialog(Bundle savedInstanceState, final String[] accounts) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Account")
                .setItems(accounts, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Toast.makeText(getApplicationContext(), " " + accounts[which], Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();
    }
}
