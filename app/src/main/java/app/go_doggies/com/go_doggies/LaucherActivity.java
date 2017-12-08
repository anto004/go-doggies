package app.go_doggies.com.go_doggies;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import app.go_doggies.com.go_doggies.sync.AccountGeneral;

public class LaucherActivity extends AppCompatActivity {
    private AccountManager mAccountManager;
    private Account mAccount;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //when getting a token start from the authenticator
        //call getAuthToken requires an account
        mAccountManager = AccountManager.get(getApplicationContext());
        String accountType = getString(R.string.accountType);
        String authType = AccountGeneral.AUTHTOKEN_TYPE;


        //getAuthTokenForAccount(accountType, authType);
        i = 0;



    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Restarted:" + ++i, Toast.LENGTH_SHORT).show();

        Button login = (Button) findViewById(R.id.sign_in_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRestart();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onStart();
    }

    public Account getAuthTokenForAccount(final String accountType, final String authType){
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
        return mAccount;
    }

    private void showMessage(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
