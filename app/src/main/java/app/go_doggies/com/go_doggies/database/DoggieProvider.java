package app.go_doggies.com.go_doggies.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by anto004 on 9/13/17.
 */

public class DoggieProvider extends ContentProvider {
    // do not close database in the provider

    private static final UriMatcher sURIMatcher = buildUriMatcher();
    private DoggieDbHelper mOpenHelper;

    private static final int ITEMS = 100;

    static UriMatcher buildUriMatcher(){
        //* -> String , # -> numbers
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = DoggieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, DoggieContract.PATH_ITEMS, ITEMS);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DoggieDbHelper(getContext());
        return true;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(sURIMatcher.match(uri)){
            case ITEMS:
                return DoggieContract.TableItems.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        switch(sURIMatcher.match(uri)){
            case ITEMS:
                return mOpenHelper.getReadableDatabase().query(
                        DoggieContract.TableItems.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
