package app.go_doggies.com.go_doggies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.database.DoggieDbHelper;
import app.go_doggies.com.go_doggies.model.Client;
import app.go_doggies.com.go_doggies.model.ClientDetails;
import app.go_doggies.com.go_doggies.model.Dog;
import app.go_doggies.com.go_doggies.model.ServiceItem;


/**
 * Created by anto004 on 10/2/17.
 */

public class Utility {
    static final String LOG_TAG = "DoggieUtility";
    //Services Name Formatted
    static final String NAIL_TRIM = "Nail Trim ";
    static final String NAIL_GRIND = "Nail Grind ";
    static final String TEETH_BRUSHING = "Teeth Brushing ";
    static final String EAR_CLEANING = "Ear Cleaning ";
    static final String PAW_TRIM = "Paw Trim ";
    static final String SANITARY_TRIM = "Sanitary Trim ";
    static final String FLEA_SHAMPOO = "Flea Shampoo ";
    static final String DEODORANT_SHAMPOO = "Deodorant Shampoo ";
    static final String DE_SHEDDING_SHAMPOO = "De-Shedding Shampoo ";
    static final String BRUSH_OUT = "Brush Out ";
    static final String SPECIAL_SHAMPOO = "Special Shampoo ";
    static final String DE_SHEDDING_CONDITIONER = "De-Shedding Conditioner ";
    static final String CONDITIONER = "Conditioner ";
    static final String DE_MATT = "De Matt ";
    static final String SPECIAL_HANDLING = "Special Handling ";


