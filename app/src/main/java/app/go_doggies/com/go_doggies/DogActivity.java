package app.go_doggies.com.go_doggies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DogActivity extends AppCompatActivity {

    public static final String LOG_TAG = DogActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dog); //frame layout with one child, the fragment listView

        String clientId = getIntent().getStringExtra(ClientDetailsActivity.CLIENT_ID);
        if(clientId == null){
            clientId = "804";
        }

        DogFragment dogFragment = new DogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ClientDetailsActivity.CLIENT_ID, clientId);
        dogFragment.setArguments(bundle);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.dog_container, dogFragment)
                    .commit();
        }

    }
}
