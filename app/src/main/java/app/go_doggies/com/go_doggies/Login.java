package app.go_doggies.com.go_doggies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by anto004 on 8/25/17.
 */

public class Login extends AsyncTask<HashMap<String, String>, Void, String>{
    private static final String LOG_TAG = Login.class.getName();

    @Override
    protected String doInBackground(HashMap<String, String>...params) {
        HttpURLConnection urlConnection = null;
        String authString = "";
        BufferedReader reader = null;
        try {
            byte[] postData = params.toString().getBytes("UTF-8");
            URLEncoder encoder = new URLEncoder(params);
            String urlString = "https://go-doggies.com/login/user_login";
            URL url = new URL(urlString);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            //*** does not work, below commented lines will not work
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("charset", "utf-8");
//                urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));
            urlConnection.getOutputStream().write(postData);


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
                authString = stringBuffer.toString();
                Log.v(LOG_TAG, "authString: "+authString);
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
        return authString;
    }
}
