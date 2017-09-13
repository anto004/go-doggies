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
        return cv;
    }
}
