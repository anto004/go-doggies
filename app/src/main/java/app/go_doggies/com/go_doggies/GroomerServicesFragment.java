package app.go_doggies.com.go_doggies;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import app.go_doggies.com.go_doggies.database.DoggieContract;

public class GroomerServicesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private GroomerServicesAdapter mGroomerServicesAdapter;
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

        // initialize the cursor parameter to null, swap it after loader is finished
        mGroomerServicesAdapter = new GroomerServicesAdapter(getActivity(), null, 0);

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
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_INT, savedInstanceState, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Query the database
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
        mGroomerServicesAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mGroomerServicesAdapter.swapCursor(null);
    }
}
