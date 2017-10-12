package app.go_doggies.com.go_doggies.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by anto004 on 10/12/17.
 */

public class DoggieSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String LOG_TAG = DoggieSyncAdapter.class.getSimpleName();
    public DoggieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority,
                              ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.v(LOG_TAG, "onPerformSync called");


    }
}
