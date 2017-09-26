package app.go_doggies.com.go_doggies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by anto004 on 9/12/17.
 */

public class DoggieDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = "Database";
    public static final String DB_FILENAME = "go_doggies.db";
    public static final int DB_VERSION = 1;

    public DoggieDbHelper(Context context) {
        super(context, DB_FILENAME, null, DB_VERSION); // takes 1 constructor arg but calls the super with 4 arg
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //match the type with the model data types
        final String SQL_CREATE =
                "CREATE TABLE " + DoggieContract.TableItems.TABLE_NAME +
                        "(" +
                        DoggieContract.TableItems._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DoggieContract.TableItems.COLUMN_NAIL_TRIM + " TEXT," +
                        DoggieContract.TableItems.COLUMN_NAIL_GRIND + " TEXT," +
                        DoggieContract.TableItems.COLUMN_EAR_CLEANING + " TEXT," +
                        DoggieContract.TableItems.COLUMN_PAW_TRIM + " TEXT," +
                        DoggieContract.TableItems.COLUMN_SANITARY_TRIM + " TEXT," +
                        DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO + " TEXT" +
                        ");";
        Log.v(LOG_TAG, "SQL Statement: " + SQL_CREATE);

        sqLiteDatabase.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        final String SQL_DELETE =
                "DROP TABLE " + DoggieContract.TableItems.TABLE_NAME + ";";
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DoggieContract.TableItems.TABLE_NAME);
        // assuming there is a previous version
        // export the data to json file, drop the table, create and import the json data
        // For this purpose right now, just wipe out and create a new Table
        onCreate(sqLiteDatabase);
    }
}
