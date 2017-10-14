package app.go_doggies.com.go_doggies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import app.go_doggies.com.go_doggies.sync.DoggieAuthAcitivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private static final int REQUEST_PERMISSION_WRITE = 1;

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

        checkPermissions();

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
                String jsonStrArray = "{\"dataItems\":[{\"a_l_l\":\"39\",\"a_l_s\":\"29\",\"a_l_x\":\"45\",\"a_m_l\":\"33\",\"a_m_s\":\"27\",\"a_m_x\":\"35\",\"a_s_l\":\"25\",\"a_s_s\":\"20\",\"a_s_x\":\"30\",\"a_x_l\":\"42\",\"a_x_s\":\"33\",\"a_x_x\":\"55\",\"b_l_l\":\"45\",\"b_l_x\":\"45\",\"b_m_l\":\"35\",\"b_m_x\":\"40\",\"b_s_l\":\"30\",\"b_s_x\":\"35\",\"b_x_l\":\"50\",\"b_x_x\":\"55\",\"brush_out\":\"10\",\"c_l_l\":\"65\",\"c_l_s\":\"55\",\"c_l_x\":\"75\",\"c_m_l\":\"50\",\"c_m_s\":\"42\",\"c_m_x\":\"55\",\"c_s_l\":\"45\",\"c_s_s\":\"35\",\"c_s_x\":\"50\",\"c_x_l\":\"70\",\"c_x_s\":\"65\",\"c_x_x\":\"85\",\"conditioner\":\"5\",\"d_l_l\":\"80\",\"d_l_s\":\"60\",\"d_l_x\":\"100\",\"d_m_l\":\"70\",\"d_m_s\":\"55\",\"d_m_x\":\"75\",\"d_s_l\":\"60\",\"d_s_s\":\"50\",\"d_s_x\":\"65\",\"d_x_l\":\"85\",\"d_x_s\":\"65\",\"d_x_x\":\"125\",\"de_matt\":\"10\",\"deodor_shampoo\":\"5\",\"deshedding_conditioner\":\"10\",\"deshedding_shampoo\":\"10\",\"ear_cleaning\":\"6\",\"flea_shampoo\":\"5\",\"groomer_id\":\"94\",\"nail_grind\":\"12\",\"nail_trim\":\"10\",\"paw_trim\":\"10\",\"sanitary_trim\":\"15\",\"special_handling\":\"10\",\"special_shampoo\":\"10\",\"teeth_brushing\":\"10\"},{\"a_l_l\":\"39\",\"a_l_s\":\"29\",\"a_l_x\":\"45\",\"a_m_l\":\"33\",\"a_m_s\":\"27\",\"a_m_x\":\"35\",\"a_s_l\":\"25\",\"a_s_s\":\"20\",\"a_s_x\":\"30\",\"a_x_l\":\"42\",\"a_x_s\":\"33\",\"a_x_x\":\"55\",\"b_l_l\":\"45\",\"b_l_x\":\"45\",\"b_m_l\":\"35\",\"b_m_x\":\"40\",\"b_s_l\":\"30\",\"b_s_x\":\"35\",\"b_x_l\":\"50\",\"b_x_x\":\"55\",\"brush_out\":\"10\",\"c_l_l\":\"65\",\"c_l_s\":\"55\",\"c_l_x\":\"75\",\"c_m_l\":\"50\",\"c_m_s\":\"42\",\"c_m_x\":\"55\",\"c_s_l\":\"45\",\"c_s_s\":\"35\",\"c_s_x\":\"50\",\"c_x_l\":\"70\",\"c_x_s\":\"65\",\"c_x_x\":\"85\",\"conditioner\":\"5\",\"d_l_l\":\"80\",\"d_l_s\":\"60\",\"d_l_x\":\"100\",\"d_m_l\":\"70\",\"d_m_s\":\"55\",\"d_m_x\":\"75\",\"d_s_l\":\"60\",\"d_s_s\":\"50\",\"d_s_x\":\"65\",\"d_x_l\":\"85\",\"d_x_s\":\"65\",\"d_x_x\":\"125\",\"de_matt\":\"10\",\"deodor_shampoo\":\"5\",\"deshedding_conditioner\":\"10\",\"deshedding_shampoo\":\"10\",\"ear_cleaning\":\"6\",\"flea_shampoo\":\"5\",\"groomer_id\":\"105\",\"nail_grind\":\"12\",\"nail_trim\":\"10\",\"paw_trim\":\"10\",\"sanitary_trim\":\"15\",\"special_handling\":\"10\",\"special_shampoo\":\"10\",\"teeth_brushing\":\"10\"}]}";
                String jsonStr = "{\"groomer_id\":\"94\",\"a_s_s\":\"20\",\"a_s_l\":\"25\",\"a_s_x\":\"30\",\"a_m_s\":\"27\",\"a_m_l\":\"33\",\"a_m_x\":\"35\",\"a_l_s\":\"29\",\"a_l_l\":\"39\",\"a_l_x\":\"45\",\"a_x_s\":\"33\",\"a_x_l\":\"42\",\"a_x_x\":\"55\",\"b_s_l\":\"30\",\"b_s_x\":\"35\",\"b_m_l\":\"35\",\"b_m_x\":\"40\",\"b_l_l\":\"45\",\"b_l_x\":\"45\",\"b_x_l\":\"50\",\"b_x_x\":\"55\",\"c_s_s\":\"35\",\"c_s_l\":\"45\",\"c_s_x\":\"50\",\"c_m_s\":\"42\",\"c_m_l\":\"50\",\"c_m_x\":\"55\",\"c_l_s\":\"55\",\"c_l_l\":\"65\",\"c_l_x\":\"75\",\"c_x_s\":\"65\",\"c_x_l\":\"70\",\"c_x_x\":\"85\",\"d_s_s\":\"50\",\"d_s_l\":\"60\",\"d_s_x\":\"65\",\"d_m_s\":\"55\",\"d_m_l\":\"70\",\"d_m_x\":\"75\",\"d_l_s\":\"60\",\"d_l_l\":\"80\",\"d_l_x\":\"100\",\"d_x_s\":\"65\",\"d_x_l\":\"85\",\"d_x_x\":\"125\",\"nail_trim\":\"10\",\"nail_grind\":\"12\",\"teeth_brushing\":\"10\",\"ear_cleaning\":\"6\",\"paw_trim\":\"10\",\"sanitary_trim\":\"15\",\"flea_shampoo\":\"5\",\"deodor_shampoo\":\"5\",\"deshedding_conditioner\":\"10\",\"brush_out\":\"10\",\"special_shampoo\":\"10\",\"deshedding_shampoo\":\"10\",\"conditioner\":\"5\",\"de_matt\":\"10\",\"special_handling\":\"10\"}";
//                Intent serviceIntent = new Intent(MainActivity.this, GroomerServices.class);
//                startActivity(serviceIntent);
                Intent doggieAuthIntent = new Intent(this, DoggieAuthAcitivity.class);
                startActivity(doggieAuthIntent);

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
}
