package app.go_doggies.com.go_doggies;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import app.go_doggies.com.go_doggies.sync.AccountGeneral;
import app.go_doggies.com.go_doggies.sync.DoggieAuthActivity;
import app.go_doggies.com.go_doggies.sync.LinkAccountActivity;
import app.go_doggies.com.go_doggies.sync.ServerAuthenticate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSION_WRITE = 1;
    private static final int LOGIN_ACTIVITY_REQUEST_CODE = 2;
    private Account mAccount;
    private AccountManager mAccountManager;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.go_doggie_toolbar);
        setSupportActionBar(toolbar);

        checkPermissions();

        mAccountManager = AccountManager.get(this);
        String accountType = getApplicationContext().getString(R.string.accountType);
        String authType = AccountGeneral.AUTHTOKEN_TYPE;

//        while(mAccount == null) {
////            mAccount = getAuthTokenForAccount(accountType, authType);
//
//        }

//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE);


        Button loginButton = (Button) findViewById(R.id.sign_in_button);
        loginButton.setOnClickListener(this);

        Button servicesButton = (Button) findViewById(R.id.services_button);
        servicesButton.setOnClickListener(this);

        Button updateNailTrimButton = (Button) findViewById(R.id.update_nail_trim_button);
        updateNailTrimButton.setOnClickListener(this);

        Button connect = (Button) findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LinkAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_ACTIVITY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "Returned from Login", Toast.LENGTH_SHORT).show();
            }
            else
                super.onActivityResult(requestCode, resultCode, data);
        }
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
//                new AuthScope("go-doggies.com/content_main/user_login", 80, AuthScope.ANY_REALM));
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
                break;
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
        CookieManager mCookieManager = ServerAuthenticate.mCookieManager;
        @Override
        protected Void doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String updatedPriceString = "";
            try {
                String charset = "UTF-8";
                StringBuilder urlParameters = new StringBuilder();
//                urlParameters.append("groomer_id");
//                urlParameters.append("=");
//                urlParameters.append("94");
//                urlParameters.append("&");
                urlParameters.append("paw_trim");
                urlParameters.append("=");
                urlParameters.append("20");
                urlParameters.append("&");
                urlParameters.append("nail_trim");
                urlParameters.append("=");
                urlParameters.append("20");

                String urlString = "https://go-doggies.com/Groomer_dashboard/update_service_rate";
                URL url = new URL(urlString);

                Log.v(LOG_TAG, "URL is: "+ url + " Parameter: "+ urlParameters.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);


                if(mCookieManager.getCookieStore().getCookies().size() > 0) {
                    urlConnection.setRequestProperty("Cookie",
                            TextUtils.join(";", mCookieManager.getCookieStore().getCookies()));
                    Log.v(LOG_TAG, "setRequestProperty: " +
                            TextUtils.join(";", mCookieManager.getCookieStore().getCookies()));
                }

                byte [] postData = urlParameters.toString().getBytes("UTF-8");
                urlConnection.getOutputStream().write(postData);

                Map<String, List<String>> headers = urlConnection.getHeaderFields();
                for(Map.Entry<String, List<String>> entry: headers.entrySet()){
                    Log.v(LOG_TAG, " Header name: "+entry.getKey() + "    "+ entry.getValue());
                }

                if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    String line;
                    StringBuffer buffer = new StringBuffer();
                    bufferedReader = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()
                    ));

                    while((line = bufferedReader.readLine()) != null){
                        buffer.append(line);
                        buffer.append("\n");
                    }
                    updatedPriceString = buffer.toString();
                    Log.v(LOG_TAG, "updatedPriceString: " + updatedPriceString);
                }

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

    public Account getAuthTokenForAccount(final String accountType, final String authType){
        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authType, null, this, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> accountManagerFuture) {
                        try {
                            Bundle bundle = accountManagerFuture.getResult();
                            String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                            if(authToken != null){
                                String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);
                                mAccount = new Account(accountName, accountType);
                            }
                            showMessage(authToken != null ? "Welcome!" : "Please Login!");
                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
        return mAccount;
    }

    private void showMessage(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
