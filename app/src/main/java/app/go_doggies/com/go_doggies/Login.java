package app.go_doggies.com.go_doggies;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anto004 on 8/25/17.
 */

public class Login extends AsyncTask<String, Void, String>{
    private static final String LOG_TAG = Login.class.getName();

    @Override
    protected String doInBackground(String...params) {
        HttpURLConnection urlConnection = null;
        String authString = "";
        BufferedReader reader = null;
        try {
//            final String username = "test@go-doggies.com";
//            final String password = "2016";
            String username = params[0];
            String password = params[1];
            StringBuilder urlParameter = new StringBuilder();
//            urlParameter.append(URLEncoder.encode("username", "UTF-8"));
//            urlParameter.append('=');
//            urlParameter.append(URLEncoder.encode(username, "UTF-8"));
//            urlParameter.append("&");
//            urlParameter.append(URLEncoder.encode("password", "UTF-8"));
//            urlParameter.append('=');
//            urlParameter.append(URLEncoder.encode(password, "UTF-8"));

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
            Log.v(LOG_TAG, "URL is: "+url);

            // Using Java.net.Authenticator
//            Authenticator.setDefault (new Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//                    return new PasswordAuthentication ("test@go_doggies.com", "2016".toCharArray());
//                }
//            });

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            //*** does not work, below commented lines will not work
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setRequestProperty("charset", "utf-8");
//                urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));
            urlConnection.getOutputStream().write(postData);

            // Using Java.net.urlConnection
//            String basicAuth = "Basic " + new String(Base64.encode(username+":"+password, Base64.NO_WRAP));
//            urlConnection.setRequestProperty("Authorization", basicAuth);

            int responseCode = urlConnection.getResponseCode();
            Log.v(LOG_TAG, "Response Code: "+responseCode);
            if(responseCode == HttpURLConnection.HTTP_OK) {
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }
}
