package app.go_doggies.com.go_doggies.sample;

import java.util.ArrayList;
import java.util.List;

import app.go_doggies.com.go_doggies.model.Client;

/**
 * Created by anto004 on 1/10/18.
 */

public class SampleClientData {
    public static List<Client> clients;

    static{
        clients = new ArrayList<>();

        addItem("Mathew");
        addItem("Mark");
        addItem("Luke");
        addItem("John");
    }

    public static void addItem(String name){
        clients.add(new Client(name));
    }
}
