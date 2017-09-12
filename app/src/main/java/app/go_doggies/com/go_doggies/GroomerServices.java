package app.go_doggies.com.go_doggies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.go_doggies.com.go_doggies.model.DataItem;

/**
 * Created by anto004 on 8/31/17.
 */

public class GroomerServices extends AppCompatActivity {
    public static final String LOG_TAG = "Go_Doggies";
    ArrayAdapter<String> groomerServicesAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groomer_services); // contains the listView

        String [] fakeData = {
                "No data to display",
                " ",
                " "
        };
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

    public class BackgroundTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
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

                Log.v(LOG_TAG, "URL is: "+url +
                        "urlParameter: "+urlParameter.toString());


                int responseCode = urlConnection.getResponseCode();
                Log.v(LOG_TAG, "Response Code: "+responseCode);
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    Log.v(LOG_TAG, "HttpURLConnection: HTTP_OK" + responseCode);

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
                else{
                    Log.v(LOG_TAG, "False - HTTP_OK");

                }

            } catch (Exception e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
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
            if(groomerServicesJsonStr != null){
                try {
//                    JSONHelper.exportJson(groomerServicesJsonStr);
//                    List<DataItem> dataItemsFromJson = JSONHelper.importJson();
                    List<DataItem> dataItems = JSONHelper.jsonToDataItem(groomerServicesJsonStr);
                    return getReadableServicesData(dataItems);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> results) {
            if(results != null){
                groomerServicesAdapter.clear();
                groomerServicesAdapter.addAll(results);
            }
        }
    }



    public static List<String> getReadableServicesData(List<DataItem> dataItems)
            throws JSONException {

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
