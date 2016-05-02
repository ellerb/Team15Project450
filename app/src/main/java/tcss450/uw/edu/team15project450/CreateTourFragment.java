package tcss450.uw.edu.team15project450;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;

import tcss450.uw.edu.team15project450.model.Place;


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

    public CreateTourFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_tour, container, false);

        mTitle = (EditText) view.findViewById(R.id.AddTourTitle);
        mDescription = (EditText) view.findViewById(R.id.AddTourDescription);
        mHasAudio = (CheckBox) view.findViewById(R.id.AddAudioDescriptionCheckBoxResponse);
        mHasImage = (CheckBox) view.findViewById(R.id.AddImageCheckBoxResponse);
        mIsPublic = (CheckBox) view.findViewById(R.id.PublicCheckBoxResponse);

        Button createBasicTourButton = (Button) view.findViewById(R.id.ButtonCreateTour);
        createBasicTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private boolean checkIfAudio() { return mHasAudio.isChecked(); }

    private boolean checkIfImage() { return mHasImage.isChecked(); }

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

//            String userid = ????;
//            sb.append("&user=");
//            sb.append(URLEncoder.encode(userid, "UTF-8"));

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
