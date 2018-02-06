package app.go_doggies.com.go_doggies.database;

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

/**
 * Created by anto004 on 9/25/17.
 */

public class TestProvider extends AndroidTestCase {
    // add the delete test cases first
    public static final String LOG_TAG = "Database";

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
        db.delete(DoggieContract.ClientEntry.TABLE_NAME, null, null);
        db.delete(DoggieContract.DogEntry.TABLE_NAME, null, null);
        db.close();
    }

    /*
       Student: Refactor this function to use the deleteAllRecordsFromProvider functionality once
       you have implemented delete functionality there.
    */
    public void deleteAllRecords() {
        deleteAllRecordsFromDB();
    }

    // Since we want each test to start with a clean slate, run deleteAllRecords
    // in setUp (called by the test runner before each test).
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
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

        //test for ClientEntry
        type = mContext.getContentResolver().getType(DoggieContract.ClientEntry.CONTENT_URI);
        assertEquals("Error: the ClientEntry CONTENT_URI should return ClientEntry.CONTENT_TYPE",
                DoggieContract.ClientEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver()
                .getType(DoggieContract.ClientEntry.buildClientDetailUri(TestUtilities.TEST_CLIENT_ID_1));
        assertEquals("Error: the ClientEntry CONTENT_URI with ClientId should return ClientEntry.CONTENT_TYPE",
                DoggieContract.ClientEntry.CONTENT_TYPE, type);

        //test for DogEntry
        type = mContext.getContentResolver().getType(DoggieContract.DogEntry.CONTENT_URI);
        assertEquals("Error: the DogEntry CONTENT_URI should return DogEntry.CONTENT_TYPE",
                DoggieContract.DogEntry.CONTENT_TYPE, type);

        type = mContext.getContentResolver()
                .getType(DoggieContract.DogEntry.buildDogUriWithClientId(TestUtilities.TEST_CLIENT_ID_1));
        assertEquals("Error: the DogEntry CONTENT_URI with ClientId should return DogEntry.CONTENT_ITEM_TYPE",
                DoggieContract.DogEntry.CONTENT_ITEM_TYPE, type);

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

    public void testBasicClientQuery() {
        // insert our test records into the database
        DoggieDbHelper dbHelper = new DoggieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createClientValues();
        long itemRowId = db.insert(DoggieContract.ClientEntry.TABLE_NAME, null, testValues);

        assertTrue("Unable to Insert ClientEntry into the Database", itemRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                DoggieContract.ClientEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicClientQuery", cursor, testValues);
    }

    public void testBasicDogQuery() {
        // insert our test records into the database
        DoggieDbHelper dbHelper = new DoggieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createDogValues();
        long itemRowId = db.insert(DoggieContract.DogEntry.TABLE_NAME, null, testValues);

        assertTrue("Unable to Insert DogEntry into the Database", itemRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor cursor = mContext.getContentResolver().query(
                DoggieContract.DogEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicDogQuery", cursor, testValues);
    }

    public void testClientWithDogQuery() {
        //fresh database
        deleteAllRecordsFromDB();

        DoggieDbHelper dbHelper = new DoggieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues[] clientValues = TestUtilities.createMultipleClientValues();
        for(int i = 0; i < clientValues.length; i++) {
            long itemRowId = db.insert(DoggieContract.ClientEntry.TABLE_NAME, null, clientValues[i]);

            assertTrue("Unable to Insert ClientEntry into the Database", itemRowId != -1);
        }

        ContentValues[] dogValues= TestUtilities.createMultipleDogValues();
        for(int i = 0; i < dogValues.length; i++) {
            long itemRowId = db.insert(DoggieContract.DogEntry.TABLE_NAME, null, dogValues[i]);

            assertTrue("Unable to Insert DogEntry into the Database", itemRowId != -1);
        }
        db.close();

        Uri clientWithDogUri = DoggieContract.ClientEntry.buildClientDetailUri(TestUtilities.TEST_CLIENT_ID_2);
        Cursor cursor = mContext.getContentResolver().query(
                clientWithDogUri,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        //test for CLIENT_ID_2 = "601"
        //inserted values client table         dog table
        //row1 -> client_id(600)            row1 -> dog_id(300) -> client_id(601)
        //row2 -> client_id(601)            row2 -> dog_id(301) -> client_id(601)
        //                                  row3 -> dog_id(302) -> client_id(600)
        //query for client_id(601) should return two joined rows of client_id(601) with dog 300,301
        ContentValues[] clientDogValues = TestUtilities.createClientDogValues();

        TestUtilities.validateCursorWithMultipleRows("testClientWithDogQuery", cursor, clientDogValues);
    }

    public void testDogsOfClientQuery() {
        //fresh database
        deleteAllRecordsFromDB();

        DoggieDbHelper dbHelper = new DoggieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues[] dogValues= TestUtilities.createMultipleDogValues();
        for(int i = 0; i < dogValues.length; i++) {
            long itemRowId = db.insert(DoggieContract.DogEntry.TABLE_NAME, null, dogValues[i]);

            assertTrue("Unable to Insert DogEntry into the Database", itemRowId != -1);
        }
        db.close();

        Uri dogUriWithClientId = DoggieContract.DogEntry.buildDogUriWithClientId(TestUtilities.TEST_CLIENT_ID_2);
        Cursor cursor = mContext.getContentResolver().query(
                dogUriWithClientId,
                null,
                null,
                null,
                DoggieContract.DogEntry.COLUMN_NAME + " DESC"
        );

        TestUtilities.validateCursorWithMultipleRows("testDogOfClientQuery", cursor, dogValues);
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

    public void testInsertClientProvider() {
        ContentValues clientValues = TestUtilities.createClientValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DoggieContract.ClientEntry.CONTENT_URI, true, tco);

        Uri clientUri = mContext.getContentResolver().insert(DoggieContract.ClientEntry.CONTENT_URI, clientValues);

        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long clientId = ContentUris.parseId(clientUri);
        // Verify we got a row back.
        assertTrue(clientId != -1);

        Cursor cursor = mContext.getContentResolver().query(
                DoggieContract.ClientEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ClientEntry.",
                cursor, clientValues);

        //after inserting client let's insert doggies woof woof
        ContentValues dogValues = TestUtilities.createDogValues(clientId);
        Uri dogUri = mContext.getContentResolver().insert(DoggieContract.DogEntry.CONTENT_URI, dogValues);

        long dogRowId = ContentUris.parseId(dogUri);

        assertTrue(dogRowId != -1);

        //after inserting client and dog values, lets query the joined tables
        Uri clientWithDogsUri = DoggieContract.ClientEntry.buildClientDetailUri(clientId);
        Cursor dogCursor = mContext.getContentResolver().query(
                clientWithDogsUri,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // sort order
        );

        clientValues.putAll(dogValues);

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ClientEntry with DogEntry.",
                dogCursor, clientValues);

    }

    public void testDelete(){
        testInsertClientProvider();
        //inserted Antonio in client with clientId = CLIENT_ID_2
        //inserted Pebbles in dog

        //Register a ContentObserver for our dogs delete
        TestUtilities.TestContentObserver dogObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DoggieContract.DogEntry.CONTENT_URI, true,
                dogObserver);

        int dogRowsDeleted = mContext.getContentResolver().delete(
                DoggieContract.DogEntry.buildDogUriWithClientId(TestUtilities.TEST_CLIENT_ID_2),
                null,
                null
        );

        assertFalse("Error: Record not deleted from dog table", dogRowsDeleted == 0);
        Cursor dogCursor = mContext.getContentResolver().query(
                DoggieContract.DogEntry.buildDogUriWithClientId(TestUtilities.TEST_CLIENT_ID_2),
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Record not deleted from dog table", 0, dogCursor.getCount());
        dogCursor.close();


        //Register a ContentObserver for our client delete
        TestUtilities.TestContentObserver clientObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DoggieContract.ClientEntry.CONTENT_URI, true,
                clientObserver);

        int clientRowsDeleted = mContext.getContentResolver().delete(
                DoggieContract.ClientEntry.buildClientDetailUri(TestUtilities.TEST_CLIENT_ID_2),
                null,
                null
        );

        assertFalse("Error: Record not deleted from client table", clientRowsDeleted == 0);
        Cursor clientCursor = mContext.getContentResolver().query(
                DoggieContract.ClientEntry.buildClientDetailUri(TestUtilities.TEST_CLIENT_ID_2),
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Record not deleted from client table", 0, dogCursor.getCount());
        clientCursor.close();

        dogObserver.waitForNotificationOrFail();
        clientObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(dogObserver);
        mContext.getContentResolver().unregisterContentObserver(clientObserver);

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


    public void testBulkInsert() {

        // Now we can bulkInsert some items.  In fact, we only implement BulkInsert for item
        // entries.  With ContentProviders, you really only have to implement the features you
        // use, after all.
        ContentValues[] bulkInsertContentValues = TestUtilities.createBulkItemInsert();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver observer = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(DoggieContract.TableItems.CONTENT_URI, true, observer);

        int insertCount = mContext.getContentResolver().bulkInsert(DoggieContract.TableItems.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        observer.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(observer);

        assertEquals(insertCount, TestUtilities.BULK_INSERT_NUMBER);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                DoggieContract.TableItems.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                DoggieContract.TableItems._ID + " ASC"  // sort order == by DATE ASCENDING
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), TestUtilities.BULK_INSERT_NUMBER);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for ( int i = 0; i < TestUtilities.BULK_INSERT_NUMBER; i++, cursor.moveToNext() ) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating ItemEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }

}
