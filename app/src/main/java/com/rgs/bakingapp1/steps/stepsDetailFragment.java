package com.rgs.bakingapp1.steps;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rgs.bakingapp1.R;


/**
 * A fragment representing a single steps detail screen.
 * This fragment is either contained in a {@link stepsListActivity}
 * in two-pane mode (on tablets) or a {@link stepsDetailActivity}
 * on handsets.
 */
public class stepsDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */

    Bundle bundle;
    private String step_desc;
    private String video_url;
    TextView discription;
    TextView vidurl;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public stepsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("v1")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            step_desc= getArguments().getString("v1");
            video_url = getArguments().getString("v2");
            Activity activity = this.getActivity();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.steps_detail, container, false);
        discription = rootView.findViewById(R.id.step_ins);
        discription.setText(step_desc);


        Toast.makeText(getActivity(), getArguments().getString("v2") , Toast.LENGTH_SHORT).show();
        return rootView;
    }


}
