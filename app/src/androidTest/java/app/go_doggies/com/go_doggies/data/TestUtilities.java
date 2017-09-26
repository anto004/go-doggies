package app.go_doggies.com.go_doggies.data;

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

import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.database.DoggieDbHelper;
import app.go_doggies.com.go_doggies.utils.PollingCheck;

public class TestUtilities extends AndroidTestCase {
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

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
}
