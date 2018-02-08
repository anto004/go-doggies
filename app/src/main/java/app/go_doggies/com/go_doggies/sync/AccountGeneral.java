package app.go_doggies.com.go_doggies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.os.Bundle;

import app.go_doggies.com.go_doggies.R;

/**
 * Created by anto004 on 10/17/17.
 */

public class AccountGeneral {

    //public static final String USERDATA_USER_OBJ_ID = "userObjectId";

    /**
     * Auth token types
     */

    public static final String AUTHTOKEN_TYPE = "Full Access";
    public static final String AUTHTOKEN_TYPE_LABEL = "Full access to a go_doggie account";

    public static String updateToken(Activity activity){
        AccountManager accountManager = AccountManager.get(activity);
        Account[] accounts = accountManager.getAccounts();
        String accountType = activity.getString(R.string.accountType);
        String authToken = null;
        try {
            if(accounts[0] == null){
                throw new Exception("No Account Exists");
            }
            AccountManagerFuture<Bundle> future = accountManager.getAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE, null, activity,
                    null, null);

            Bundle result = future.getResult();
            authToken = result.getString(AccountManager.KEY_AUTHTOKEN);

            accountManager.invalidateAuthToken(accountType, authToken);

            future = accountManager.getAuthToken(accounts[0], AccountGeneral.AUTHTOKEN_TYPE, null, activity,
                    null, null);

            result = future.getResult();
            authToken = result.getString(AccountManager.KEY_AUTHTOKEN);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return authToken;
    }

}
