package app.go_doggies.com.go_doggies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anto004 on 1/18/18.
 */

public class Client {
    @SerializedName("client_details")
    private ClientDetails clientDetails;
    @SerializedName("dogs")
    private List<Dog> dogs = null;

    public ClientDetails getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(ClientDetails clientDetails) {
        this.clientDetails = clientDetails;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }
}
