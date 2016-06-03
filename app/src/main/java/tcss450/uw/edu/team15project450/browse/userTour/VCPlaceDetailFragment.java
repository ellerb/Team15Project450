package tcss450.uw.edu.team15project450.browse.userTour;

import android.content.Context;
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

import tcss450.uw.edu.team15project450.Maps.MapActivity;
import tcss450.uw.edu.team15project450.R;
import tcss450.uw.edu.team15project450.model.Place;

/**
 * This fragment is used to retrieve the details of a tour's place
 */
public class VCPlaceDetailFragment extends Fragment {

    private AudioListenListener mListener;
    private Bundle mArgs;
    private TextView mPlaceTitle;
    private TextView mPlaceDescription;
    private TextView mPlaceInstruction;
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
        mPlaceInstruction = (TextView) view.findViewById(R.id.vc_place_item_instruct);
        mPlaceLatitude = (TextView) view.findViewById(R.id.vc_place_item_latitude);
        mPlaceLongitude = (TextView) view.findViewById(R.id.vc_place_item_longitude);

        // to hide the floating action button
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        String tourTitle = "";
        String title = "";
        String description = "";
        String instruction = "";

        mArgs = getArguments();

        // get latitude and longitude
        final double mLatitude = getLatitude((Place) mArgs.getSerializable(PLACE_ITEM_SELECTED));
        final double mLongitude = getLongitude((Place) mArgs.getSerializable(PLACE_ITEM_SELECTED));

        if (mArgs != null) {
            // Set article based on argument passed in
            tourTitle = getTourTitle((Place) mArgs.getSerializable(PLACE_ITEM_SELECTED));
            title = getTitle((Place) mArgs.getSerializable(PLACE_ITEM_SELECTED));
            description = getDescription((Place) mArgs.getSerializable(PLACE_ITEM_SELECTED));
            instruction = getInstruction((Place) mArgs.getSerializable(PLACE_ITEM_SELECTED));
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

        Button launchAudioPlayer = (Button) view.findViewById(R.id.launch_audio);
        launchAudioPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.listenAudio(mArgs);
            }
        });

        FloatingActionButton mapFloatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fabMap);
        mapFloatingActionButton.show();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fabMap);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MapActivity.class);
                i.putExtra(MapActivity.LATITUDE, mLatitude);
                i.putExtra(MapActivity.LONGITUDE, mLongitude);
                startActivity(i);
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

    public String getInstruction(Place place) {
        return place.getInstruction();
    }

    public double getLatitude(Place place) { return place.getLatitude(); }

    public double getLongitude(Place place) {
        return place.getLongitude();
    }

    public void updateView(Place place) {
        if (place != null) {
            mPlaceTitle.setText("Place: " + place.getTitle());
            mPlaceDescription.setText("Description: " + place.getDescription());
            mPlaceInstruction.setText("Instructions: " + place.getInstruction());
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AudioListenListener) {
            mListener = (AudioListenListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddAudioListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface AudioListenListener {
        void listenAudio(Bundle args);
    }
}
