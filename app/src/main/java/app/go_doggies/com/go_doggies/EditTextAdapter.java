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
import android.widget.TextView;

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
            ViewHolder.textView = (TextView) view.findViewById(R.id.groomer_services_item_textView);
            viewHolder.editText = (EditText) view.findViewById(R.id.groomer_services_item_editTextView);
            //Saving data in the view itself
            view.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(viewHolder.textWatcher != null){
            viewHolder.editText.removeTextChangedListener(viewHolder.textWatcher);
        }

        final ServiceItem item = getItem(position);
        final String previousPrice = item.getPrice();

        viewHolder.textWatcher = new TextWatcher() {
            boolean ignore = false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
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
                        UpdatePrice updatePrice = new UpdatePrice();
                        updatePrice.execute(item);
//                    insertNewPrice.execute(item);
                    }
                }
            }
        });

        return view;
    }


    private static class ViewHolder{
        private static TextView textView;
        private EditText editText;
        private TextWatcher textWatcher;
    }

    class UpdatePrice extends AsyncTask<ServiceItem, Void, Void>{

        @Override
        protected Void doInBackground(ServiceItem... serviceItems) {
            Log.v(LOG_TAG, "UpdatePrice doInBackground called");
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
            Log.v(LOG_TAG, "Content Values: "+ cv.toString());

            Utility.insertIntoDatabase(cv, mContext);
            cursor.close();
            return null;
        }

    }
}

