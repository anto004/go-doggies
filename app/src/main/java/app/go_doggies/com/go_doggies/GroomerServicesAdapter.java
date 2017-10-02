package app.go_doggies.com.go_doggies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by anto004 on 10/2/17.
 */

public class GroomerServicesAdapter extends CursorAdapter {


    public GroomerServicesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View rootView = parent.findViewById(R.id.groomer_services_listView);

        return rootView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView)view.findViewById(R.id.groomer_services_item_textView);
        //setText
    }
}
