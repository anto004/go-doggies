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

    static final int ITEMS = 100;
    static final int CLIENTS = 200;
    static final int CLIENTS_DETAILS = 201;
    static final int DOGS = 300;
    static final int DOGS_WITH_CLIENT_ID = 301;

    static UriMatcher buildUriMatcher(){
        //* -> String , # -> numbers
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = DoggieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, DoggieContract.PATH_ITEMS, ITEMS);
        uriMatcher.addURI(authority, DoggieContract.PATH_CLIENT, CLIENTS);
        uriMatcher.addURI(authority, DoggieContract.PATH_CLIENT + "/*", CLIENTS_DETAILS);
        uriMatcher.addURI(authority, DoggieContract.PATH_DOG, DOGS);
        uriMatcher.addURI(authority, DoggieContract.PATH_DOG + "/*", DOGS_WITH_CLIENT_ID);
        return uriMatcher;
    }

//    private Cursor getWeatherByLocationSetting(Uri uri, String[] projection, String sortOrder) {
//        String locationSetting = WeatherContract.WeatherEntry.getLocationSettingFromUri(uri);
//        long startDate = WeatherContract.WeatherEntry.getStartDateFromUri(uri);
//
//        String[] selectionArgs;
//        String selection;
//
//        if (startDate == 0) { // if no date
//            selection = sLocationSettingSelection; // just the location
//            selectionArgs = new String[]{locationSetting}; // provide the location as an argument
//        } else {
//            selection = sLocationSettingWithStartDateSelection; // location with date
//            selectionArgs = new String[]{locationSetting, Long.toString(startDate)}; // provide location and date as strings
//        }
//
//        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
//                projection, // selected columns to display
//                selection, // where column(s) =
//                selectionArgs, // args for the where clause
//                null,
//                null,
//                sortOrder
//        );
    private Cursor getClientFromClients(Uri uri, String[] projection, String sortOrder){
        String clientId = DoggieContract.ClientEntry.getClientIdFromUri(uri);
        String selection = DoggieContract.ClientEntry.TABLE_NAME +
                "." + DoggieContract.ClientEntry.COLUMN_CLIENT_ID +
                " = ? ";
        String[] selectionArgs = {clientId};

        return mOpenHelper.getReadableDatabase().query(
                DoggieContract.ClientEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
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
            //for images return mime type
            case ITEMS:
                return DoggieContract.TableItems.CONTENT_TYPE;
            case CLIENTS:
                return DoggieContract.ClientEntry.CONTENT_TYPE;
            case CLIENTS_DETAILS:
                return DoggieContract.ClientEntry.CONTENT_ITEM_TYPE;
            case DOGS:
                return DoggieContract.DogEntry.CONTENT_TYPE;
            case DOGS_WITH_CLIENT_ID:
                return DoggieContract.DogEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch(sURIMatcher.match(uri)){
            case ITEMS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DoggieContract.TableItems.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CLIENTS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DoggieContract.ClientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case CLIENTS_DETAILS:
                retCursor = getClientFromClients(uri, projection, sortOrder);
                break;

            case DOGS:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DoggieContract.DogEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            default:
                throw new UnsupportedOperationException("Unknown Uri: "+uri);
        }
        // loaders wont update changes if this is not set
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
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
