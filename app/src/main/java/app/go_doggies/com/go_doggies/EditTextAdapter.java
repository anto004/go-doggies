package app.go_doggies.com.go_doggies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.model.ServiceItem;

/**
 * Created by anto004 on 10/26/17.
 */

public class EditTextAdapter extends ArrayAdapter<ServiceItem> {
    public static final String LOG_TAG = "DoggieEditTextAdapter";
    private LayoutInflater mInflater;
    private Context mContext;

    EditTextAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = mInflater.inflate(R.layout.groomer_services_list_item,
                    parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            //Saving data in the view itself
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
//        if(viewHolder.textWatcher != null){
//            viewHolder.editText.removeTextChangedListener(viewHolder.textWatcher);
//        }

        final ServiceItem item = getItem(position);
        final String previousPrice = item.getPrice();

//        viewHolder.textWatcher = new TextWatcher() {
//            boolean ignore = false;
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                Log.v(LOG_TAG, "BeforeText Changed");
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                Log.v(LOG_TAG, "OnText Changed");
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if(ignore)
//                    return;
//                ignore = true;
//                item.setPrice(editable.toString());
////                Log.v(LOG_TAG, "AfterText Changed: previous price: "+ previousPrice + " new price: "+item.getPrice());
//                ignore = false;
////                Log.v(LOG_TAG, "AfterText Changed: previous price: "+ previousPrice + " new price: "+price[0]);
//            }
//        };
//        viewHolder.editText.addTextChangedListener(viewHolder.textWatcher);
        viewHolder.textView.setText(item.getName());
        viewHolder.editText.setText(item.getPrice());
        Log.v(LOG_TAG, " price: "+item.getPrice());

        //The loader from GroomerServicesFragment Activity will monitor changes to the Table
        viewHolder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    if(!item.getPrice().equals(previousPrice)) {
                        Log.v(LOG_TAG, "new price:"+item.getPrice()+" previous price:"+previousPrice);
//                        UpdatePrice updatePrice = new UpdatePrice();
//                        updatePrice.execute(item);
                    }
                }
            }
        });
//        String newPrice = viewHolder.editText.getText().toString();
//        Log.v(LOG_TAG, "New Price: "+newPrice);

//        viewHolder.editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if(actionId == keyEvent.ACTION_UP){
//                    Log.v(LOG_TAG, "action up");
//                }
//
//                return false;
//            }
//        });

        return view;
    }


    private static class ViewHolder{
        private TextView textView;
        private EditText editText;
        private TextWatcher textWatcher;

        ViewHolder(View view){
            textView = (TextView) view.findViewById(R.id.groomer_services_item_textView);
            editText = (EditText) view.findViewById(R.id.groomer_services_item_editTextView);
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

