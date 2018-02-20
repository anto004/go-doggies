package app.go_doggies.com.go_doggies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.go_doggies.com.go_doggies.database.DoggieContract;

/**
 * Created by anto004 on 2/19/18.
 */

public class DogAdapter extends CursorAdapter {

    public DogAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View root = LayoutInflater.from(context).inflate(R.layout.test_list_item, parent, false);
        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameView = (TextView) view.findViewById(R.id.dog_list_item);
        String name = cursor.getString(cursor.getColumnIndex(DoggieContract.DogEntry.COLUMN_NAME));
        nameView.setText(name);
    }
}
