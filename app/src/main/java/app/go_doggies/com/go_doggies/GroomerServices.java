package app.go_doggies.com.go_doggies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import app.go_doggies.com.go_doggies.database.DataSource;
import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.model.DataItem;
import app.go_doggies.com.go_doggies.sample.SampleDataProvider;

/**
 * Created by anto004 on 8/31/17.
 */

public class GroomerServices extends AppCompatActivity {
    public static final String LOG_TAG = "Go_Doggies";
    ArrayAdapter<String> groomerServicesAdapter;
    DataSource mDataSource;
    List<DataItem> mDataItems = SampleDataProvider.dataItemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groomer_services); // contains the listView
        //mDataSource.open();
        mDataSource = new DataSource(this);
        mDataSource.seedDatabase(mDataItems);

        String [] fakeData = {
                "No data to display",
                " "};
        List<String> servicesList = new ArrayList<>(Arrays.asList(fakeData));
        groomerServicesAdapter = new ArrayAdapter<String>(
                this,
                R.layout.groomer_services_list_item,
                R.id.groomer_services_item_textView,
                servicesList
        );

        ListView listView = (ListView)findViewById(R.id.groomer_services_listView);
        listView.setAdapter(groomerServicesAdapter);

        BackgroundTask bg = new BackgroundTask();
        bg.execute();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataSource.close(); // close the database when the activity is on background
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataSource.open(); // open the database connection when the activity is back to foreground
    }

    public class BackgroundTask extends AsyncTask<String, Void, List<String>> {

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



            Cursor cursor = getContentResolver().query(
                    DoggieContract.TableItems.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
            Vector<ContentValues> cVVectorFromDatabase = new Vector<ContentValues>(cursor.getCount());
            if ( cursor.moveToFirst() ) {
                do {
                    ContentValues cv = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(cursor, cv);
                    cVVectorFromDatabase.add(cv);
                } while (cursor.moveToNext());
            }
            Log.d(LOG_TAG, "FetchWeatherTask Complete. " + cVVectorFromDatabase.size() + " Inserted");

            cursor.close();
           return convertContentValuesToUXFormat(cVVectorFromDatabase);
        }

        @Override
        protected void onPostExecute(List<String> results) {
            if(results != null){
                groomerServicesAdapter.clear();
                groomerServicesAdapter.addAll(results);
            }
        }
    }

    private List<String> convertContentValuesToUXFormat(Vector<ContentValues> cvv) {
        List<String> results = new ArrayList<>();
        for(int i = 0; i < cvv.size(); i++){
            ContentValues value = cvv.elementAt(i);
            results.add("Nail Trim  $"+ value.getAsString(DoggieContract.TableItems.COLUMN_NAIL_TRIM));
            results.add("Nail Grind $"+ value.getAsString(DoggieContract.TableItems.COLUMN_NAIL_GRIND));
            results.add("Teeth Brushing $"+ null);
            results.add("Ear Cleaning $"+ value.getAsString(DoggieContract.TableItems.COLUMN_EAR_CLEANING));
            results.add("Paw Trim $"+ value.getAsString(DoggieContract.TableItems.COLUMN_PAW_TRIM));
            results.add("Sanitary Trim $"+ value.getAsString(DoggieContract.TableItems.COLUMN_SANITARY_TRIM));
            results.add("Flea Shampoo $"+ value.getAsString(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO));
            results.add("Deodorant Shampoo $"+ null);
            results.add("De-Shedding Conditioner $"+ null);
            results.add("Brush Out $"+ null);
            results.add("Special Shampoo $"+ null);
            results.add("DeShedding Shampoo $"+ null);
            results.add("Condtitioner $"+ null);
            results.add("De Matt $"+ null);
            results.add("Special Handling $"+ null);
        }
        return results;
    }

    public static List<String> getReadableServicesData(List<DataItem> dataItems){

        List<String> results = new ArrayList<>();
        DataItem item = dataItems.get(0);
        results.add("Nail Trim  $"+item.getNailTrim());
        results.add("Nail Grind $"+item.getNailGrind());
        results.add("Teeth Brushing $"+item.getTeethBrushing());
        results.add("Ear Cleaning $"+item.getEarCleaning());
        results.add("Paw Trim $"+item.getPawTrim());
        results.add("Sanitary Trim $"+item.getSanitaryTrim());
        results.add("Flea Shampoo $"+item.getFleaShampoo());
        results.add("Deodorant Shampoo $"+item.getDeodorShampoo());
        results.add("De-Shedding Conditioner $"+item.getDesheddingConditioner());
        results.add("Brush Out $"+item.getBrushOut());
        results.add("Special Shampoo $"+item.getSpecialShampoo());
        results.add("DeShedding Shampoo $"+item.getDesheddingShampoo());
        results.add("Condtitioner $"+item.getConditioner());
        results.add("De Matt $"+item.getDeMatt());
        results.add("Special Handling $"+item.getSpecialHandling());

        return results;
    }

}
