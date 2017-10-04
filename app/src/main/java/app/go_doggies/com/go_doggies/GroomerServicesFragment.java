package app.go_doggies.com.go_doggies;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import app.go_doggies.com.go_doggies.database.DoggieContract;

public class GroomerServicesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "Go-doggies";
    private ArrayAdapter<String> mGroomerServicesAdapter;
    private static final int LOADER_INT = 0;

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

        List<String> servicesList = new ArrayList<>();

        mGroomerServicesAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.groomer_services_list_item,
                R.id.groomer_services_item_textView,
                servicesList
        );

        ListView listView = (ListView)rootView.findViewById(R.id.groomer_services_listView);
        //ListView set to Adapter, next inflate the layout of the TextView, next bind the TextView
        listView.setAdapter(mGroomerServicesAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    public void updateData(){
        FetchData fetchData = new FetchData(getActivity());
        fetchData.execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getLoaderManager().initLoader(LOADER_INT, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(
                getActivity(),
                DoggieContract.TableItems.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mGroomerServicesAdapter.notifyDataSetChanged();
        List<String> services = Utility.convertCursorToUXFormat(cursor);

        if(services != null) {
            mGroomerServicesAdapter.addAll(services);
        }
        else {
            Log.v(LOG_TAG, "SERVICES is NULL");
            //getLoaderManager().restartLoader(LOADER_INT, null, this);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(LOG_TAG, "ON LOAD RESET CALLELD");
        mGroomerServicesAdapter.clear();
    }

}
