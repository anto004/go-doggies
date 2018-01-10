package app.go_doggies.com.go_doggies.model;

import android.media.Image;

/**
 * Created by anto004 on 1/9/18.
 */

public class Client {
    public int clientId;
    public String name;
    public Image image;

    Client(String name){
        this.name = name;
    }
}
