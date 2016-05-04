package tcss450.uw.edu.team15project450;

import android.content.Context;
import android.content.SharedPreferences;
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


/**
 *
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
                String pTitle = mTitle.getText().toString();
                String pDescription = mDescription.getText().toString();
                if (TextUtils.isEmpty(pTitle)) {
                    Toast.makeText(view.getContext(), "Enter title", Toast.LENGTH_SHORT).show();
                    mTitle.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pDescription)) {
                    Toast.makeText(view.getContext(), "Enter description", Toast.LENGTH_SHORT).show();
                    mDescription.requestFocus();
                    return;

                }
                if (pTitle.length() > 25) {
                    Toast.makeText(view.getContext(), "Title must be under 25 characters", Toast.LENGTH_SHORT)
                            .show();
                    mDescription.requestFocus();
                    return;
                }
                if (pDescription.length() < 25) {
                    Toast.makeText(view.getContext(), "Enter description of at least 25 characters", Toast.LENGTH_SHORT)
                            .show();
                    mDescription.requestFocus();
                    return;
                }

                String url = buildCourseURL(view);
                boolean bHasAudio = checkIfAudio();
                boolean bHasImage = checkIfImage();
                mListener.createBasicTour(url, bHasAudio, bHasImage);
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

    private boolean checkIfAudio() {
        // IF STATMENT IS FOR PHASE 1 ONLY
        if (mHasAudio.isChecked()) {
            mHasAudio.setChecked(false);
        }
        return mHasAudio.isChecked();
    }

    private boolean checkIfImage() {
        // IF STATMENT IS FOR PHASE 1 ONLY
        if (mHasImage.isChecked()) {
            mHasImage.setChecked(false);
        }
        return mHasImage.isChecked();
    }

    private boolean checkIfPublic() { return mIsPublic.isChecked(); }

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
     *
     */
    public interface CreateBasicTourListener {
        void createBasicTour(String url, boolean hasAudio, boolean hasImage);
    }
}
