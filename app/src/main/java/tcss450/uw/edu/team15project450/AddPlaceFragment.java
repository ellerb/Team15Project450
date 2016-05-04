package tcss450.uw.edu.team15project450;

        import android.content.Context;
        import android.net.Uri;
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
public class AddPlaceFragment extends Fragment {

    private final static String ADD_PLACE_URL
            = "http://cssgate.insttech.washington.edu/~glynng/Android/addPlace.php?";
    //= "http://cssgate.insttech.washington.edu/~_450atm15/addPlace.php?";

    private AddPlaceListener mListener;
    private EditText mTitle;
    private EditText mDescription;
    private EditText mInstruction;
    private EditText mLongitude;
    private EditText mLatitude;
    private CheckBox mHasAudio;
    private CheckBox mHasImage;
    private String mTour;
    private String mCreatedBy;

    public AddPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_place, container, false);

        Bundle bundle = this.getArguments();

        mTitle = (EditText) view.findViewById(R.id.AddPlaceTitle);
        mDescription = (EditText) view.findViewById(R.id.AddPlaceDescription);
        mInstruction = (EditText) view.findViewById(R.id.AddPlaceInstruction);
        mLatitude = (EditText) view.findViewById(R.id.AddPlaceLatitude);
        mLongitude = (EditText) view.findViewById(R.id.AddPlaceLongitude);
        mHasAudio = (CheckBox) view.findViewById(R.id.AddAudioPlaceCheckBoxResponse);
        mHasImage = (CheckBox) view.findViewById(R.id.AddImagePlaceCheckBoxResponse);
        mTour = bundle.getString("tourTitle");
        mCreatedBy = bundle.getString("userid");

        Button addPlaceButton = (Button) view.findViewById(R.id.ButtonAddPlace);
        addPlaceButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String pTitle = mTitle.getText().toString();
                String pLatitude = mLatitude.getText().toString();
                String pLongitude = mLongitude.getText().toString();
                if (TextUtils.isEmpty(pTitle)) {
                    Toast.makeText(view.getContext(), "Enter title", Toast.LENGTH_SHORT).show();
                    mTitle.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pLatitude)) {
                    Toast.makeText(view.getContext(), "Enter latitude", Toast.LENGTH_SHORT).show();
                    mLatitude.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(pLongitude)) {
                    Toast.makeText(view.getContext(), "Enter longitude", Toast.LENGTH_SHORT).show();
                    mLongitude.requestFocus();
                    return;
                }

                String url = buildCourseURL(view);
                boolean bHasAudio = checkIfAudio();
                boolean bHasImage = checkIfImage();
                mListener.addPlace(url, bHasAudio, bHasImage);
            }
        });

        return view;
    }

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

    private String buildCourseURL(View view) {

        StringBuilder sb = new StringBuilder(ADD_PLACE_URL);

        try {

            String placeTitle = mTitle.getText().toString();
            sb.append("title=");
            sb.append(placeTitle);

            String placeDesc = mDescription.getText().toString();
            sb.append("&desc=");
            sb.append(URLEncoder.encode(placeDesc, "UTF-8"));

            String placeInstruction = mInstruction.getText().toString();
            sb.append("&instruct=");
            sb.append(URLEncoder.encode(placeInstruction, "UTF-8"));

            String placeLatititude = mLatitude.getText().toString();
            sb.append("&lat=");
            sb.append(URLEncoder.encode(placeLatititude, "UTF-8"));

            String placeLongitude = mLongitude.getText().toString();
            sb.append("&long=");
            sb.append(URLEncoder.encode(placeLongitude, "UTF-8"));

            //String tourTitle = mTour.getText().toString();
            //sb.append("&tour=");
            //sb.append(URLEncoder.encode(tourTitle, "UTF-8"));

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
     *
     */
    public interface AddPlaceListener {
        void addPlace(String url, boolean hasAudio, boolean hasImage);
    }
}