    public static List<String> convertVectorContentValuesToUXFormat(Vector<ContentValues> cvv) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < cvv.size(); i++) {
            ContentValues value = cvv.elementAt(i);

            results.add(NAIL_TRIM + value.getAsString(DoggieContract.TableItems.COLUMN_NAIL_TRIM));
            results.add(NAIL_GRIND + value.getAsString(DoggieContract.TableItems.COLUMN_NAIL_GRIND));
            results.add(TEETH_BRUSHING + null);
            results.add(EAR_CLEANING + value.getAsString(DoggieContract.TableItems.COLUMN_EAR_CLEANING));
            results.add(PAW_TRIM + value.getAsString(DoggieContract.TableItems.COLUMN_PAW_TRIM));
            results.add(SANITARY_TRIM + value.getAsString(DoggieContract.TableItems.COLUMN_SANITARY_TRIM));
            results.add(FLEA_SHAMPOO + value.getAsString(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO));
            results.add(DEODORANT_SHAMPOO + null);
            results.add(DE_SHEDDING_SHAMPOO + null);
            results.add(BRUSH_OUT + null);
            results.add(SPECIAL_SHAMPOO + null);
            results.add(DE_SHEDDING_CONDITIONER + null);
            results.add(CONDITIONER + null);
            results.add(DE_MATT + null);
            results.add(SPECIAL_HANDLING + null);
        }
        return results;
    }

    public static void convertCursorToContentValues(Cursor cursor){
        Vector<ContentValues> cVVectorFromDatabase = new Vector<ContentValues>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, cv);
                cVVectorFromDatabase.add(cv);
            } while (cursor.moveToNext());
        }
    }

    public static List<ServiceItem> convertCursorToUXFormat(Cursor cursor){
        if(!cursor.moveToFirst())
            return null;
        List<ServiceItem> results = new ArrayList<>();
        results.add(new ServiceItem(NAIL_TRIM, cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_NAIL_TRIM))));
        results.add(new ServiceItem(NAIL_GRIND, cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_NAIL_GRIND))));
        results.add(new ServiceItem(TEETH_BRUSHING, null));
        results.add(new ServiceItem(EAR_CLEANING, cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_EAR_CLEANING))));
        results.add(new ServiceItem(PAW_TRIM, cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_PAW_TRIM))));
        results.add(new ServiceItem(SANITARY_TRIM, cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_SANITARY_TRIM))));
        results.add(new ServiceItem(FLEA_SHAMPOO, cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO))));
        results.add(new ServiceItem(DEODORANT_SHAMPOO, null));
        results.add(new ServiceItem(DE_SHEDDING_SHAMPOO, null));
        results.add(new ServiceItem(BRUSH_OUT, null));
        results.add(new ServiceItem(SPECIAL_SHAMPOO, null));
        results.add(new ServiceItem(DE_SHEDDING_CONDITIONER, null));
        results.add(new ServiceItem(CONDITIONER, null));
        results.add(new ServiceItem(DE_MATT, null));
        results.add(new ServiceItem(SPECIAL_HANDLING, null));

        return results;
    }

    public static List<ClientDetails> convertCursorToClientUXFormat(Cursor cursor){
        //move cursor to first
        if(!cursor.moveToFirst()){
            return null;
        }
        List<ClientDetails> clients = new ArrayList<>();
        do{
            int clientId = cursor.getInt(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_CLIENT_ID));
            String clientType = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_TYPE));
            String comment = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_COMMENT));
            String clientName = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_NAME));
            String clientImg = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_IMAGE));
            String clientPhone = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_PHONE));
            clients.add(new ClientDetails(String.valueOf(clientId), clientType, comment, clientName, clientImg, clientPhone));
        } while(cursor.moveToNext());

        return clients;
    }

    public static List<Client> convertCursorToClientWithDogsUXFormat(Cursor cursor){
        //move cursor to first
        if(!cursor.moveToFirst()){
            return null;
        }
        Map<ClientDetails, Set<Dog>> map = new HashMap<>();
        do{
            int clientId = cursor.getInt(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_CLIENT_ID));
            String clientType = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_TYPE));
            String comment = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_COMMENT));
            String clientName = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_NAME));
            String clientImg = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_IMAGE));
            String clientPhone = cursor.getString(cursor.getColumnIndex(DoggieContract.ClientEntry.COLUMN_PHONE));
            ClientDetails client = new ClientDetails(String.valueOf(clientId), clientType, comment, clientName, clientImg, clientPhone);

            Set<Dog> dogs = map.get(client);
            if(dogs == null){
                dogs = new HashSet<>();
            }

            int dogtId = cursor.getInt(cursor.getColumnIndex(DoggieContract.DogEntry.COLUMN_DOG_ID));
            String dogtName = cursor.getString(cursor.getColumnIndex(DoggieContract.DogEntry.COLUMN_NAME));
            String dogtImg = cursor.getString(cursor.getColumnIndex(DoggieContract.DogEntry.COLUMN_IMAGE));
            String dogtSize = cursor.getString(cursor.getColumnIndex(DoggieContract.DogEntry.COLUMN_SIZE));
            String dogHairType = cursor.getString(cursor.getColumnIndex(DoggieContract.DogEntry.COLUMN_HAIR_TYPE));
            Dog dog = new Dog(String.valueOf(dogtId), dogtName, dogtImg, dogtSize, dogHairType);

            dogs.add(dog);
            map.put(client, dogs);
        } while(cursor.moveToNext());

        List<Client> clients = new ArrayList<>();
        for(Map.Entry<ClientDetails, Set<Dog>> entry: map.entrySet()){
            clients.add(new Client(entry.getKey(), new ArrayList<Dog>(entry.getValue())));
        }

        return clients;
    }



    public static void insertIntoDatabase(ContentValues values, Context context){
        Log.v(LOG_TAG, "New Values to Insert: "+ values.toString());
        //insert into Database
        Uri returnUri = context.getContentResolver().insert(
                DoggieContract.TableItems.CONTENT_URI,
                values
        );
    }

    public static boolean hasNetwork(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    public static void deleteAllRecordsFromDB(Context context) {
        DoggieDbHelper dbHelper = new DoggieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DoggieContract.TableItems.TABLE_NAME, null, null);
        db.delete(DoggieContract.ClientEntry.TABLE_NAME, null, null);
        db.delete(DoggieContract.DogEntry.TABLE_NAME, null, null);
        db.close();
    }

}
