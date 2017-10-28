package app.go_doggies.com.go_doggies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.util.List;

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
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.editText = (EditText) view.findViewById(R.id.groomer_services_item_editTextView);
            //Saving data in the view itself
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if(viewHolder.textWatcher != null){
            viewHolder.editText.removeTextChangedListener(viewHolder.textWatcher);
        }

        final ServiceItem item = getItem(position);

        viewHolder.textWatcher = new TextWatcher() {
            String previousPrice;
            boolean ignore = false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                previousPrice = charSequence.toString();
//                Log.v(LOG_TAG, "BeforeText Changed");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Log.v(LOG_TAG, "OnText Changed");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ignore)
                    return;
                ignore = true;
                item.setPrice(editable.toString());
//                Log.v(LOG_TAG, "AfterText Changed: previous price: "+ previousPrice + " new price: "+item.getPrice());
                ignore = false;
//                Log.v(LOG_TAG, "AfterText Changed: previous price: "+ previousPrice + " new price: "+price[0]);
            }
        };
        viewHolder.editText.addTextChangedListener(viewHolder.textWatcher);
        viewHolder.editText.setText(item.getPrice());
        Log.v(LOG_TAG, " price: "+item.getPrice());

        viewHolder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                InsertNewPrice insertNewPrice = new InsertNewPrice();
                if(!hasFocus) {
                    Log.v(LOG_TAG, "Exit onFocusChangeListener called ");
//                    insertNewPrice.execute(item);
                }
                if(hasFocus) {
                    Log.v(LOG_TAG, "Entering onFocusChangeListener called");
//                    insertNewPrice.execute(item);
                }
            }
        });
        return view;
    }


    private static class ViewHolder{
        private EditText editText;
        private TextWatcher textWatcher;
    }

    class InsertNewPrice extends AsyncTask<ServiceItem, Void, Void>{

        @Override
        protected Void doInBackground(ServiceItem... serviceItems) {
            ServiceItem item = serviceItems[0];
            Log.v(LOG_TAG, "new service price inserting: "+item.toString());
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
            List<ServiceItem> databaseServiceItems = Utility.convertCursorToUXFormat(cursor);

            ContentValues cv = new ContentValues();
            for(ServiceItem serviceItem: databaseServiceItems){

                String databaseServiceName = serviceItem.getName();

                cv.put(databaseServiceName, serviceItem.getPrice());

                if(databaseServiceName.equals(serviceItem.getName())){
                    cv.remove(databaseServiceName);
                    cv.put(databaseServiceName, serviceItem.getPrice());
                }
            }

            Utility.insertIntoDatabase(cv, mContext);
            cursor.close();
            return null;
        }
    }
}
