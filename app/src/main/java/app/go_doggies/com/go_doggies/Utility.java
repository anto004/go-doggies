package app.go_doggies.com.go_doggies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.model.PriceItem;

/**
 * Created by anto004 on 10/2/17.
 */

public class Utility {
    static final String NAIL_TRIM = "Nail Trim: ";
    static final String NAIL_GRIND = "Nail Grind: ";
    static final String TEETH_BRUSHING = "Teeth Brushing: ";
    static final String EAR_CLEANING = "Ear Cleaning: ";
    static final String PAW_TRIM = "Paw Trim: ";
    static final String SANITARY_TRIM = "Sanitary Trim: ";
    static final String FLEA_SHAMPOO = "Flea Shampoo: ";
    static final String DEODORANT_SHAMPOO = "Deodorant Shampoo: ";
    static final String DE_SHEDDING_SHAMPOO = "De-Shedding Shampoo: ";
    static final String BRUSH_OUT = "Brush Out: ";
    static final String SPECIAL_SHAMPOO = "Special Shampoo: ";
    static final String DE_SHEDDING_CONDITIONER = "De-Shedding Conditioner: ";
    static final String CONDITIONER = "Conditioner: ";
    static final String DE_MATT = "De Matt: ";
    static final String SPECIAL_HANDLING = "Special Handling: ";


    public static List<String> convertVectorContentValuesToUXFormat(Vector<ContentValues> cvv) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < cvv.size(); i++) {
            ContentValues value = cvv.elementAt(i);

            results.add(NAIL_TRIM + value.getAsString(DoggieContract.TableItems.COLUMN_NAIL_TRIM));
            results.add(NAIL_GRIND + value.getAsString(DoggieContract.TableItems.COLUMN_NAIL_GRIND));
            results.add(TEETH_BRUSHING + null);
            results.add(EAR_CLEANING + value.getAsString(DoggieContract.TableItems.COLUMN_EAR_CLEANING));
            results.add(PAW_TRIM + value.getAsString(DoggieContract.TableItems.COLUMN_PAW_TRIM));
            results.add(SANITARY_TRIM + value.getAsString(DoggieContract.TableItems.COLUMN_SANITARY_TRIM));
            results.add(FLEA_SHAMPOO + value.getAsString(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO));
            results.add(DEODORANT_SHAMPOO + null);
            results.add(DE_SHEDDING_SHAMPOO + null);
            results.add(BRUSH_OUT + null);
            results.add(SPECIAL_SHAMPOO + null);
            results.add(DE_SHEDDING_CONDITIONER + null);
            results.add(CONDITIONER + null);
            results.add(DE_MATT + null);
            results.add(SPECIAL_HANDLING + null);
        }
        return results;
    }

    public static void convertCursorToContentValues(Cursor cursor){
        Vector<ContentValues> cVVectorFromDatabase = new Vector<ContentValues>(cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(cursor, cv);
                cVVectorFromDatabase.add(cv);
            } while (cursor.moveToNext());
        }
    }

    public static List<PriceItem> convertCursorToUXFormat(Cursor cursor){
        if(!cursor.moveToFirst())
            return null;
        List<PriceItem> results = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
            results.add(new PriceItem(NAIL_TRIM + cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_NAIL_TRIM))));
            results.add(new PriceItem(NAIL_GRIND + cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_NAIL_GRIND))));
            results.add(new PriceItem(TEETH_BRUSHING + null));
//        results.add(EAR_CLEANING + cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_EAR_CLEANING)));
//        results.add(PAW_TRIM + cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_PAW_TRIM)));
//        results.add(SANITARY_TRIM + cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_SANITARY_TRIM)));
//        results.add(FLEA_SHAMPOO + cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO)));
//        results.add(DEODORANT_SHAMPOO + null);
//        results.add(DE_SHEDDING_SHAMPOO + null);
//        results.add(BRUSH_OUT + null);
//        results.add(SPECIAL_SHAMPOO + null);
//        results.add(DE_SHEDDING_CONDITIONER + null);
//        results.add(CONDITIONER + null);
//        results.add(DE_MATT + null);
//        results.add(SPECIAL_HANDLING + null);
        }

        return results;
    }

}
