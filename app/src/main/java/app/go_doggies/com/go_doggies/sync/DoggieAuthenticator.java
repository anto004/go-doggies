package app.go_doggies.com.go_doggies.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by anto004 on 10/12/17.
 */

public class DoggieAuthenticator extends AbstractAccountAuthenticator {

    public static final String LOG_TAG = "DoggieAuthenticator";
    private final Context mContext;

    public DoggieAuthenticator(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * If the authenticator needs information from the user
     * to satisfy the request then it will create an Intent to an activity (DoggieAuthAcitivity)
     * with intent accountAuthenticatorResponse
     * which will prompt the user for the information and then carry out the request.
     * This intent must be returned in a Bundle as key KEY_INTENT.
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse, String accountType,
                             String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.d(LOG_TAG, "Authenticator, addAccount called");

        Intent intent = new Intent(mContext, DoggieAuthActivity.class);
        intent.putExtra(DoggieAuthActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(DoggieAuthActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(DoggieAuthActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        //If AbstractAccountAuthenticator needs to use an DoggieAuthActivity to handle the request
        //It passes in the response as an intent to the AccountAuthenticatorActivity
        //AccountAuthenticatorAcitivity will set the result via setAccountAuthenticatorResult
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                                accountAuthenticatorResponse);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse, String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.v(LOG_TAG, "getAuthToken called");

        //If the caller requested an authToken type we don't support, return an error
        if(!authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE)) {
            Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return result;
        }

        //Get the authToken from the server by signing in
        AccountManager accountManager = AccountManager.get(mContext);
        //Check the cache in accountManager for the token
        String authToken = accountManager.peekAuthToken(account, authTokenType);
        //String userId = null;

        Log.v(LOG_TAG, "peekAuthToken: "+authToken);

        // Test for changed password
        // Invalidate AuthToken
        // checking for not empty authToken as well
        // empty for first time
        // not empty for Re-authenticating

        //If not present in cache
        if(TextUtils.isEmpty(authToken)){
            String password = accountManager.getPassword(account);
            if(password != null){
                try {
                    Log.v(LOG_TAG, "Re-authenticating with the existing password, to be implemented");
//                    User user = sServerAuthenticate.userSignIn(account.name, password, authTokenType);
//                    if (user != null) {
//                        authToken = user.getSessionToken();
//                        userId = user.getObjectId();
//                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        //If we get an authToken we return it
        if(!TextUtils.isEmpty(authToken)){
            Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);

            return result;
        }

        //If we reach here we couldn't authenticate with the user's password
        //Re-prompt for the credentials through Authenticator's Activity
        Intent intent = new Intent(mContext, DoggieAuthActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
                accountAuthenticatorResponse);
        intent.putExtra(DoggieAuthActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(DoggieAuthActivity.ARG_AUTH_TYPE, authTokenType);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if(AccountGeneral.AUTHTOKEN_TYPE.equals(authTokenType))
            return AccountGeneral.AUTHTOKEN_TYPE_LABEL;
        else
            return authTokenType + " (Label)";
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse, Account account, String[] strings) throws NetworkErrorException {
        throw new UnsupportedOperationException();
    }
}
