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
        cv.put(DoggieContract.TableItems.COLUMN_GROOMER_ID, mDataItem.getGroomerId());
        cv.put(DoggieContract.TableItems.COLUMN_NAIL_TRIM, mDataItem.getNailTrim());
        cv.put(DoggieContract.TableItems.COLUMN_NAIL_GRIND, mDataItem.getNailGrind());
        cv.put(DoggieContract.TableItems.COLUMN_EAR_CLEANING, mDataItem.getEarCleaning());
        cv.put(DoggieContract.TableItems.COLUMN_PAW_TRIM, mDataItem.getPawTrim());
        cv.put(DoggieContract.TableItems.COLUMN_SANITARY_TRIM, mDataItem.getSanitaryTrim());
        cv.put(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO, mDataItem.getFleaShampoo());
        return cv;
    }
}
