package tcss450.uw.edu.team15project450.creation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.net.URLEncoder;

import tcss450.uw.edu.team15project450.R;

/**
 * A fragment that will allow a user to add a place to a tour that the user created.
 * Right now implementation only includes adding one place when CreateTourActivity
 * is active. Will eventually be used to modify tours by allowing user to delete and
 * create many in a row.
 *
 * @author Gabrielle Bly, Gabrielle Glynn
 * @version May 4, 2016
 */
public class AddPlaceFragment extends Fragment {

    private final static String ADD_PLACE_URL
            = "http://cssgate.insttech.washington.edu/~_450atm15/addPlace.php?";
    private static final int RESULT_OK = 1;
    int PLACE_PICKER_REQUEST = 1;

    private AddPlaceListener mListener;
    private EditText mTitle;
    private EditText mDescription;
    private EditText mInstruction;
    private EditText mLongitude;
    private EditText mLatitude;
    private CheckBox mHasAudio;
    private CheckBox mHasImage;
    private String mCreatedBy;
    private String mTour;

    public AddPlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Inflates the layout for this fragment as well as
     * gets the info from the form created by the layout on button click
     * as well as validates user data before being sent to the database.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);

        Bundle bundle = this.getArguments();

        mTitle = (EditText) view.findViewById(R.id.AddPlaceTitle);
        mDescription = (EditText) view.findViewById(R.id.AddPlaceDescription);
        mInstruction = (EditText) view.findViewById(R.id.AddPlaceInstruction);
        mHasAudio = (CheckBox) view.findViewById(R.id.AddAudioPlaceCheckBoxResponse);
        mHasImage = (CheckBox) view.findViewById(R.id.AddImagePlaceCheckBoxResponse);

        // variables passed in by activity in order to get the info necessary to
        // create unique rows in database table Place.
        mCreatedBy = bundle.getString("userid");
        mTour = bundle.getString("tour");

        Button addLocationButton = (Button) view.findViewById(R.id.ButtonAddLocation);
        addLocationButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String pTitle = mTitle.getText().toString();
                if (TextUtils.isEmpty(pTitle)) {

                    Toast.makeText(view.getContext(), "Enter title", Toast.LENGTH_SHORT).show();
                    mTitle.requestFocus();
                    return;
                }

                String url = buildPlaceURL(view);
                boolean bHasAudio = checkIfAudio();
                boolean bHasImage = checkIfImage();
                mListener.addPlace(url, pTitle, bHasAudio, bHasImage);
            }
        });

        return view;
    }

    /**
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddPlaceListener) {
            mListener = (AddPlaceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddPlaceListener");
        }
    }

    /**
     * Included if statement setting boolean value to false is only
     * for phase 1 in order to prevent the audio fragment from being called.
     *
     * @return
     */
    private boolean checkIfAudio() {
        return mHasAudio.isChecked();
    }

    /**
     * Included if statement setting boolean value to false is only
     * for phase 1 in order to prevent the image fragment from being called.
     *
     * @return
     */
    private boolean checkIfImage() {
        // IF STATMENT IS FOR PHASE 1 ONLY
        if (mHasImage.isChecked()) {
            mHasImage.setChecked(false);
        }
        return mHasImage.isChecked();
    }

    /**
     * Builds the url with variables to pass to the php files
     * on the server.
     *
     * @param view
     * @return
     */
    private String buildPlaceURL(View view) {

        StringBuilder sb = new StringBuilder(ADD_PLACE_URL);

        try {

            String placeTitle = mTitle.getText().toString();
            sb.append("title=");
            sb.append(URLEncoder.encode(placeTitle, "UTF-8"));

            String placeDesc = mDescription.getText().toString();
            sb.append("&desc=");
            sb.append(URLEncoder.encode(placeDesc, "UTF-8"));

            String placeInstruction = mInstruction.getText().toString();
            sb.append("&instruct=");
            sb.append(URLEncoder.encode(placeInstruction, "UTF-8"));

            String tTitle = mTour;
            sb.append("&tour=");
            sb.append(URLEncoder.encode(tTitle, "UTF-8"));

            String userid = mCreatedBy;
            sb.append("&userid=");
            sb.append(URLEncoder.encode(userid, "UTF-8"));

            Log.i("AddPlaceFragment", sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(view.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface AddPlaceListener {
        void addPlace(String url, String place, boolean hasAudio, boolean hasImage);
    }
}
