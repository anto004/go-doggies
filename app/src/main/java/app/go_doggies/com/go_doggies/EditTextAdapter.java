package app.go_doggies.com.go_doggies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.go_doggies.com.go_doggies.EditTextAdapter.MyViewHolder;
import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.model.ServiceItem;

/**
 * Created by anto004 on 10/26/17.
 */

public class EditTextAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private static final String LOG_TAG = "DoggieEditTextAdapter";
    private Context mContext;
    private List<ServiceItem> services;
    public EditTextAdapter(Context context, List<ServiceItem> services) {
        this.mContext = context;
        this.services = services;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.services_list_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.v(LOG_TAG, "onBindViewHolder position: "+position);
        ServiceItem item = services.get(position);
        holder.textView.setText(item.getName());
        holder.editText.setText(item.getPrice());

//        holder.editText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                EditText editText = (EditText) view.findViewById(R.id.services_item_editText);
//                Toast.makeText(mContext, editText.getText(), Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private EditText editText;
        private TextWatcher textWatcher;


        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.services_item_textView);
            editText = (EditText) view.findViewById(R.id.services_item_editText);
        }
    }

    class UpdatePrice extends AsyncTask<ServiceItem, Void, Void>{

        @Override
        protected Void doInBackground(ServiceItem... serviceItems) {
            Log.v(LOG_TAG, "UpdatePrice doInBackground called");
            ServiceItem item = serviceItems[0];
            // remember to close cursor
            Cursor cursor = mContext.getContentResolver().query(
                    DoggieContract.TableItems.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
            if(!cursor.moveToFirst())
                return null;

            HashMap<String, String> columnNamesWithPrice = cursorToColumnNamesWithPrice(cursor);
            // I want to insert the new price by replacing the old price with the new price
            // how? I get a serviceItem object with formatted Name so I need to compare the formatted Name
            // with the ColumnName and then change the price, let's try with On^2 time complexity first
            // sol: convert formatted name to lower case and remove all space. convert columnNames to lower case and compare

            ContentValues cv = new ContentValues();
            String columnId = cursor.getString(cursor.getColumnIndex(DoggieContract.TableItems._ID));

            for(Map.Entry<String, String> entry: columnNamesWithPrice.entrySet() ){
                String columnName = entry.getKey();
                String itemNameToUpdate = item.getName();
                //Convert both Formatted Item Name and Column Name to lowercase and remove spaces
                itemNameToUpdate = itemNameToUpdate.replaceAll("\\s","").toLowerCase();
                columnName = columnName.toLowerCase();

                cv.put(entry.getKey(), entry.getValue());
                //update price for matched Item Name
                if(columnName.equals(itemNameToUpdate)){
                    //remove the old price
                    cv.remove(entry.getKey());
                    //add new price
                    cv.put(entry.getKey(), item.getPrice());
                }
            }
            Log.v(LOG_TAG, "Content Values: "+cv.toString());

            //Before insert, delete the row to be inserted with new values, goal is to replace the row
            mContext.getContentResolver().delete(
                    DoggieContract.TableItems.CONTENT_URI,
                    DoggieContract.TableItems._ID + "=?",
                    new String[]{columnId}
            );

//            Utility.insertIntoDatabase(cv, mContext);
            cursor.close();
            return null;
        }

        public HashMap<String, String> cursorToColumnNamesWithPrice(Cursor cursor){
            //ColumnName, Price
            HashMap<String, String> map = new HashMap<>();

            if(!cursor.moveToFirst())
                return null;

            String [] columnNames = cursor.getColumnNames();
            for(int i = 0; i < columnNames.length; i++){
                String columnName = columnNames[i];
                String price = cursor.getString(cursor.getColumnIndex(columnName));
                map.put(columnName, price);
                Log.v("getColumnNames", "columnName: " + columnName +
                        "   Price: " + price);
            }
            return map;
        }

    }
}

