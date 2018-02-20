package app.go_doggies.com.go_doggies;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import app.go_doggies.com.go_doggies.database.DoggieContract;

/**
 * Created by anto004 on 2/16/18.
 */

public class DogFragment extends Fragment
                        implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String LOG_TAG = DogFragment.class.getSimpleName();
    public static final int DOG_LOADER = 2;
    private int mClientId;
    private ListView mListView;
    private DogAdapter mDogAdapter;

    public DogFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //might need to attach to root
        View root = inflater.inflate(R.layout.content_dog, container, false);
        mListView = (ListView) root.findViewById(R.id.dog_list_view);

        mDogAdapter = new DogAdapter(getActivity(), null, 0);
        mListView.setAdapter(mDogAdapter);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if(bundle != null){
            mClientId = Integer.parseInt(bundle.getString(ClientDetailsActivity.CLIENT_ID));
        }
        getLoaderManager().initLoader(DOG_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = DoggieContract.DogEntry.COLUMN_NAME + " ASC";
        return new CursorLoader(
                getActivity(),
                DoggieContract.DogEntry.buildDogUriWithClientId(mClientId),
                null,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "onLoadFinished called");
        mDogAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDogAdapter.swapCursor(null);
    }
}
