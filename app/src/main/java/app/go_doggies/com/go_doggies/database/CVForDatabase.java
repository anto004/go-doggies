package app.go_doggies.com.go_doggies.database;

import android.content.ContentValues;

import app.go_doggies.com.go_doggies.model.DataItem;

/**
 * Created by anto004 on 9/12/17.
 */

public class CVForDatabase {
    private DataItem mDataItem;

    public CVForDatabase(DataItem dataItem){
        this.mDataItem = dataItem;
    }

    public ContentValues getCVValues(){
        ContentValues cv = new ContentValues();
        cv.put(ItemsTable.COLUMN_ID, mDataItem.getGroomerId());
        cv.put(ItemsTable.COLUMN_NAIL_TRIM, mDataItem.getNailTrim());
        cv.put(ItemsTable.COLUMN_NAIL_GRIND, mDataItem.getNailGrind());
        cv.put(ItemsTable.COLUMN_EAR_CLEANING, mDataItem.getEarCleaning());
        cv.put(ItemsTable.COLUMN_PAW_TRIM, mDataItem.getPawTrim());
        cv.put(ItemsTable.COLUMN_SANITARY_TRIM, mDataItem.getSanitaryTrim());
        cv.put(ItemsTable.COLUMN_FLEA_SHAMPOO, mDataItem.getFleaShampoo());
        return cv;
    }
}
