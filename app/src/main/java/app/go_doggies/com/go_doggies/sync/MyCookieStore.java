package app.go_doggies.com.go_doggies.sync;

import android.content.Context;
import android.content.SharedPreferences;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anto004 on 1/17/18.
 */

public class MyCookieStore implements CookieStore {
    public static final String LOG_TAG = MyCookieStore.class.getSimpleName();
    public static final String COOKIE_PREF = "cookie_pref";
    public static final String COOKIE_STR = "cookie";
    public Context mContext;

    public MyCookieStore(Context context){
        this.mContext = context;
    }
    @Override
    public void add(URI uri, HttpCookie httpCookie) {
        SharedPreferences.Editor cookiePrefs = mContext.getSharedPreferences(
                COOKIE_PREF, Context.MODE_PRIVATE).edit();
        //use a set for multiple cookies
        cookiePrefs.putString(COOKIE_STR, httpCookie.toString());
        cookiePrefs.apply();

    }

    @Override
    public List<HttpCookie> get(URI uri) {
        return null;
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> cookies = new ArrayList<>();
        SharedPreferences cookiePrefs = mContext.getSharedPreferences(
                MyCookieStore.COOKIE_PREF, Context.MODE_PRIVATE);
        String cookie = cookiePrefs.getString(MyCookieStore.COOKIE_STR, " ");
        if(cookie.trim().length() > 0) {
            HttpCookie httpCookie = HttpCookie.parse(cookie).get(0);
            cookies.add(httpCookie);
        }
        return cookies;
    }

    @Override
    public List<URI> getURIs() {
        return null;
    }

    @Override
    public boolean remove(URI uri, HttpCookie httpCookie) {
        return false;
    }

    @Override
    public boolean removeAll() {
        return false;
    }
}
