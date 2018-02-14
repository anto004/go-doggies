package app.go_doggies.com.go_doggies.sample;

import java.util.ArrayList;
import java.util.List;

import app.go_doggies.com.go_doggies.model.Client;
import app.go_doggies.com.go_doggies.model.ClientDetails;
import app.go_doggies.com.go_doggies.model.Dog;

/**
 * Created by anto004 on 1/10/18.
 */

public class SampleClientData {
    public static List<Client> clientWithDogs;
    public static List<ClientDetails> clients;

    static{
        clientWithDogs = new ArrayList<>();
        ClientDetails c1 = new ClientDetails("600", "client", null, "John","john.img", null);
        List<Dog> c1Dogs = new ArrayList<>();
        c1Dogs.add(new Dog("300", "John's Dog1", " ", "big", "short"));
        c1Dogs.add(new Dog("301", "John's Dog2", " ", "big", "short"));
        addClientWithDogs(c1, c1Dogs);

        ClientDetails c2 = new ClientDetails("601", "client", null, "Jane","jane.img", null);
        List<Dog> c2Dogs = new ArrayList<>();
        c1Dogs.add(new Dog("301", "Jane's Dog", " ", "small", "fluffy"));
        addClientWithDogs(c2, c2Dogs);
    }

    static{
        clients = new ArrayList<>();
        clients.add(new ClientDetails("600", "client", null, "John","john.img", null));
        clients.add(new ClientDetails("601", "client", null, "Jane","jane.img", null));
    }


    public static void addClientWithDogs(ClientDetails clientDetails, List<Dog> dogs){
        clientWithDogs.add(new Client(clientDetails, dogs));
    }

}
