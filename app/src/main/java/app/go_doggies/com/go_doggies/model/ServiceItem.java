package app.go_doggies.com.go_doggies.model;

/**
 * Created by anto004 on 10/27/17.
 */

public class ServiceItem {
    private String name;
    private String price;

    public ServiceItem(String name, String price){
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String toString(){
        return "name: " + name + " price: " + price;
    }
}
