package tcss450.uw.edu.team15project450.creation;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import tcss450.uw.edu.team15project450.R;

/**
 * A fragment that will allow users to attach an audio file to either a
 * tour or a place. This class is not * fully implemented; right now an
 * instance is skipped by all relevant activities.
 *
 * @author Gabrielle Bly, Gabrielle Glynn
 * @version May 4, 2016
 */
public class AddAudioFragment extends Fragment {

    private static final String TAG = "AddAudioFragment";
    private final static String ADD_AUDIO_URL
            = "http://cssgate.insttech.washington.edu/~_450atm15/addAudio.php?";
    private static final String TOUR = "tour";
    private static final String PLACE = "place";

    private AddAudioListener mListener;
    private MediaRecorder mRecorder;
    private String mOutputFile = null;
    String mServerFilePath;
    private Button mRecordButton;
    private Button mStopButton;
    private Button mAddAudioButton;
    private TextView mText;
    private String mTour;
    private String mPlace;
    private String mCreatedBy;
    private String mType;
    private boolean mPreviouslyRecorded;

    public AddAudioFragment() {
        // empty public constructor
    }

    /**
     * Inflates the layout for fragment.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_audio, container, false);

        mPreviouslyRecorded = false;

        Bundle args = getArguments();

        if (args != null) {
            mCreatedBy = args.getString("userid");
            mTour = args.getString("tour");
            mPlace = args.getString("place");
            mType = args.getString("type");
        }

        mText = (TextView) view.findViewById(R.id.record_instructions);

        String appName =  getString(R.string.app_name);

        File appFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), appName);
        File createdByFile =new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + appName, mCreatedBy);
        File tourFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + appName + "/" + mCreatedBy, mTour);

        if (!appFile.exists()) {
            appFile.mkdirs();
        }
        if (!createdByFile.exists()) {
            createdByFile.mkdirs();
        }
        if (!tourFile.exists()) {
            tourFile.mkdirs();
        }
        if(mType == PLACE) {
            File placeFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/" + appName + "/" + mCreatedBy + "/" + mTour, mPlace);

            if (!placeFile.exists()) {
                placeFile.mkdirs();
            }
        }

        mOutputFile = Environment.getExternalStorageDirectory().getAbsolutePath();
        mServerFilePath = getString(R.string.base_server_url);

        switch (mType) {
            case TOUR:
                mOutputFile += "/" + appName + "/" + mCreatedBy + "/" + mTour + "/touraudio.3gp";
                mServerFilePath += "/" + mCreatedBy + "/" + mTour + "/touraudio.3gp";
                break;
            case PLACE:
                mOutputFile += "/" + appName + "/" + mCreatedBy + "/" + mTour + "/" + mPlace + "/audio.3gp";
                mServerFilePath += "/" + mCreatedBy + "/" + mTour + "/" + mPlace + "/audio.3gp";
                break;
            default:
                mOutputFile = null;
                break;
        }

        initializeRecorder();

        mRecordButton = (Button) view.findViewById(R.id.record_button);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });

        mStopButton = (Button) view.findViewById(R.id.stop_recording_button);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

        mAddAudioButton = (Button) view.findViewById(R.id.add_audio_button);
        mAddAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddAudioButton.setEnabled(false);
                String url = buildAudioURL(v);
                mListener.addAudio(url, mType, mOutputFile);
            }
        });

        return view;
    }

    public void initializeRecorder(){
        try {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setOutputFile(mOutputFile);
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        }
    }

    public void startRecording() {
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }

    public void record(){
        if (!mPreviouslyRecorded) {
            startRecording();
            mText.setText(getString(R.string.recording_message));
            mRecordButton.setEnabled(false);
            mStopButton.setEnabled(true);
        } else {
            initializeRecorder();
            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (IllegalStateException e) {
                // start:it is called before prepare()
                // prepare: it is called after start() or before setOutputFormat()
                e.printStackTrace();
            } catch (IOException e) {
                // prepare() fails
                e.printStackTrace();
            }
            mText.setText(getString(R.string.recording_message));
            mRecordButton.setEnabled(false);
            mStopButton.setEnabled(true);
        }
    }

    public void stop(){
        stopRecording();
        mStopButton.setEnabled(false);
        mRecordButton.setEnabled(true);
        mAddAudioButton.setEnabled(true);
        mText.setText(getString(R.string.stopped_message));
        mRecordButton.setText(getString(R.string.rerecord_button));
        mPreviouslyRecorded = true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddAudioListener) {
            mListener = (AddAudioListener) context;
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
     * Builds the url with variables to pass to the php files
     * on the server.
     *
     * @param view
     * @return
     */
    private String buildAudioURL(View view) {

        StringBuilder sb = new StringBuilder(ADD_AUDIO_URL);

        try {
            sb.append("tour=");
            sb.append(URLEncoder.encode(mTour, "UTF-8"));

            sb.append("&userid=");
            sb.append(URLEncoder.encode(mCreatedBy, "UTF-8"));

            sb.append("&type=");
            sb.append(URLEncoder.encode(mType, "UTF-8"));

            if (mType == PLACE) {
                sb.append("&place=");
                sb.append(URLEncoder.encode(mPlace, "UTF-8"));
            }

            Log.i(TAG, sb.toString());
        }
        catch(Exception e) {
            Toast.makeText(view.getContext(), "Something wrong with the url."
                    + e.getMessage(), Toast.LENGTH_LONG)
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
    public interface AddAudioListener {
            void addAudio(String url, String type, String outputFile);
    }
}


