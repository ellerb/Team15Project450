package tcss450.uw.edu.team15project450.listen;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
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
import tcss450.uw.edu.team15project450.model.Place;
import tcss450.uw.edu.team15project450.model.Tour;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AudioListenFragment} factory method to
 * create an instance of this fragment.
 */
public class AudioListenFragment extends Fragment {

    private static final String TOUR = "tour";
    private static final String PLACE = "place";

    private MediaPlayer mPlayer;
    private String mPlayFile = null;
    private Button mPlayButton;
    private Button mPauseButton;
    private String mTour;
    private String mPlace;
    private String mCreatedBy;
    private String mType;
    private boolean mIsPaused;
    private TextView mText;
    private int mPlaceID;

    public static String PLACE_ITEM_SELECTED = "PlaceItemSelected";
    private static final String TAG = "AudioListenFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_listen, container, false);

        // to hide the floating action button
        FloatingActionButton mapFloatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fabMap);
        mapFloatingActionButton.hide();

        Bundle args = getArguments();

        if (args != null && (args.getString("type") != TOUR || args.getString("type") != PLACE)) {
            mType = args.getString("type");

            if (mType == PLACE) {
                mTour = getPlaceTour((Place) args.getSerializable(PLACE_ITEM_SELECTED));
                mPlace = getPlace((Place) args.getSerializable(PLACE_ITEM_SELECTED));
                mCreatedBy = getTourCreatedBy((Place) args.getSerializable(PLACE_ITEM_SELECTED));
                mPlaceID = getPlaceID((Place) args.getSerializable(PLACE_ITEM_SELECTED));
            } else {
                mTour = getTour((Tour) args.getSerializable(PLACE_ITEM_SELECTED));
                mCreatedBy = getCreatedBy((Tour) args.getSerializable(PLACE_ITEM_SELECTED));
            }

            mIsPaused = false;

            mText = (TextView) view.findViewById(R.id.play_instructions);

            String appName =  getString(R.string.app_name);

            mPlayFile = Environment.getExternalStorageDirectory().getAbsolutePath();

            switch (mType) {
                case TOUR:
                    mPlayFile += "/" + appName + "/" + mCreatedBy + "/" + mTour + "/touraudio.3gp";
                    break;
                case PLACE:
                    mPlayFile += "/" + appName + "/" + mCreatedBy + "/" + mTour + "/" + mPlace + "/audio.3gp";
                    break;
                default:
                    mPlayFile = null;
                    break;
            }

            initializePlayer();

            mPlayButton = (Button) view.findViewById(R.id.play_button);
            mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play();
                }
            });

            mPauseButton = (Button) view.findViewById(R.id.pause_button);
            mPauseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stop();
                }
            });
        } else {
            Button noPlayButton = (Button) view.findViewById(R.id.play_button);
            noPlayButton.setEnabled(false);
            Toast.makeText(getActivity().getApplicationContext(), "Unable to find file. Press back button.", Toast.LENGTH_LONG)
                    .show();
        }

        return view;
    }

    public String getTour(Tour tour) { return tour.getTitle(); }

    public String getCreatedBy(Tour tour) { return tour.getCreatedBy(); }

    public String getPlaceTour(Place place) { return place.getTourTitle(); }

    public String getPlace(Place place) { return place.getTitle(); }

    public String getTourCreatedBy(Place place) { return place.getTourCreatedBy(); }

    public int getPlaceID(Place place) { return place.getPlaceID(); }

    public void initializePlayer(){
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(mPlayFile);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPlayer() {
        try {
            mPlayer.prepare();
            mPlayer.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }
    }

    public void stopPlayer() {
        try {
            if (mPlayer != null) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }

    public void play(){
        if (!mIsPaused) {
            startPlayer();
            mText.setText("Press Stop to Stop Audio...");
            mPlayButton.setEnabled(false);
            mPauseButton.setEnabled(true);
        } else {
            initializePlayer();
            startPlayer();
            mText.setText("Press Stop to Stop Audio...");
            mPlayButton.setEnabled(false);
            mPauseButton.setEnabled(true);
        }
    }

    public void stop(){
        stopPlayer();
        mText.setText("Press Play to Restart Audio...");
        mPauseButton.setEnabled(false);
        mPlayButton.setEnabled(true);
        mIsPaused = true;
    }
}
