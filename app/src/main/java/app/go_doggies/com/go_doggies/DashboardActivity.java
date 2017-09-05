package app.go_doggies.com.go_doggies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by anto004 on 8/29/17.
 */

public class DashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //process json
        //setText to each view
        setContentView(R.layout.dashboard);
    }
}
