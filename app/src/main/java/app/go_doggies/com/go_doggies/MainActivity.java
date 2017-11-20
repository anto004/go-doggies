package app.go_doggies.com.go_doggies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import app.go_doggies.com.go_doggies.sync.DoggieAuthActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_WRITE = 1;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkPermissions();

        Button loginButton = (Button) findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(this);

        Button servicesButton = (Button)findViewById(R.id.services_button);
        servicesButton.setOnClickListener(this);

        Button updateNailTrimButton = (Button) findViewById(R.id.update_nail_trim_button);
        updateNailTrimButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_in_button:
                Intent loginIntent = new Intent(this, DoggieAuthActivity.class);
                startActivity(loginIntent);
            break;


            // Using AsyncHttpClient for authentication
//        AsyncHttpClient client = new AsyncHttpClient();
//        //client.setBasicAuth("test@go_doggies.com", "2016");
//        client.setBasicAuth("test@go_doggies.com", "2016",
//                new AuthScope("go-doggies.com/login/user_login", 80, AuthScope.ANY_REALM));
//        client.get("https://go-doggies.com/login/user_login",
//                new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                Log.v(LOG_TAG,"Response: "+responseBody);
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable
//                    error)
//            {
//                Log.v(LOG_TAG,"Failed server connect: "+statusCode + " "+ headers
//                + " "+ responseBody);
//            }
//        });

            case R.id.services_button:
                Intent serviceIntent = new Intent(MainActivity.this, GroomerServices.class);
                startActivity(serviceIntent);
                break;

            case R.id.update_nail_trim_button:
                UpdatePriceToServer updatePriceToServer = new UpdatePriceToServer();
                updatePriceToServer.execute();
        }
    }


    public boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }
    //entry request permissions
    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Toast.makeText(this, "This app only works on devices with usable external storage",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }

    static class UpdatePriceToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;

            try {

                StringBuilder urlParameter = new StringBuilder();
                urlParameter.append(URLEncoder.encode("nail_trim", "UTF-8"));
                urlParameter.append('=');
                urlParameter.append(URLEncoder.encode(String.valueOf(11), "UTF-8"));

                byte[] postData = urlParameter.toString().getBytes("UTF-8");

                String urlString = "https://go-doggies.com/Groomer_dashboard/update_service_rate";
                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(postData);

                Log.v(LOG_TAG, "URL is: "+ url + " Parameter: "+ urlParameter.toString());

                int responseCode = urlConnection.getResponseCode();
                Log.v(LOG_TAG, "Response Code: " + responseCode);

            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }

            return null;
        }
    }
}
