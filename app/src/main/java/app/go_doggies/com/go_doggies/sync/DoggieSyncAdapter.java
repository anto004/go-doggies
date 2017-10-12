package app.go_doggies.com.go_doggies.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import app.go_doggies.com.go_doggies.R;
import app.go_doggies.com.go_doggies.database.DoggieContract;

/**
 * Created by anto004 on 10/12/17.
 */

public class DoggieSyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String LOG_TAG = DoggieSyncAdapter.class.getSimpleName();

    public DoggieSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority,
                              ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync called");

                /*
         * Logs might not show inside Service
         */

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String groomerServicesJsonStr = null;

        try {
//                "groomer_id=94";
            StringBuilder urlParameter = new StringBuilder();
            urlParameter.append(URLEncoder.encode("groomer_id", "UTF-8"));
            urlParameter.append('=');
            urlParameter.append(URLEncoder.encode(String.valueOf(94), "UTF-8"));

            byte[] postData = urlParameter.toString().getBytes("UTF-8");
            String urlString = "https://go-doggies.com/Groomer_dashboard/get_groomer_service_rates";
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(postData);

            Log.d(LOG_TAG, "URL is: "+ url +
                    " URLlParameter: "+ urlParameter.toString());

            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {

                String line;
                StringBuffer stringBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()
                ));
                while((line = reader.readLine()) != null){
                    stringBuffer.append(line +"\n");
                }
                groomerServicesJsonStr= stringBuffer.toString();
                Log.v(LOG_TAG, "groomerServicesJSONString: "+groomerServicesJsonStr);
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return ;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        if(groomerServicesJsonStr != null) {
            try {
                getReadableDataFromJSON(groomerServicesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getReadableDataFromJSON(String groomerServicesJSONStr)
            throws JSONException{

        //Table Items information
        final String GROOMER_ID = "groomer_id";
        final String NAIL_TRIM = "nail_trim";
        final String NAIL_GRIND = "nail_grind";
        final String EAR_CLEANING = "ear_cleaning";
        final String PAW_TRIM = "paw_trim";
        final String SANITARY_TRIM = "sanitary_trim";
        final String FLEA_SHAMPOO = "flea_shampoo";

        JSONObject jsonObj = new JSONObject(groomerServicesJSONStr);
        int groomerId = jsonObj.getInt(GROOMER_ID);
        int nailTrim = jsonObj.getInt(NAIL_TRIM);
        int nailGrind = jsonObj.getInt(NAIL_GRIND);
        int earCleaning = jsonObj.getInt(EAR_CLEANING);
        int pawTrim = jsonObj.getInt(PAW_TRIM);
        int sanitaryTrim = jsonObj.getInt(SANITARY_TRIM);
        int fleaShampoo = jsonObj.getInt(FLEA_SHAMPOO);
//        int teethBrushing = jsonObj.getInt("teeth_brushing");
//        int deodorShampoo = jsonObj.getInt("deodor_shampoo");
//        int desheddingConditioner = jsonObj.getInt("deshedding_conditioner");
//        int brushOut = jsonObj.getInt("brush_out");
//        int specialShampoo = jsonObj.getInt("special_shampoo");
//        int desheddingShampoo = jsonObj.getInt("deshedding_shampoo");
//        int conditioner = jsonObj.getInt("conditioner");
//        int deMatt = jsonObj.getInt("de_matt");
//        int specialHandling = jsonObj.getInt("special_handling");
        ContentValues values = new ContentValues();
        values.put(DoggieContract.TableItems.COLUMN_GROOMER_ID, groomerId);
        values.put(DoggieContract.TableItems.COLUMN_NAIL_TRIM, nailTrim);
        values.put(DoggieContract.TableItems.COLUMN_NAIL_GRIND, nailGrind);
        values.put(DoggieContract.TableItems.COLUMN_EAR_CLEANING, earCleaning);
        values.put(DoggieContract.TableItems.COLUMN_PAW_TRIM, pawTrim);
        values.put(DoggieContract.TableItems.COLUMN_SANITARY_TRIM, sanitaryTrim);
        values.put(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO, fleaShampoo);

        // Query from Database
        Cursor cursor = getContext().getContentResolver().query(
                DoggieContract.TableItems.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //If no data present in database then insert
        if(!cursor.moveToFirst()){
            insertIntoDatabase(values);
            //Testing purpose
            cursor = getContext().getContentResolver().query(
                    DoggieContract.TableItems.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }
        Log.d(LOG_TAG, "Fetch BackgroundTask Complete. " + cursor.getCount() + " Rows fetched");

        cursor.close();
    }

    private void insertIntoDatabase(ContentValues values){
        //insert into Database
        Uri returnUri = getContext().getContentResolver().insert(
                DoggieContract.TableItems.CONTENT_URI,
                values
        );
        long rowId = ContentUris.parseId(returnUri);
        Log.v(LOG_TAG, "Inserted New Data at Row: "+ rowId);
    }

    public static void syncImmediately(Context context){
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);

        ContentResolver.requestSync(account, authority, bundle);
    }

    public static Account getSyncAccount(Context context){
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account account = new Account(
                context.getString(R.string.app_name),
                context.getString(R.string.accountType)
        );

        if(null == accountManager.getPassword(account)){
            Log.v(LOG_TAG, "creating new account");
            if(!accountManager.addAccountExplicitly(account, "", null)){
                Log.v(LOG_TAG, "error creating account");
                return null;
            }
        }
        return account;
    }
}
