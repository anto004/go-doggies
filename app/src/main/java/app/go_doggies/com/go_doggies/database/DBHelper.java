package app.go_doggies.com.go_doggies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by anto004 on 9/12/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_FILENAME = "go_doggies.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_FILENAME, null, DB_VERSION); // takes 1 constructor arg but calls the super with 4 arg
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ItemsTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(ItemsTable.SQL_DELETE);
        // assuming there is a previous version
        // export the data to json file, drop the table, create and import the json data
        // for this purpose right now, just wipe out and create a new Table
        onCreate(sqLiteDatabase);
    }
}
