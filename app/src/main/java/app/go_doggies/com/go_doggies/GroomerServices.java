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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by anto004 on 8/31/17.
 */

public class GroomerServices extends AppCompatActivity {
    private static final String LOG_TAG = "Go_Doggies";
    ArrayAdapter<String> groomerServicesAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groomer_services);

        String [] fakeData = {
                "nail-trim: 100",
                "nail-grind: 90",
                "ear-cleaning: 100"
        };
        List<String> servicesList = new ArrayList<>(Arrays.asList(fakeData));
        groomerServicesAdapter = new ArrayAdapter<String>(
                this,
                R.layout.groomer_services_item_layout,
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
                    return getReadableServicesData(groomerServicesJsonStr);
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



    public static List<String> getReadableServicesData(String groomerServicesJSONStr)
            throws JSONException {
        JSONObject jsonObj = new JSONObject(groomerServicesJSONStr);
        List<String> results = new ArrayList<>();
        int groomerId = jsonObj.getInt("groomer_id");
        int nailTrim = jsonObj.getInt("nail_trim");
        int nailGrind = jsonObj.getInt("nail_grind");
        int teethBrushing = jsonObj.getInt("teeth_brushing");
        int earCleaning = jsonObj.getInt("ear_cleaning");
        int pawTrim = jsonObj.getInt("paw_trim");
        int sanitaryTrim = jsonObj.getInt("sanitary_trim");
        int fleaShampoo = jsonObj.getInt("flea_shampoo");
        int deodorShampoo = jsonObj.getInt("deodor_shampoo");
        int desheddingConditioner = jsonObj.getInt("deshedding_conditioner");
        int brushOut = jsonObj.getInt("brush_out");
        int specialShampoo = jsonObj.getInt("special_shampoo");
        int desheddingShampoo = jsonObj.getInt("deshedding_shampoo");
        int conditioner = jsonObj.getInt("conditioner");
        int deMatt = jsonObj.getInt("de_matt");
        int specialHandling = jsonObj.getInt("special_handling");

        results.add("Nail Trim  $"+nailTrim);
        results.add("Nail Grind $"+nailGrind);
        results.add("Teeth Brushing $"+teethBrushing);
        results.add("Ear Cleaning $"+earCleaning);
        results.add("Paw Trim $"+pawTrim);
        results.add("Sanitary Trim $"+sanitaryTrim);
        results.add("Flea Shampoo $"+fleaShampoo);
        results.add("Deodorant Shampoo $"+deodorShampoo);
        results.add("De-Shedding Conditioner $"+desheddingConditioner);
        results.add("Brush Out $"+brushOut);
        results.add("Special Shampoo $"+specialShampoo);
        results.add("DeShedding Shampoo $"+desheddingShampoo);
        results.add("Condtitioner $"+conditioner);
        results.add("De Matt $"+deMatt);
        results.add("Special Handling $"+specialHandling);

        return results;
    }
}
