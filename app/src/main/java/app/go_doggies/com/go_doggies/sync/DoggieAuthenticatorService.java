package app.go_doggies.com.go_doggies.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by anto004 on 10/12/17.
 */

public class DoggieAuthenticatorService extends Service {

    DoggieAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new DoggieAuthenticator(this);
    }

    // When the system binds to this service to make RPC call
    // return the authenticator's binder
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
