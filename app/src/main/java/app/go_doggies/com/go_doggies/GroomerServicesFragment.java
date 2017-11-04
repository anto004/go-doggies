package app.go_doggies.com.go_doggies;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.go_doggies.com.go_doggies.database.DoggieContract;
import app.go_doggies.com.go_doggies.model.ServiceItem;

public class GroomerServicesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = "Go-doggies";
    private EditTextAdapter mGroomerServicesAdapter;
    private static final int LOADER_INT = 0;
    private RecyclerView mRecyclerView;
    private boolean firstTime;
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
        View rootView =  inflater.inflate(R.layout.services, container, false);

        mGroomerServicesAdapter = new EditTextAdapter(getActivity(), new ArrayList<ServiceItem>());

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.services_recycler);
        //ListView set to Adapter, next inflate the layout of the TextView, next bind the TextView
        mRecyclerView.setAdapter(mGroomerServicesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(null);

        firstTime = true;

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    public void updateData(){

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
        Log.v(LOG_TAG, "onLoad Finished Called");

        if(firstTime) {
            //mGroomerServicesAdapter.setHasStableIds(true);
            mGroomerServicesAdapter.notifyDataSetChanged();

            List<ServiceItem> services = Utility.convertCursorToUXFormat(cursor);


            if (services != null) {
                mGroomerServicesAdapter = new EditTextAdapter(getActivity(), services);
                //set removeAndRecycleExistingViews to false if it hasStableId's
                mRecyclerView.swapAdapter(mGroomerServicesAdapter, false);
            }
            firstTime = false;
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.v(LOG_TAG, "onLoader Reset Called");
        mRecyclerView.setAdapter(null);
        // try swapAdapter
    }

}
