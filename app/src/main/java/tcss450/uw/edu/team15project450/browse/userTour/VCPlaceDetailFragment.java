package tcss450.uw.edu.team15project450.browse.userTour;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        String tourTitle = "";
        String title = "";
        String description = "";

        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            tourTitle = getTourTitle((Place) args.getSerializable(PLACE_ITEM_SELECTED));
            title = getTitle((Place) args.getSerializable(PLACE_ITEM_SELECTED));
            description = getDescription((Place) args.getSerializable(PLACE_ITEM_SELECTED));
        }

        final String emailBody = tourTitle + "\n\n" + title + "\n" + description;

        Button emailBtn = (Button) view.findViewById(R.id.shareEmail);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "My tour from StoryTour");
                intent.putExtra(Intent.EXTRA_TEXT, emailBody);
                intent.setData(Uri.parse("mailto:"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    public String getTourTitle(Place place) {
        return place.getTourTitle();
    }

    public String getDescription(Place place) {
        return place.getDescription();
    }

    public String getTitle(Place place) {
        return place.getTitle();
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
