package app.go_doggies.com.go_doggies;

import android.content.Context;
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

/**
 * Created by anto004 on 10/26/17.
 */

public class EditTextAdapter extends ArrayAdapter<String> {
    public static final String LOG_TAG = "DoggieEditTextAdapter";
    private LayoutInflater mInflater;

    public EditTextAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mInflater = LayoutInflater.from(context);
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

        final String[] price = {getItem(position)};

        viewHolder.textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                price[0] = charSequence.toString();
                Log.v(LOG_TAG, "price changed to: "+price[0]);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        viewHolder.editText.addTextChangedListener(viewHolder.textWatcher);
        viewHolder.editText.setText(price[0]);

        return view;
    }

    private static class ViewHolder{
        private EditText editText;
        private TextWatcher textWatcher;
    }
}
