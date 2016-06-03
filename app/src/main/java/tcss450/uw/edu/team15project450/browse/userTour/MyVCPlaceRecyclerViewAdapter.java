package tcss450.uw.edu.team15project450.browse.userTour;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.team15project450.R;
import tcss450.uw.edu.team15project450.browse.userTour.VCPlaceFragment.OnPlaceListFragmentInteractionListener;
import tcss450.uw.edu.team15project450.model.Place;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Place} and makes a call to the
 * specified {@link OnPlaceListFragmentInteractionListener}.
 */
public class MyVCPlaceRecyclerViewAdapter extends RecyclerView.Adapter<MyVCPlaceRecyclerViewAdapter.ViewHolder> {

    private final List<Place> mPlaceValues;
    private final VCPlaceFragment.OnPlaceListFragmentInteractionListener mListener;

    public MyVCPlaceRecyclerViewAdapter(List<Place> items, VCPlaceFragment.OnPlaceListFragmentInteractionListener listener) {
        mPlaceValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_place_vc, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mPlaceValues.get(position);
        holder.mIdView.setText(mPlaceValues.get(position).getTitle());
        holder.mContentView.setText(mPlaceValues.get(position).getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onPlaceListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaceValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Place mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
