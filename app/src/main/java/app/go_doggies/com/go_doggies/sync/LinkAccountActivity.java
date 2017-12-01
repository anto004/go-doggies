package app.go_doggies.com.go_doggies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.go_doggies.com.go_doggies.R;

public class LinkAccountActivity extends AppCompatActivity {

    private static final String LOG_TAG = LinkAccountActivity.class.getSimpleName();
    private AccountManager mAccountManager;
    private String authToken = null;
    private Account mConnectedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_account);

        final String accountType = getString(R.string.accountType);
        final String authTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;

        mAccountManager = AccountManager.get(this);

        getAuthTokenForAccountCreated(accountType, authTokenType);

        //set on click listener here
        Button syncNow = (Button) findViewById(R.id.sync_now);
        syncNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mConnectedAccount != null) {
                    Toast.makeText(getBaseContext(), "Account Name: " + mConnectedAccount.name, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getBaseContext(), "Please Create Account!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getAuthTokenForAccountCreated(final String accountType, final String authTokenType){
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
                        Bundle bundle = null;
                        try {
                            bundle = accountManagerFuture.getResult();
                            authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                            if(authToken != null){ // if it has a token get the account details
                                String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                                mConnectedAccount = new Account(accountName, accountType);
                            }

                            showMessage(authToken != null ? "\n Token: " + authToken : "Fail Token Retrieval" );

                        }catch(Exception e){
                            e.printStackTrace();
                            showMessage(e.getMessage());
                        }

                    }
                }, null);
    }

    private void showMessage(final String msg){
        if(msg == null || msg.trim().equals(""))
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
