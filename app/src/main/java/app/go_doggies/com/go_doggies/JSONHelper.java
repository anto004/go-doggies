package app.go_doggies.com.go_doggies;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import app.go_doggies.com.go_doggies.model.DataItem;
import app.go_doggies.com.go_doggies.sample.SampleDataProvider;


/**
 * Created by anto004 on 9/11/17.
 */

public class JSONHelper {
    public static final String LOG_TAG = JSONHelper.class.getName();
    public static final String FILE_NAME = "doggie_services.json";

    public JSONHelper() {
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
//        BufferedReader reader = null;
        FileReader reader = null;

        try {
            File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
            Gson gson = new Gson();
//            reader = new BufferedReader(new FileReader(file));
//            StringBuffer jsonStr = new StringBuffer();
//            String line = "";
//            try {
//                while((line = reader.readLine()) != null){
//                    jsonStr.append(line);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Log.v(LOG_TAG, "JsonStr BUffer:"+jsonStr);

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

    public static List<DataItem> jsonToDataItem(String jsonString){
        Gson gson = new Gson();
        DataItems dataItems = gson.fromJson(jsonString, DataItems.class);
        if(dataItems != null) {
            for (DataItem dI : dataItems.getDataItems())
                Log.v(LOG_TAG, " dataItem Object" + dI);
        }
        return null;

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

    public static DataItem doggieFromJson(JSONObject jsonObj)
                                                throws JSONException {
        DataItem dataItem = new DataItem();
        int groomerId = jsonObj.getInt("groomer_id");

        String nailTrim = jsonObj.getString("nail_trim");
        String nailGrind = jsonObj.getString("nail_grind");
        String teethBrushing = jsonObj.getString("teeth_brushing");
        String earCleaning = jsonObj.getString("ear_cleaning");
        String pawTrim = jsonObj.getString("paw_trim");
        String sanitaryTrim = jsonObj.getString("sanitary_trim");
        String fleaShampoo = jsonObj.getString("flea_shampoo");
        String deodorShampoo = jsonObj.getString("deodor_shampoo");
        String desheddingConditioner = jsonObj.getString("deshedding_conditioner");
        String brushOut = jsonObj.getString("brush_out");
        String specialShampoo = jsonObj.getString("special_shampoo");
        String desheddingShampoo = jsonObj.getString("deshedding_shampoo");
        String conditioner = jsonObj.getString("conditioner");
        String deMatt = jsonObj.getString("de_matt");
        String specialHandling = jsonObj.getString("special_handling");

        dataItem.setNailTrim(nailTrim);
        dataItem.setNailGrind(nailGrind);
        dataItem.setTeethBrushing(teethBrushing);
        dataItem.setEarCleaning(earCleaning);
        dataItem.setPawTrim(pawTrim);
        dataItem.setSanitaryTrim(sanitaryTrim);
        dataItem.setFleaShampoo(fleaShampoo);
        dataItem.setDeodorShampoo(deodorShampoo);
        dataItem.setDesheddingConditioner(desheddingConditioner);
        dataItem.setBrushOut(brushOut);
        dataItem.setSpecialShampoo(specialShampoo);
        dataItem.setDesheddingShampoo(desheddingShampoo);
        dataItem.setConditioner(conditioner);
        dataItem.setDeMatt(deMatt);
        dataItem.setSpecialHandling(specialHandling);

        return dataItem;
    }

}
