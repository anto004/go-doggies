package app.go_doggies.com.go_doggies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.go_doggies.com.go_doggies.model.DataItem;

/**
 * Created by anto004 on 9/12/17.
 */

public class DataSource {
    public Context mContext;
    public SQLiteOpenHelper mDBHelper;
    public SQLiteDatabase mDatabase;

    public DataSource(Context context){
        this.mContext = context;
        this.mDBHelper = new DBHelper(context);
        this.mDatabase = mDBHelper.getWritableDatabase();
    }

    public void open(){
        mDatabase = mDBHelper.getWritableDatabase();
    }

    public void close(){
        mDatabase.close();
    }
    // insertion to database
    public DataItem createItem(DataItem item){
        CVForDatabase cv = new CVForDatabase(item);
        ContentValues cVValues = cv.getCVValues();
        mDatabase.insert(ItemsTable.TABLE_ITEMS, null, cVValues);
        return item;
    }

    public void seedDatabase(List<DataItem> dataItems){
        long itemsSize = getItemsCount();
        if(itemsSize == 0){
            for(DataItem item: dataItems) {
                try {
                    // use try catch, this could have exceptions like same id erro when insertion
                    createItem(item);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(mContext, "Added to Database ", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mContext, "Database has " + itemsSize + " items", Toast.LENGTH_SHORT).show();
        }
    }

    public long getItemsCount(){
        return DatabaseUtils.queryNumEntries(mDatabase, ItemsTable.TABLE_ITEMS);
    }

    public List<DataItem> getAllItemsFromDatabase(){
        List<DataItem> dataItems = new ArrayList<>();
        //Remember to close it
        Cursor cursor = mDatabase.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS, null, null,
                                            null, null, null);
        while(cursor.moveToNext()){
            DataItem item = new DataItem();
            item.setGroomerId(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_ID)));

            item.setNailTrim(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_NAIL_TRIM)));

            item.setNailGrind(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_NAIL_GRIND)));
            item.setEarCleaning(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_EAR_CLEANING)));
            item.setPawTrim(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_PAW_TRIM)));
            item.setSanitaryTrim(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_SANITARY_TRIM)));
            item.setFleaShampoo(cursor.getString(
                    cursor.getColumnIndex(ItemsTable.COLUMN_FLEA_SHAMPOO)));

            dataItems.add(item);
        }
        cursor.close(); // very very important

        return dataItems;
    }
}

