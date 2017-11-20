package app.go_doggies.com.go_doggies.sync;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by anto004 on 10/13/17.
 */

public class ServerAuthenticate {
    public static final String LOG_TAG = "DoggieServerAuth";

    public static String signIn(String username, String password){

        HttpURLConnection urlConnection = null;
        String authString = "";
        BufferedReader reader = null;
        try {

            StringBuilder urlParameter = new StringBuilder();

            urlParameter.append("user_name");
            urlParameter.append('=');
            urlParameter.append(username);
            urlParameter.append("&");
            urlParameter.append("user_psw");
            urlParameter.append('=');
            urlParameter.append(password);

            byte[] postData = urlParameter.toString().getBytes("UTF-8");
            String urlString = "https://go-doggies.com/login/user_login";
            URL url = new URL(urlString);
            Log.v(LOG_TAG, "URL is: " + url);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            //adding lines below will not work
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("charset", "utf-8");
//                urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));
            urlConnection.getOutputStream().write(postData);

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                Map<String, List<String>> headers = urlConnection.getHeaderFields ();
                for(Map.Entry<String, List<String>> entry: headers.entrySet()){
                    Log.v(LOG_TAG, "Header name: "+ entry.getKey());
                    for(String header: entry.getValue()){
                        Log.v(LOG_TAG, "values: "+ header);
                    }
                }
            }


            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()
                ));
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                authString = stringBuffer.toString();
                Log.v(LOG_TAG, "authString: " + authString);
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully authenticate
            return null;
        } finally {
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
