package tcss450.uw.edu.team15project450.browse.userTour;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.team15project450.R;
import tcss450.uw.edu.team15project450.model.Place;

/**
 * A simple {@link Fragment} subclass.
 */
public class VCPlaceDetailFragment extends Fragment {

    private TextView mPlaceTitle;
    private TextView mPlaceDescription;
    private TextView mPlaceLatitude;
    private TextView mPlaceLongitude;

    public static String PLACE_ITEM_SELECTED = "PlaceItemSelected";

    public VCPlaceDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_vcplace_detail, container, false);
        mPlaceTitle = (TextView) view.findViewById(R.id.vc_place_item_title);
        mPlaceDescription = (TextView) view.findViewById(R.id.vc_place_item_desc);
        mPlaceLatitude = (TextView) view.findViewById(R.id.vc_place_item_latitude);
        mPlaceLongitude = (TextView) view.findViewById(R.id.vc_place_item_longitude);

        // to hide the floating action button
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        return view;
    }

    public void updateView(Place place) {
        if (place != null) {
            mPlaceTitle.setText(place.getTitle());
            mPlaceDescription.setText(place.getDescription());
            //mPlaceLatitude.setText(place.getLatitude());
            //mPlaceLongitude.setText(place.getLongitude());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateView((Place) args.getSerializable(PLACE_ITEM_SELECTED));
        }
    }

}
