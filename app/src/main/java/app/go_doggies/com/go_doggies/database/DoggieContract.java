package app.go_doggies.com.go_doggies.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by anto004 on 9/12/17.
 */

public class DoggieContract {
    public static final String CONTENT_AUTHORITY = "app.go_doggies.com.go_doggies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEMS = "items";


    public static class TableItems implements BaseColumns{
        // BaseColumns will provide _ID column
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEMS).build();

        //Constant Value: "vnd.android.cursor.dir"
        // vnd.android.cursor.dir/app.go_doggies.com.go_doggies/items
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        //Constant Value: "vnd.android.cursor.item"
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        //id would be the row number
        public static Uri buildItemsrUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = "items";
        public static final String COLUMN_GROOMER_ID = "groomerId";
        public static final String COLUMN_NAIL_TRIM = "nailTrim";
        public static final String COLUMN_NAIL_GRIND = "nailGrind";
        public static final String COLUMN_EAR_CLEANING = "earCleaning";
        public static final String COLUMN_PAW_TRIM = "pawTrim";
        public static final String COLUMN_SANITARY_TRIM = "sanitaryTrim";
        public static final String COLUMN_FLEA_SHAMPOO = "fleaShampoo";
    }


}
