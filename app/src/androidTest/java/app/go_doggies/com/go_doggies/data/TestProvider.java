package app.go_doggies.com.go_doggies.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.database.DoggieDbHelper;
import app.go_doggies.com.go_doggies.database.DoggieProvider;

/**
 * Created by anto004 on 9/25/17.
 */

public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = "Database";

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                DoggieProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: DoggieProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + DoggieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, DoggieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: DoggieProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        // content://app.go_doggies.com.go_doggies/items/
        String type = mContext.getContentResolver().getType(DoggieContract.TableItems.CONTENT_URI);
        // vnd.android.cursor.dir/app.go_doggies.com.go_doggies/items
        assertEquals("Error: the DoggieEntry CONTENT_URI should return DoggieEntry.CONTENT_TYPE",
                DoggieContract.TableItems.CONTENT_TYPE, type);

    }

    public void testItemQuery() {
        // insert our test records into the database
        DoggieDbHelper dbHelper = new DoggieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createItemValues();
        long itemRowId = TestUtilities.insertItemValues(mContext);

        assertTrue("Unable to Insert ItemEntry into the Database", itemRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                DoggieContract.TableItems.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicItemQuery", cursor, testValues);
    }

}
