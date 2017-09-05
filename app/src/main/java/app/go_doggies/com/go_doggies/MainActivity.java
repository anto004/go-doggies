package app.go_doggies.com.go_doggies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_TAG = this.getClass().getName();
    HashMap<String, String> map;
    EditText username;
    EditText password;

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

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        Button testButton = (Button)findViewById(R.id.test_button);
        testButton.setOnClickListener(this);

        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());
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

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
//    public native String stringFromJNI();
//
//    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.login_button:
            username = (EditText) findViewById(R.id.email_text);
            password = (EditText) findViewById(R.id.password_text);

            String[] authString = {username.getText().toString(), password.getText().toString()};
            Login login = new Login();
            login.execute(authString);
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

            case R.id.test_button:
                Intent serviceIntent = new Intent(MainActivity.this, GroomerServices.class);
                startActivity(serviceIntent);
        }
    }
}
