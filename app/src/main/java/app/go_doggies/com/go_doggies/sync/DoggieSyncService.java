package app.go_doggies.com.go_doggies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by anto004 on 10/12/17.
 */

public class DoggieSyncService extends Service {
    public static final String LOG_TAG = DoggieSyncService.class.getSimpleName();

    private static final Object sSyncAdapterLock = new Object();
    private static DoggieSyncAdapter mSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.v(LOG_TAG, "onCreate DoggieSyncService called");
        synchronized (sSyncAdapterLock){
            if(sSyncAdapterLock == null){
                mSyncAdapter = new DoggieSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        /*
            SyncAdapter component has a binder object which will provide the framework
            invoke onPerformSync
         */
        return mSyncAdapter.getSyncAdapterBinder();
    }
}
