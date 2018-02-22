package app.go_doggies.com.go_doggies.sync;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anto004 on 1/17/18.
 */

public class MyCookieStore implements CookieStore {
    public static final String LOG_TAG = MyCookieStore.class.getSimpleName();
    private static final String COOKIE_PREF = "cookie_pref";

    private SharedPreferences mCookiePrefs;
    private Map<String, HttpCookie> mCookies;

    public MyCookieStore(Context context){
        mCookiePrefs = context.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        //one cookie for one host
        mCookies = new HashMap<String, HttpCookie>();

        Map<String, ?> prefsMap = mCookiePrefs.getAll();
        for(Map.Entry<String, ?> entry: prefsMap.entrySet()){
            String cookie = (String) entry.getValue();
            if(cookie.trim().length() > 0) {
                mCookies.put(entry.getKey(), HttpCookie.parse(cookie).get(0));
            }
        }
    }

    @Override
    public void add(URI uri, HttpCookie httpCookie) {
        String host = uri.getHost();

        mCookies.put(host, httpCookie);

        SharedPreferences.Editor cookiePrefsEditor = mCookiePrefs.edit();
        //use a set for multiple cookies
        cookiePrefsEditor.putString(host, httpCookie.toString());
        cookiePrefsEditor.apply();

    }

    @Override
    public List<HttpCookie> get(URI uri) {
        List<HttpCookie> cookies = new ArrayList<>();
        if(mCookies.containsKey(uri.getHost())) {
            cookies.add(mCookies.get(uri.getHost()));
        }
        return cookies;
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> cookies = new ArrayList<>();
        for(String host: mCookies.keySet()){
            cookies.add(mCookies.get(host));
        }
        return cookies;
    }

    @Override
    public List<URI> getURIs() {
        List<URI> uris = new ArrayList<>();
        for(String host: mCookies.keySet()){
            try {

                uris.add(new URI(host));

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return uris;
    }

    @Override
    public boolean remove(URI uri, HttpCookie httpCookie) {
        if(!mCookies.containsKey(uri.getHost())){
            return false;
        }
        mCookies.remove(uri.getHost());

        SharedPreferences.Editor cookiePrefsEditor = mCookiePrefs.edit();
        if(mCookiePrefs.contains(uri.getHost())){
            cookiePrefsEditor.remove(uri.getHost());
        }
        cookiePrefsEditor.apply();
        return true;
    }

    @Override
    public boolean removeAll() {
        mCookies.clear();

        SharedPreferences.Editor cookiePrefsEditor = mCookiePrefs.edit();
        cookiePrefsEditor.clear();
        cookiePrefsEditor.apply();

        return true;
    }
}
