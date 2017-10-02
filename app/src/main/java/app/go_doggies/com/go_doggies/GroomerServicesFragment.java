package app.go_doggies.com.go_doggies;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import app.go_doggies.com.go_doggies.database.DataSource;
import app.go_doggies.com.go_doggies.model.DataItem;
import app.go_doggies.com.go_doggies.sample.SampleDataProvider;

public class GroomerServicesFragment extends Fragment {

    //private GroomerServicesAdapter mGroomerServicesAdapter;
    private Context mContext;
    private DataSource mDataSource;
    private ArrayAdapter<String> mGroomerServicesAdapter;
    List<DataItem> mDataItems = SampleDataProvider.dataItemList;

    public GroomerServicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.groomer_services, container, false);

        mDataSource = new DataSource(getActivity());
        mDataSource.seedDatabase(mDataItems);

        List<String> servicesList = new ArrayList<>();

        mGroomerServicesAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.groomer_services_list_item,
                R.id.groomer_services_item_textView,
                servicesList
        );

        ListView listView = (ListView)rootView.findViewById(R.id.groomer_services_listView);
        listView.setAdapter(mGroomerServicesAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    public void updateData(){
        FetchData fetchData = new FetchData(getActivity(), mGroomerServicesAdapter);
        fetchData.execute();
    }

}
