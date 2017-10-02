package app.go_doggies.com.go_doggies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Vector;

import app.go_doggies.com.go_doggies.database.DoggieContract;

/**
 * Created by anto004 on 10/2/17.
 */

public class FetchData extends AsyncTask<String, Void, List<String>> {
    private static final String LOG_TAG = "Go_doggies";

    private Context mContext;
    private ArrayAdapter<String> mGroomerServicesAdapter;
    public FetchData(Context context, ArrayAdapter<String> adapter) {
        mContext = context;
        mGroomerServicesAdapter = adapter;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String groomerServicesJsonStr = null;

//            try {
////                "groomer_id=94";
//                StringBuilder urlParameter = new StringBuilder();
//                urlParameter.append(URLEncoder.encode("groomer_id", "UTF-8"));
//                urlParameter.append('=');
//                urlParameter.append(URLEncoder.encode(String.valueOf(94), "UTF-8"));
//
//                byte[] postData = urlParameter.toString().getBytes("UTF-8");
//                String urlString = "https://go-doggies.com/Groomer_dashboard/get_groomer_service_rates";
//                URL url = new URL(urlString);
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setDoOutput(true);
//                urlConnection.getOutputStream().write(postData);
//
//                Log.v(LOG_TAG, "URL is: "+ url +
//                        " URLlParameter: "+ urlParameter.toString());
//
//                int responseCode = urlConnection.getResponseCode();
//                if(responseCode == HttpURLConnection.HTTP_OK) {
//                    Log.v(LOG_TAG, "HttpURLConnection: HTTP_OK" + responseCode);
//
//                    String line;
//                    StringBuffer stringBuffer = new StringBuffer();
//                    reader = new BufferedReader(new InputStreamReader(
//                            urlConnection.getInputStream()
//                    ));
//                    while((line = reader.readLine()) != null){
//                        stringBuffer.append(line +"\n");
//                    }
//                    groomerServicesJsonStr= stringBuffer.toString();
//                    Log.v(LOG_TAG, "groomerServicesJSONString: "+groomerServicesJsonStr);
//                }
//                else{
//                    Log.v(LOG_TAG, "HTTP NO CONTENT");
//                }
//
//            } catch (Exception e) {
//                Log.e(LOG_TAG, "Error ", e);
//                // If the code didn't successfully get the weather data, there's no point in attemping
//                // to parse it.
//                return null;
//            } finally{
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e(LOG_TAG, "Error closing stream", e);
//                    }
//                }
//            }
//            if(groomerServicesJsonStr != null){
//                try {
////                    JSONHelper.exportJson(groomerServicesJsonStr);
////                    List<DataItem> dataItemsFromJson = JSONHelper.importJson();
//                    List<DataItem> dataItems = JSONHelper.jsonToDataItem(groomerServicesJsonStr);
//                    return getReadableServicesData(dataItems);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            return null;


        Cursor cursor = mContext.getContentResolver().query(
                DoggieContract.TableItems.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        Vector<ContentValues> cVVectorFromDatabase = new Vector<ContentValues>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, cv);
                cVVectorFromDatabase.add(cv);
            } while (cursor.moveToNext());
        }
        Log.d(LOG_TAG, "Fetch BackgroundTask Complete. " + cVVectorFromDatabase.size() + " Inserted");

        cursor.close();
        return Utility.convertContentValuesToUXFormat(cVVectorFromDatabase);
    }

    @Override
    protected void onPostExecute(List<String> results) {
        if(results != null){
            mGroomerServicesAdapter.clear();
            mGroomerServicesAdapter.addAll(results);
        }
    }

}
