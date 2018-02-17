package app.go_doggies.com.go_doggies;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
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
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.model.Client;
import app.go_doggies.com.go_doggies.model.ClientDetails;
import app.go_doggies.com.go_doggies.model.Dog;
import app.go_doggies.com.go_doggies.sample.SampleClientData;
import app.go_doggies.com.go_doggies.sync.MyCookieStore;

public class MyClients extends AppCompatActivity
                        implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String LOG_TAG = MyClients.class.getSimpleName();
    public ClientAdapter mClientAdapter;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private static final int LOADER_INT = 1;
    private List<ClientDetails> mClients;
    /**
     * ClientDetailsActivity list:
     * URL: groomer_dashboard/get_groomer_clients
     * Input: groomer_id
     * <p>
     * ClientDetailsActivity Detail:
     * URL: groomer_dashboard/get_client_details
     * Input: client_id
     * <p>
     * Transactions:
     * URL: groomer_dashboard/get_groomer_past_appointments
     * Input: none
     * <p>
     * Appointments:
     * URL: groomer_dashboard/get_groomer_upcoming_appointments
     * Input: groomer_id
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_clients);
        Toolbar toolbar = (Toolbar) findViewById(R.id.go_doggie_toolbar);
        setSupportActionBar(toolbar);

        getLoaderManager().initLoader(LOADER_INT, null, this);

        mContext = this;
        List<ClientDetails> clients = SampleClientData.clients;

        mClientAdapter = new ClientAdapter(this, clients);
        mRecyclerView = (RecyclerView) findViewById(R.id.client_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mClientAdapter);

        ClientAsyncTask task = new ClientAsyncTask();
        task.execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(
                this,
                DoggieContract.ClientEntry.CONTENT_URI,
                null,
                null,
                null,
                DoggieContract.ClientEntry.COLUMN_NAME + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.i(LOG_TAG, "onLoadFinished called");
        //client without dogs
        mClients = Utility.convertCursorToClientUXFormat(cursor);
        if(mClients != null){
            mClientAdapter = new ClientAdapter(this, mClients);
            mRecyclerView.swapAdapter(mClientAdapter, false);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    public String fetchClients() {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;

        String clientsJsonStr;

        StringBuilder urlParameter = new StringBuilder();

        try {
            urlParameter.append(URLEncoder.encode("groomer_id", "UTF-8"));
            urlParameter.append('=');
            //groomer_id = 617
            urlParameter.append(URLEncoder.encode(String.valueOf(617), "UTF-8"));

            byte[] postData = urlParameter.toString().getBytes("UTF-8");

            String urlString = "https://go-doggies.com/groomer_dashboard/get_groomer_clients";
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            CookieManager cookieManager = new CookieManager(new MyCookieStore(this), CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);

            if (cookieManager.getCookieStore().getCookies().size() > 0) {
                List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
                if(!cookies.isEmpty() && cookies != null) {
                    urlConnection.setRequestProperty("Cookie",
                            TextUtils.join(";", cookies));
                }
                //Log.v(LOG_TAG, "cookie: "+cookieManager.getCookieStore().getCookies().toString());
            }

            urlConnection.getOutputStream().write(postData);

            int responseCode = urlConnection.getResponseCode();
            Log.v(LOG_TAG, "Response Code: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                bufferedReader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()
                ));
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                clientsJsonStr = stringBuffer.toString();
                Log.v(LOG_TAG, "clientsJsonStr: " + clientsJsonStr);

                return clientsJsonStr;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    class ClientAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            String clientsJsonStr = fetchClients();
            if(clientsJsonStr != null) {
                getReadableDataFromJson(clientsJsonStr);
            }
            return null;
        }

    }

    public void getReadableDataFromJson(String clientJsonStr){
        //Insert client first, use clientId in returnedUri to insert dogs with clientId
        List<Client> clients = JSONHelper.parseClientJsonData(clientJsonStr);

        //Use provider, this is only for testing
        Utility.deleteAllRecordsFromDB(this);

        for(int i = 0; i < clients.size(); i++){
            Client client = clients.get(i);
            ClientDetails clientDetails = client.getClientDetails();
            ContentValues clientValues = new ContentValues();
            clientValues.put(DoggieContract.ClientEntry.COLUMN_CLIENT_ID, Integer.parseInt(clientDetails.getClientId()));
            clientValues.put(DoggieContract.ClientEntry.COLUMN_TYPE, clientDetails.getClientType());
            clientValues.put(DoggieContract.ClientEntry.COLUMN_COMMENT, clientDetails.getComment() == null ?
                                                                        "" : clientDetails.getComment().toString());
            clientValues.put(DoggieContract.ClientEntry.COLUMN_NAME, clientDetails.getClientName());
            clientValues.put(DoggieContract.ClientEntry.COLUMN_IMAGE, clientDetails.getClientImg());
            clientValues.put(DoggieContract.ClientEntry.COLUMN_PHONE, clientDetails.getClientPhone());

            Uri clientUri = mContext.getContentResolver().insert(
                    DoggieContract.ClientEntry.CONTENT_URI,
                    clientValues);
            String clientId = DoggieContract.ClientEntry.getClientIdFromClientUri(clientUri);

            //multiple dogs for each client
            for(Dog dog: client.getDogs()){
                ContentValues dogValues = new ContentValues();
                dogValues.put(DoggieContract.DogEntry.COLUMN_CLIENT_KEY, Integer.parseInt(clientId));
                dogValues.put(DoggieContract.DogEntry.COLUMN_DOG_ID, Integer.parseInt(dog.getDogId()));
                dogValues.put(DoggieContract.DogEntry.COLUMN_NAME, dog.getDogName());
                dogValues.put(DoggieContract.DogEntry.COLUMN_IMAGE, dog.getDogImg());
                dogValues.put(DoggieContract.DogEntry.COLUMN_SIZE, dog.getDogSize());
                dogValues.put(DoggieContract.DogEntry.COLUMN_HAIR_TYPE, dog.getDogHairType());

                mContext.getContentResolver().insert(
                        DoggieContract.DogEntry.CONTENT_URI,
                        dogValues);
            }
        }
    }



}
