package app.go_doggies.com.go_doggies.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static app.go_doggies.com.go_doggies.database.DoggieDbHelper.LOG_TAG;

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
                Cursor cursor = mOpenHelper.getReadableDatabase().query(
                        DoggieContract.TableItems.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                // loaders wont update changes if this is not set
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri returnUri;
        switch(sURIMatcher.match(uri)){
            case ITEMS:
                // returns the row ID inserted
                long rowId = mOpenHelper.getWritableDatabase().insert(
                        DoggieContract.TableItems.TABLE_NAME,
                        null,
                        contentValues
                );
                if(rowId >= 0) {
                    Log.v(LOG_TAG, "INSERTED at _ID: "+rowId+ " into TABLE: "+ DoggieContract.TableItems.TABLE_NAME);
                    returnUri = DoggieContract.TableItems.buildItemsrUri(rowId);
                }
                else
                    throw new android.database.SQLException("Failed to Insert to uri: "+ uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
        // notifyChange on the passed uri not the returned uri
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        if(selection == null)
            selection = "1";
        switch(sURIMatcher.match(uri)){
            case ITEMS:
                rowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        DoggieContract.TableItems.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
        Log.v(LOG_TAG, "DELETED NO OF ROWS: "+rowsDeleted);
        if(rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        int rowsUpdated;// return the number of rows updated
        switch(sURIMatcher.match(uri)){
            case ITEMS:
                rowsUpdated = mOpenHelper.getWritableDatabase().update(
                        DoggieContract.TableItems.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+ uri);
        }
        Log.v(LOG_TAG, "UPDATED ROWS: "+ rowsUpdated);
        if(rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    public int bulkInsert(Uri uri, ContentValues[] contentValues){
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count = 0;
        long rowId = -1;
        switch(sURIMatcher.match(uri)){
            case ITEMS:
                db.beginTransaction();
                try {
                    for (ContentValues value : contentValues) {
                        rowId = db.insert(
                                DoggieContract.TableItems.TABLE_NAME,
                                null,
                                value
                        );
                        if(rowId != -1) {
                            Log.v(LOG_TAG, "INSERTED at _ID: "+rowId+ " into TABLE: "+ DoggieContract.TableItems.TABLE_NAME);
                            count++;
                        }
                        else
                            throw new android.database.SQLException("Failed to Insert at Uri: "+uri);
                    }
                    db.setTransactionSuccessful();
                }
                finally{
                    db.endTransaction();
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
