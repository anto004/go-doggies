package app.go_doggies.com.go_doggies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import app.go_doggies.com.go_doggies.model.ClientDetails;

public class ClientDetailsActivity extends AppCompatActivity {
    public static final String LOG_TAG = ClientDetailsActivity.class.getSimpleName();
    public static final String CLIENT_ID = "client_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        ClientDetails client = (ClientDetails) getIntent().getExtras().getParcelable(ClientAdapter.CLIENT_DETAIL);
        if(client == null){
            throw new AssertionError("Null data item received");
        }

        TextView clientName = (TextView) findViewById(R.id.client_name);
        clientName.setText(client.getClientName());

        String clientId = client.getClientId();

        DogFragment dogFragment = new DogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CLIENT_ID, clientId);
        dogFragment.setArguments(bundle);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.dog_container, dogFragment)
                    .commit();
        }

    }

}
