package app.go_doggies.com.go_doggies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import app.go_doggies.com.go_doggies.model.Client;
import app.go_doggies.com.go_doggies.sample.SampleClientData;
import app.go_doggies.com.go_doggies.sync.MyCookieStore;

public class MyClients extends AppCompatActivity {
    public static final String LOG_TAG = MyClients.class.getSimpleName();
    public ClientAdapter mClientAdapter;


    /**
     *
     Client list:
     URL: groomer_dashboard/get_groomer_clients
     Input: groomer_id

     Client Detail:
     URL: groomer_dashboard/get_client_details
     Input: client_id

     Transactions:
     URL: groomer_dashboard/get_groomer_past_appointments
     Input: none

     Appointments:
     URL: groomer_dashboard/get_groomer_upcoming_appointments
     Input: groomer_id
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.go_doggie_toolbar);
        setSupportActionBar(toolbar);

        List<Client> clients = SampleClientData.clients;
        for(Client c: clients){
            Log.v(LOG_TAG, "Name: "+c.getName());
        }
        mClientAdapter = new ClientAdapter(this, clients);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.client_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mClientAdapter);
//        recyclerView.setItemAnimator(null);

        ClientAsyncTask task = new ClientAsyncTask();
        task.execute();

    }

    public void fetchClients(){
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String clientsJsonStr = null;

        StringBuilder urlParameter = new StringBuilder();
        try {
            urlParameter.append(URLEncoder.encode("groomer_id","UTF-8"));
            urlParameter.append('=');
            //groomer_id = 617
            urlParameter.append(URLEncoder.encode(String.valueOf(617),"UTF-8"));

            byte[] postData = urlParameter.toString().getBytes("UTF-8");

            String urlString = "https://go-doggies.com/groomer_dashboard/get_groomer_clients";
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            CookieManager cookieManager = new CookieManager(new MyCookieStore(this), CookiePolicy.ACCEPT_ALL);

            if(cookieManager.getCookieStore().getCookies().size() > 0){
                urlConnection.setRequestProperty("Cookie",
                        TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
                Log.v(LOG_TAG, cookieManager.getCookieStore().getCookies().toString());
            }

            urlConnection.getOutputStream().write(postData);

            int responseCode = urlConnection.getResponseCode();
            Log.v(LOG_TAG, "Response Code: "+responseCode);
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                bufferedReader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()
                ));
                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }
                clientsJsonStr = stringBuffer.toString();
                Log.v(LOG_TAG, "clientsJsonStr: "+clientsJsonStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    class ClientAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            fetchClients();
            return null;
        }
    }

}
