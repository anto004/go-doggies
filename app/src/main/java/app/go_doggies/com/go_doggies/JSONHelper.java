package app.go_doggies.com.go_doggies;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.go_doggies.com.go_doggies.model.Client;
import app.go_doggies.com.go_doggies.model.DataItem;
import app.go_doggies.com.go_doggies.model.Dog;
import app.go_doggies.com.go_doggies.sample.SampleDataProvider;


/**
 * Created by anto004 on 9/11/17.
 */

public class JSONHelper {
    public static final String LOG_TAG = JSONHelper.class.getName();
    public static final String FILE_NAME = "doggie_services.json";

    public static List<Client> parseClientJsonData(String clientJsonStr){
        Gson gson = new Gson();
        Client[] clients = gson.fromJson(clientJsonStr, Client[].class);

        for(Client client: clients) {
            Log.v(LOG_TAG, client.getClientDetails().toString());
            for(Dog dog: client.getDogs()){
                Log.v(LOG_TAG,"  Dog: "+ dog.toString());
            }
        }

        return Arrays.asList(clients);
    }


    public static boolean exportJson(String jsonStr){
        FileOutputStream outputStream = null;
        DataItems dataItems = new DataItems();
        dataItems.setDataItems(SampleDataProvider.dataItemList);

        try {
            File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            Gson gson = new Gson();
            outputStream = new FileOutputStream(file);
            String objToJson = gson.toJson(dataItems);
            Log.v(LOG_TAG, " File written. GSON data"+ objToJson);
            outputStream.write(objToJson.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static List<DataItem> importJson(){
        FileReader reader = null;

        try {
            File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            Gson gson = new Gson();
            reader = new FileReader(file);
//            Type listType = new TypeToken<List<DataItem>>(){}.getType();
//            List<DataItem> dataItem = gson.fromJson(reader, listType);
            DataItems dataItem = gson.fromJson(reader, DataItems.class);
            Log.v(LOG_TAG, "File Reading, dataItem Object: "+ dataItem.toString());
            //return dataItems.getDataItems();
            return dataItem.getDataItems();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    //convert jsonStr to DataItem objects
    public static List<DataItem> jsonToDataItem(String jsonString) throws JSONException {
        Gson gson = new GsonBuilder().create();
//        JsonParser parser = new JsonParser();
//        JsonElement obj = (JsonElement)parser.parse(jsonString);
//        if(obj.isJsonArray())
//            Log.v(LOG_TAG,"JSONArray"+obj.toString());

        // Returns Map<Key, Value>
//        Type type = new TypeToken<Map<String, String>>(){}.getType();
//        Map<String, String> dataItemMap = gson.fromJson(jsonString, type);

        List<DataItem> dataItemList = new ArrayList<>();
        // since jsonStr is not a JSONArray
        DataItem dataItem = gson.fromJson(jsonString, DataItem.class);
        dataItemList.add(dataItem);
        if(dataItem != null) {
            Log.v(LOG_TAG, "JSON String To Data Item: "+dataItem.toString());
        }
        return dataItemList;
    }

    static class DataItems{
        List<DataItem> dataItems;

        public List<DataItem> getDataItems() {
            return dataItems;
        }

        public void setDataItems(List<DataItem> dataItems) {
            this.dataItems = dataItems;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append("DataItem ");
            for(DataItem item: dataItems){
                result.append(item).append(" ");
            }
            return result.toString();
        }
    }
}
