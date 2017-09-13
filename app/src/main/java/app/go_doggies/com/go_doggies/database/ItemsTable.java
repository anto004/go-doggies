package app.go_doggies.com.go_doggies.database;

/**
 * Created by anto004 on 9/12/17.
 */

public class ItemsTable{
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ID = "groomerId";
    public static final String COLUMN_NAIL_TRIM = "nailTrim";
    public static final String COLUMN_NAIL_GRIND = "nailGrind";

    public static final String[] ALL_COLUMNS = {
            COLUMN_ID, COLUMN_NAIL_TRIM, COLUMN_NAIL_GRIND};

    //match the type with the model data types
    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_ITEMS + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_NAIL_TRIM + " TEXT," +
                    COLUMN_NAIL_GRIND + " TEXT" +");";

    public static final String SQL_DELETE =
            "DROP TABLE " + TABLE_ITEMS + ";";
}
