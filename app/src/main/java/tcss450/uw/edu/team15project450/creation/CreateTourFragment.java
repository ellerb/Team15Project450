package tcss450.uw.edu.team15project450.creation;

import android.content.Context;
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

import java.net.URLEncoder;

import tcss450.uw.edu.team15project450.R;


/**
 * A fragment that will allow a user to add a tour to the database but only with basic
 * data. From this fragment a user can choose to add audio or an image to the tour
 * (although those fragments are not yet implemented). This fragment is called exclusively
 * from CreateTourActivity.
 *
 * @author Gabrielle Bly, Gabrielle Glynn
 * @version May 4, 2016
 */
public class CreateTourFragment extends Fragment {

    private final static String CREATE_BASIC_TOUR_URL
            = "http://cssgate.insttech.washington.edu/~glynng/Android/createBasicTour.php?";
            //= "http://cssgate.insttech.washington.edu/~_450atm15/createBasicTour.php?";

    private CreateBasicTourListener mListener;
    private EditText mTitle;
    private EditText mDescription;
    private CheckBox mHasAudio;
    private CheckBox mHasImage;
    private CheckBox mIsPublic;
    private String mCreatedBy;

    public CreateTourFragment() {
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

        View view = inflater.inflate(R.layout.fragment_create_tour, container, false);

        Bundle bundle = this.getArguments();

        mTitle = (EditText) view.findViewById(R.id.AddTourTitle);
        mDescription = (EditText) view.findViewById(R.id.AddTourDescription);
        mHasAudio = (CheckBox) view.findViewById(R.id.AddAudioDescriptionCheckBoxResponse);
        mHasImage = (CheckBox) view.findViewById(R.id.AddImageCheckBoxResponse);
        mIsPublic = (CheckBox) view.findViewById(R.id.PublicCheckBoxResponse);
        mCreatedBy = bundle.getString("userid");

        Button createBasicTourButton = (Button) view.findViewById(R.id.ButtonCreateTour);
        createBasicTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tTitle = mTitle.getText().toString();
                String tDescription = mDescription.getText().toString();
                if (TextUtils.isEmpty(tTitle)) {
                    Toast.makeText(view.getContext(), "Enter title", Toast.LENGTH_SHORT).show();
                    mTitle.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(tDescription)) {
                    Toast.makeText(view.getContext(), "Enter description", Toast.LENGTH_SHORT).show();
                    mDescription.requestFocus();
                    return;

                }
                if (tTitle.length() > 25) {
                    Toast.makeText(view.getContext(), "Title must be under 25 characters", Toast.LENGTH_SHORT)
                            .show();
                    mDescription.requestFocus();
                    return;
                }
                if (tDescription.length() < 25) {
                    Toast.makeText(view.getContext(), "Enter description of at least 25 characters", Toast.LENGTH_SHORT)
                            .show();
                    mDescription.requestFocus();
                    return;
                }

                String url = buildCourseURL(view);
                boolean bHasAudio = checkIfAudio();
                boolean bHasImage = checkIfImage();
                mListener.createBasicTour(url, bHasAudio, bHasImage, tTitle);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateBasicTourListener) {
            mListener = (CreateBasicTourListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CreateBasicTourListener");
        }
    }

    /**
     * Included if statement setting boolean value to false is only
     * for phase 1 in order to prevent the audio fragment from being called.
     *
     * @return
     */
    private boolean checkIfAudio() {
        if (mHasAudio.isChecked()) {
            mHasAudio.setChecked(false);
        }
        return mHasAudio.isChecked();
    }

    /**
     * Included if statement setting boolean value to false is only
     * for phase 1 in order to prevent the image fragment from being called.
     *
     * @return
     */
    private boolean checkIfImage() {
        if (mHasImage.isChecked()) {
            mHasImage.setChecked(false);
        }
        return mHasImage.isChecked();
    }

    /**
     * This checks whether or not the user checked the box marking
     * a tour private.
     *
     * @return
     */
    private boolean checkIfPublic() { return mIsPublic.isChecked(); }

    /**
     * Builds the url with variables to pass to the php files
     * on the server.
     *
     * @param view
     * @return
     */
    private String buildCourseURL(View view) {

        StringBuilder sb = new StringBuilder(CREATE_BASIC_TOUR_URL);

        try {

            String tourTitle = mTitle.getText().toString();
            sb.append("title=");
            sb.append(tourTitle);

            String tourDesc = mDescription.getText().toString();
            sb.append("&desc=");
            sb.append(URLEncoder.encode(tourDesc, "UTF-8"));

            boolean isPublic = checkIfPublic();
            sb.append("&isPublic=");
            sb.append(isPublic);

            boolean isPublished = false;
            sb.append("&isPublish=");
            sb.append(isPublished);

            String userid = mCreatedBy;
            sb.append("&userid=");
            sb.append(URLEncoder.encode(userid, "UTF-8"));

            Log.i("CreateTourFragment", sb.toString());

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
    public interface CreateBasicTourListener {
        void createBasicTour(String url, boolean hasAudio, boolean hasImage, String title);
    }
}
