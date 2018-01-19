package app.go_doggies.com.go_doggies.sample;

import java.util.ArrayList;
import java.util.List;

import app.go_doggies.com.go_doggies.model.ClientDetails;

/**
 * Created by anto004 on 1/10/18.
 */

public class SampleClientData {
    public static List<ClientDetails> clientDetails;

    static{
        clientDetails = new ArrayList<>();

        addItem("Mathew");
        addItem("Mark");
        addItem("Luke");
        addItem("John");
    }

    public static void addItem(String name){
        clientDetails.add(new ClientDetails(name));
    }
}
