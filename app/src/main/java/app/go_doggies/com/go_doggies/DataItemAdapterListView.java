package app.go_doggies.com.go_doggies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.go_doggies.com.go_doggies.model.DataItem;

/**
 * Created by anto004 on 9/11/17.
 */

public class DataItemAdapterListView extends ArrayAdapter<DataItem> {
    List<DataItem> mDataItems;
    LayoutInflater mInflater;

    public DataItemAdapterListView(@NonNull Context context, @NonNull List<DataItem> objects) {
        super(context, R.layout.data_item_list_item, objects);
        mDataItems = objects;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.data_item_list_item, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.groomer_services_item_textView);

        return super.getView(position, convertView, parent);
    }
}
