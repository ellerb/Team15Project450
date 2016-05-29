package tcss450.uw.edu.team15project450.browse;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.team15project450.R;
import tcss450.uw.edu.team15project450.browse.ViewCreatedTourListFragment.OnListFragmentInteractionListener;
import tcss450.uw.edu.team15project450.model.Tour;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Tour} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyVCTourRecyclerViewAdapter extends RecyclerView.Adapter<MyVCTourRecyclerViewAdapter.ViewHolder> {

    private final List<Tour> mValues;
    private final ViewCreatedTourListFragment.OnListFragmentInteractionListener mListener;


    public MyVCTourRecyclerViewAdapter(List<Tour> items, ViewCreatedTourListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_vc_tour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Tour mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id2);
            mContentView = (TextView) view.findViewById(R.id.content2);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
