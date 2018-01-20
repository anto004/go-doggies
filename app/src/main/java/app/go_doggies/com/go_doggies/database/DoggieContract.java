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
    public static final String PATH_DOG = "dog";
    public static final String PATH_CLIENT = "client";

    public static class DogEntry implements BaseColumns{
        //content uri for dog
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DOG).build();
        //cursor
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOG;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOG;

        //appends it to the path: authority/path/id
        public static Uri buildDogUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildClientWithDog(String client){
            return CONTENT_URI.buildUpon().appendPath(client).build();
        }

        //Base columns will provide an ID with column _id
        public static final String TABLE_NAME = "dog";
        public static final String COLUMN_DOG_ID = "dog_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_HAIR_TYPE = "hair_type";
        public static final String COLUMN_CLIENT_KEY = "client_id";
    }

    public static class ClientEntry implements BaseColumns{
        //content uri for client
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLIENT).build();
        //cursor
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLIENT;

        public static Uri buildClientUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //Base Column will provide an ID column _id
        public static final String TABLE_NAME = "client";
        public static final String COLUMN_ID = "client_id";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_PHONE = "phone";


    }

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

        public static final String TABLE_NAME = "items";
        public static final String COLUMN_GROOMER_ID = "groomerId";
        public static final String COLUMN_NAIL_TRIM = "nailTrim";
        public static final String COLUMN_NAIL_GRIND = "nailGrind";
        public static final String COLUMN_EAR_CLEANING = "earCleaning";
        public static final String COLUMN_PAW_TRIM = "pawTrim";
        public static final String COLUMN_SANITARY_TRIM = "sanitaryTrim";
        public static final String COLUMN_FLEA_SHAMPOO = "fleaShampoo";

        //id would be the row number
        public static Uri buildItemsrUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
