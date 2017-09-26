package app.go_doggies.com.go_doggies.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.database.DoggieDbHelper;
import app.go_doggies.com.go_doggies.database.DoggieProvider;

/**
 * Created by anto004 on 9/25/17.
 */

public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = "Database";

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    /*
       Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
       you have implemented delete functionality there.
    */
    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

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

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createItemValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DoggieContract.TableItems.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(DoggieContract.TableItems.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                DoggieContract.TableItems.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ItemEntry.",
                cursor, testValues);

    }

    public void testUpdate() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createItemValues();

        Uri uri = mContext.getContentResolver().
                insert(DoggieContract.TableItems.CONTENT_URI, values);
        long rowId = ContentUris.parseId(uri);

        // Verify we got a row back.
        assertTrue(rowId != -1);
        Log.d(LOG_TAG, "New row id: " + rowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(DoggieContract.TableItems._ID, rowId);
        updatedValues.put(DoggieContract.TableItems.COLUMN_NAIL_TRIM, "50");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor cursor = mContext.getContentResolver().query(DoggieContract.TableItems.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DoggieContract.TableItems.CONTENT_URI, true, tco);
        //cursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                DoggieContract.TableItems.CONTENT_URI,
                updatedValues,
                DoggieContract.TableItems._ID + "= ?",
                new String[] { Long.toString(rowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        //cursor.unregisterContentObserver(tco);
        getContext().getContentResolver().unregisterContentObserver(tco);
        cursor.close();

        // A cursor is your primary interface to the query results.
        Cursor updatedCursor = mContext.getContentResolver().query(
                DoggieContract.TableItems.CONTENT_URI,
                null,   // projection
                DoggieContract.TableItems._ID + " = " + rowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateLocation.  Error validating item entry update.",
                updatedCursor, updatedValues);

        updatedCursor.close();
    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver locationObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DoggieContract.TableItems.CONTENT_URI, true, locationObserver);

        deleteAllRecordsFromProvider();

        mContext.getContentResolver().unregisterContentObserver(locationObserver);
    }

    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(
                DoggieContract.TableItems.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                DoggieContract.TableItems.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from Items table during delete", 0, cursor.getCount());
        cursor.close();
    }

    /*
       This helper function deletes all records from both database tables using the database
       functions only.  This is designed to be used to reset the state of the database until the
       delete functionality is available in the ContentProvider.
     */
    public void deleteAllRecordsFromDB() {
        DoggieDbHelper dbHelper = new DoggieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DoggieContract.TableItems.TABLE_NAME, null, null);
        db.close();
    }

}
