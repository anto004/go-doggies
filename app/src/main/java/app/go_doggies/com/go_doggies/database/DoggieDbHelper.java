package app.go_doggies.com.go_doggies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
                        DoggieContract.TableItems.COLUMN_GROOMER_ID + " TEXT," +
                        DoggieContract.TableItems.COLUMN_NAIL_TRIM + " TEXT," +
                        DoggieContract.TableItems.COLUMN_NAIL_GRIND + " TEXT," +
                        DoggieContract.TableItems.COLUMN_EAR_CLEANING + " TEXT," +
                        DoggieContract.TableItems.COLUMN_PAW_TRIM + " TEXT," +
                        DoggieContract.TableItems.COLUMN_SANITARY_TRIM + " TEXT," +
                        DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO + " TEXT" +
                        ");";

//        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
//                // Why AutoIncrement here, and not above?
//                // Unique keys will be auto-generated in either case.  But for weather
//                // forecasting, it's reasonable to assume the user will want information
//                // for a certain date and all dates *following*, so the forecast data
//                // should be sorted accordingly.
//                WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
//
//                // the ID of the location entry associated with this weather data
//                WeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
//                WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
//                WeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
//                WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +
//
//                WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
//                WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +
//
//                WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
//                WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
//                WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
//                WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, " +
//
//                // Set up the location column as a foreign key to location table.
//                "FOREIGN KEY (" + WeatherEntry.COLUMN_LOC_KEY + ") " +
//                "REFERENCES " + LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +
//
//                // To assure the application have just one weather entry per day
//                // per location, it's created a UNIQUE constraint with REPLACE strategy
//                "UNIQUE (" + WeatherEntry.COLUMN_DATE + ", " +
//                WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_CLIENT_TABLE = "CREATE TABLE " + DoggieContract.ClientEntry.TABLE_NAME + "(" +
                DoggieContract.ClientEntry._ID + " " + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                DoggieContract.ClientEntry.COLUMN_CLIENT_ID + " " + "INTEGER NOT NULL," +
                DoggieContract.ClientEntry.COLUMN_TYPE + " " + "TEXT," +
                DoggieContract.ClientEntry.COLUMN_COMMENT + " " + "TEXT," +
                DoggieContract.ClientEntry.COLUMN_NAME + " " + "TEXT," +
                DoggieContract.ClientEntry.COLUMN_IMAGE + " " + "TEXT," +
                DoggieContract.ClientEntry.COLUMN_PHONE + " " + "TEXT" +
                ");";

        final String SQL_CREATE_DOG_TABLE = "CREATE TABLE " + DoggieContract.DogEntry.TABLE_NAME + "(" +
                DoggieContract.DogEntry._ID + " " + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                DoggieContract.DogEntry.COLUMN_CLIENT_KEY + " " + "INTEGER NOT NULL," +
                DoggieContract.DogEntry.COLUMN_DOG_ID + " " + "INTEGER NOT NULL," +
                DoggieContract.DogEntry.COLUMN_NAME + " " + "TEXT," +
                DoggieContract.DogEntry.COLUMN_IMAGE + " " + "TEXT," +
                DoggieContract.DogEntry.COLUMN_SIZE + " " + "TEXT," +
                DoggieContract.DogEntry.COLUMN_HAIR_TYPE + " " + "TEXT," +
                "FOREIGN KEY " + "(" + DoggieContract.ClientEntry.COLUMN_CLIENT_ID + ")" + " " +
                "REFERENCES " + DoggieContract.ClientEntry.TABLE_NAME + " (" +
                DoggieContract.ClientEntry.COLUMN_CLIENT_ID + ")" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_CLIENT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DOG_TABLE);
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
