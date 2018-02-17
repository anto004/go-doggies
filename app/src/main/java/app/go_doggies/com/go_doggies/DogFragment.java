package app.go_doggies.com.go_doggies;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by anto004 on 2/16/18.
 */

public class DogFragment extends Fragment{
    public static final String LOG_TAG = DogFragment.class.getSimpleName();
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //might need to attach to root
        View root = inflater.inflate(R.layout.activity_dog, container, false);
        mListView = (ListView) root.findViewById(R.id.dog_list_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.activity_dog,
                R.id.groomer_services_item_textView,
                new String[]{"pebbles", "spike", "tike"}
        );
        mListView.setAdapter(adapter);
        return root;
    }
}
