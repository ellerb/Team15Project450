package tcss450.uw.edu.team15project450.creation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private AddAudioListener mListener;

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
        return view;

    }

    public static final String ARG_POSITION = "POSITION";
    private int mCurrentPosition = -1;

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
            updateAddAudioView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateAddAudioView(mCurrentPosition);
        }
    }

    public void updateAddAudioView(int position) {
        // This is where I set text variables?
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface AddAudioListener {
        void addTourAudio(String url, boolean hasImage);
    }
}
