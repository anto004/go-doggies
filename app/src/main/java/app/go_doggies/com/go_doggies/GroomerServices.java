package app.go_doggies.com.go_doggies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by anto004 on 8/31/17.
 */

public class GroomerServices extends AppCompatActivity {
    public static final String LOG_TAG = "Go_Doggies";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_groomer_services);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.groomer_service_container, new GroomerServicesFragment())
                    .commit();
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mDataSource.close(); // close the database when the activity is on background
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mDataSource.open(); // open the database connection when the activity is back to foreground
    }

}
