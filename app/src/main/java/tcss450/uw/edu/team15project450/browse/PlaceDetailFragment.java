package tcss450.uw.edu.team15project450.browse;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.team15project450.R;
import tcss450.uw.edu.team15project450.model.Place;
import tcss450.uw.edu.team15project450.model.Tour;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceDetailFragment extends Fragment {

    private TextView mPlaceTitle;
    private TextView mPlaceDescription;
    private TextView mPlaceLatitude;
    private TextView mPlaceLongitude;

    public static String PLACE_ITEM_SELECTED = "PlaceItemSelected";


    public PlaceDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_place_detail, container, false);
        mPlaceTitle = (TextView) view.findViewById(R.id.place_item_title);
        mPlaceDescription = (TextView) view.findViewById(R.id.place_item_desc);
        mPlaceLatitude = (TextView) view.findViewById(R.id.place_item_latitude);
        mPlaceLongitude = (TextView) view.findViewById(R.id.place_item_longitude);

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
