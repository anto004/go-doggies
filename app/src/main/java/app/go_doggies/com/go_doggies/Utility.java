package app.go_doggies.com.go_doggies;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import app.go_doggies.com.go_doggies.database.DoggieContract;

/**
 * Created by anto004 on 10/2/17.
 */

public class Utility {

    public static List<String> convertContentValuesToUXFormat(Vector<ContentValues> cvv) {
        List<String> results = new ArrayList<>();
        for (int i = 0; i < cvv.size(); i++) {
            ContentValues value = cvv.elementAt(i);
            results.add("Nail Trim  $" + value.getAsString(DoggieContract.TableItems.COLUMN_NAIL_TRIM));
            results.add("Nail Grind $" + value.getAsString(DoggieContract.TableItems.COLUMN_NAIL_GRIND));
            results.add("Teeth Brushing $" + null);
            results.add("Ear Cleaning $" + value.getAsString(DoggieContract.TableItems.COLUMN_EAR_CLEANING));
            results.add("Paw Trim $" + value.getAsString(DoggieContract.TableItems.COLUMN_PAW_TRIM));
            results.add("Sanitary Trim $" + value.getAsString(DoggieContract.TableItems.COLUMN_SANITARY_TRIM));
            results.add("Flea Shampoo $" + value.getAsString(DoggieContract.TableItems.COLUMN_FLEA_SHAMPOO));
            results.add("Deodorant Shampoo $" + null);
            results.add("De-Shedding Conditioner $" + null);
            results.add("Brush Out $" + null);
            results.add("Special Shampoo $" + null);
            results.add("DeShedding Shampoo $" + null);
            results.add("Condtitioner $" + null);
            results.add("De Matt $" + null);
            results.add("Special Handling $" + null);
        }
        return results;
    }

}
