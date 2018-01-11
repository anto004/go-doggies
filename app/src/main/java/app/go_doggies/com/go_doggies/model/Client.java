package app.go_doggies.com.go_doggies.model;

import android.media.Image;

/**
 * Created by anto004 on 1/9/18.
 */

public class Client {
    public int clientId;
    public String name;
    public Image image;

    public Client(String name){
        this.name = name;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
