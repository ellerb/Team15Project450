package tcss450.uw.edu.team15project450.creation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tcss450.uw.edu.team15project450.R;


/**
 * A fragment that will allow users to attach an image to either a
 * tour or a place. This class is not * fully implemented; right now an
 * instance is skipped by all relevant activities.
 *
 * @author Gabrielle Bly, Gabrielle Glynn
 * @version May 4, 2016
 */
public class AddImageFragment extends Fragment {

    private AddImageListener mListener;

    public AddImageFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_image, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddImageListener) {
            mListener = (AddImageListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddImageListener");
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
    public interface AddImageListener {
        void addTourImage(String url);
    }
}
