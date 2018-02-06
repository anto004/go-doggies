package app.go_doggies.com.go_doggies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
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
    private static final SQLiteQueryBuilder sClientWithDogQueryBuilder;
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

    static{
        sClientWithDogQueryBuilder = new SQLiteQueryBuilder();
        //client LEFT JOIN dog ON client.client_id = dog.client_id
        sClientWithDogQueryBuilder.setTables(
                DoggieContract.ClientEntry.TABLE_NAME + " " + "LEFT JOIN" + " " +
                        DoggieContract.DogEntry.TABLE_NAME + " " + "ON" + " " +
                        DoggieContract.ClientEntry.TABLE_NAME +
                        "." + DoggieContract.ClientEntry.COLUMN_CLIENT_ID +
                        " " +
                        "=" +
                        " " + DoggieContract.DogEntry.TABLE_NAME +
                        "." + DoggieContract.DogEntry.COLUMN_CLIENT_KEY
        );
    }

    private Cursor getClientWithDogs(Uri uri, String[] projection, String sortOrder){
        String clientId = DoggieContract.ClientEntry.getClientIdFromClientUri(uri);
        String selection = DoggieContract.ClientEntry.TABLE_NAME +
                "." + DoggieContract.ClientEntry.COLUMN_CLIENT_ID +
                " = ? ";
        String[] selectionArgs = {clientId};

        return sClientWithDogQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    private Cursor getDogsOfClient(Uri uri, String[] projection, String sortOrder){
        String clientId = DoggieContract.DogEntry.getClientIdFromDogUri(uri);
        String selection = DoggieContract.DogEntry.TABLE_NAME +
                "." + DoggieContract.DogEntry.COLUMN_CLIENT_KEY +
                " = ? ";
        String[] selectionArgs = {clientId};

        return mOpenHelper.getReadableDatabase().query(
                DoggieContract.DogEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private int deleteDogsOfClient(Uri uri, String selection, String[] selectionArgs){
        String clientId = DoggieContract.DogEntry.getClientIdFromDogUri(uri);
        if(selection == null) {
            selection = DoggieContract.DogEntry.TABLE_NAME +
                    "." + DoggieContract.DogEntry.COLUMN_CLIENT_KEY +
                    " = ? ";
            selectionArgs = new String[]{clientId};
        }
        return mOpenHelper.getWritableDatabase().delete(
                DoggieContract.DogEntry.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    private int deleteClient(Uri uri, String selection, String[] selectionArgs){
        String clientId = DoggieContract.ClientEntry.getClientIdFromClientUri(uri);
        if(selection == null) {
            selection = DoggieContract.ClientEntry.TABLE_NAME +
                    "." + DoggieContract.ClientEntry.COLUMN_CLIENT_ID +
                    " = ? ";
            selectionArgs = new String[]{clientId};
        }

        return mOpenHelper.getWritableDatabase().delete(
                DoggieContract.ClientEntry.TABLE_NAME,
                selection,
                selectionArgs
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
                return DoggieContract.ClientEntry.CONTENT_TYPE;
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
                retCursor = getClientWithDogs(uri, projection, sortOrder);
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

            case DOGS_WITH_CLIENT_ID:
                retCursor = getDogsOfClient(uri, projection, sortOrder);
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
        long rowId;
        switch(sURIMatcher.match(uri)){
            case ITEMS:
                // returns the row ID inserted
                rowId = mOpenHelper.getWritableDatabase().insert(
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

            case CLIENTS:
                rowId = mOpenHelper.getWritableDatabase().insert(
                        DoggieContract.ClientEntry.TABLE_NAME,
                        null,
                        contentValues
                );
                if(rowId >= 0) {
                    Log.v(LOG_TAG, "INSERTED at _ID: "+rowId+ " into TABLE: "+ DoggieContract.ClientEntry.TABLE_NAME);
                    long clientId = contentValues.getAsLong(DoggieContract.ClientEntry.COLUMN_CLIENT_ID);
                    returnUri = DoggieContract.ClientEntry.buildClientDetailUri(clientId);
                }
                else
                    throw new android.database.SQLException("Failed to Insert to uri: "+ uri);
                break;

            case DOGS:
                rowId = mOpenHelper.getWritableDatabase().insert(
                        DoggieContract.DogEntry.TABLE_NAME,
                        null,
                        contentValues
                );
                if(rowId >= 0) {
                    Log.v(LOG_TAG, "INSERTED at _ID: "+rowId+ " into TABLE: "+ DoggieContract.DogEntry.TABLE_NAME
                                        + "for client: " + contentValues.get(DoggieContract.DogEntry.COLUMN_CLIENT_KEY));
                    //you can build the uri with dog id instead of the rowId
                    returnUri = ContentUris.withAppendedId(uri, rowId);
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
        // delete returns, num of rows deleted -> if there is a where clause
        //                 0 -> if passed null or all rows are deleted except if passed "1" which
        //                      returns the no of rows deleted
        switch(sURIMatcher.match(uri)){
            case ITEMS:
                if(selection == null) {
                    selection = "1";
                }
                rowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        DoggieContract.TableItems.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case CLIENTS_DETAILS:
                //delete client with client Id
                rowsDeleted = deleteClient(uri, selection, selectionArgs);
                break;
            case DOGS_WITH_CLIENT_ID:
                //delete dog with client Id
                rowsDeleted = deleteDogsOfClient(uri, selection, selectionArgs);
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
