package app.go_doggies.com.go_doggies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import app.go_doggies.com.go_doggies.utils.PollingCheck;

public class TestUtilities extends AndroidTestCase {
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014
    static final int TEST_CLIENT_ID_1 = 600;
    static final int TEST_CLIENT_ID_2 = 601;

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createItemValues() {
        ContentValues cv = new ContentValues();
        cv.put(DoggieContract.TableItems.COLUMN_NAIL_TRIM, "10");
        cv.put(DoggieContract.TableItems.COLUMN_NAIL_GRIND,"14");
        cv.put(DoggieContract.TableItems.COLUMN_EAR_CLEANING, "15");
        cv.put(DoggieContract.TableItems.COLUMN_PAW_TRIM, "12");
        cv.put(DoggieContract.TableItems.COLUMN_SANITARY_TRIM, "20");
        cv.put(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO, "12");

        return cv;
    }

    static ContentValues createClientValues() {
        ContentValues cv = new ContentValues();
        cv.put(DoggieContract.ClientEntry.COLUMN_CLIENT_ID, TEST_CLIENT_ID_2);
        cv.put(DoggieContract.ClientEntry.COLUMN_TYPE, "client");
        cv.put(DoggieContract.ClientEntry.COLUMN_COMMENT, "My dog likes treats after grooming");
        cv.put(DoggieContract.ClientEntry.COLUMN_NAME, "Antonio");
        cv.put(DoggieContract.ClientEntry.COLUMN_IMAGE, "img.jpeg");
        cv.put(DoggieContract.ClientEntry.COLUMN_PHONE, "213-352-3144");

        return cv;
    }

    static ContentValues[] createMultipleClientValues(){
        ContentValues[] cv = new ContentValues[2];
        cv[0] = new ContentValues();
        cv[0].put(DoggieContract.ClientEntry.COLUMN_CLIENT_ID, TEST_CLIENT_ID_1);
        cv[0].put(DoggieContract.ClientEntry.COLUMN_TYPE, "client");
        cv[0].put(DoggieContract.ClientEntry.COLUMN_COMMENT, "My dog is very excited");
        cv[0].put(DoggieContract.ClientEntry.COLUMN_NAME, "Jane");
        cv[0].put(DoggieContract.ClientEntry.COLUMN_IMAGE, "img.jpeg");
        cv[0].put(DoggieContract.ClientEntry.COLUMN_PHONE, "213-345-2345");

        cv[1] = new ContentValues();
        cv[1].put(DoggieContract.ClientEntry.COLUMN_CLIENT_ID, TEST_CLIENT_ID_2);
        cv[1].put(DoggieContract.ClientEntry.COLUMN_TYPE, "client");
        cv[1].put(DoggieContract.ClientEntry.COLUMN_COMMENT, "My dog likes treats after grooming");
        cv[1].put(DoggieContract.ClientEntry.COLUMN_NAME, "Antonio");
        cv[1].put(DoggieContract.ClientEntry.COLUMN_IMAGE, "img.jpeg");
        cv[1].put(DoggieContract.ClientEntry.COLUMN_PHONE, "213-352-3144");

        return cv;
    }

    static ContentValues createDogValues() {
        ContentValues cv = new ContentValues();
        cv.put(DoggieContract.DogEntry.COLUMN_CLIENT_KEY, 601);
        cv.put(DoggieContract.DogEntry.COLUMN_DOG_ID, 301);
        cv.put(DoggieContract.DogEntry.COLUMN_NAME, "Pebbles");
        cv.put(DoggieContract.DogEntry.COLUMN_IMAGE, "image.jpeg");
        cv.put(DoggieContract.DogEntry.COLUMN_SIZE, "small");
        cv.put(DoggieContract.DogEntry.COLUMN_HAIR_TYPE, "short");
        return cv;
    }

    static final int BULK_INSERT_NUMBER = 10;
    static ContentValues[] createBulkItemInsert(){
        ContentValues[] cv = new ContentValues[BULK_INSERT_NUMBER];
        for(int i = 0; i < BULK_INSERT_NUMBER; i++){
            cv[i] = new ContentValues();
            cv[i].put(DoggieContract.TableItems.COLUMN_NAIL_TRIM, String.valueOf(10 + (i/2)));
            cv[i].put(DoggieContract.TableItems.COLUMN_NAIL_GRIND,String.valueOf(14 + (i/2)));
            cv[i].put(DoggieContract.TableItems.COLUMN_EAR_CLEANING, String.valueOf(15 + (i/2)));
            cv[i].put(DoggieContract.TableItems.COLUMN_PAW_TRIM, String.valueOf(12 + (i/2)));
            cv[i].put(DoggieContract.TableItems.COLUMN_SANITARY_TRIM, String.valueOf(20 + (i/2)));
            cv[i].put(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO, String.valueOf(12 + (i/2)));
        }
        return cv;
    }

    static long insertItemValues(Context context){
        SQLiteDatabase database = new DoggieDbHelper(context).getWritableDatabase();
        ContentValues values = createItemValues();
        long rowId = database.insert(DoggieContract.TableItems.TABLE_NAME,
                null,
                values);
        // Verify we got a row back.
        assertTrue("Error: Failure to insert Item Values", rowId != -1);
        return rowId;
    }

    public void testInsertClientValues(){
        //create a fresh database
        testDb();

        SQLiteDatabase database = new DoggieDbHelper(mContext).getWritableDatabase();
        ContentValues values = createClientValues();
        long rowId = database.insert(DoggieContract.ClientEntry.TABLE_NAME,
                null,
                values);
        // Verify we got a row back.
        assertTrue("Error: Failure to insert Client Values", rowId != -1);
    }

    public void testInsertDogValues(){
        //first Insert client Values
        SQLiteDatabase database = new DoggieDbHelper(mContext).getWritableDatabase();
        ContentValues values = createDogValues();
        long rowId = database.insert(DoggieContract.DogEntry.TABLE_NAME,
                null,
                values);
        // Verify we got a row back.
        assertTrue("Error: Failure to insert Dog Values", rowId != -1);
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }

    public void testDb(){
        deleteTheDatabase();
        DoggieDbHelper dbHelper = new DoggieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        assertTrue("ERROR: CREATING TABLE", db.isOpen());
        db.close();
    }

    void deleteTheDatabase() {
        mContext.deleteDatabase(DoggieDbHelper.DB_FILENAME);
    }

}
