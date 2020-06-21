package com.e.shelter.adapers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.e.shelter.R;
import com.e.shelter.utilities.Review;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ReviewListAdapter extends ArrayAdapter<Review> {
    /**
     * class ReviewListAdapter fields
     */
    private static final String TAG = "CustomListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private ReviewListAdapter adapter;
    private ArrayList<Review> cards;

    /**
     * Holds variables in a View
     */
    static class ViewHolder {
        TextView shlterName;
        TextView userName;
        TextView userEmail;
        TextView review;
        TextView star;

        MaterialButton removeButton;
    }

    /**
     * Default constructor for the PersonListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public ReviewListAdapter(Context context, int resource, ArrayList<Review> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        adapter = this;
        cards = objects;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return View
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final View result;
        final ReviewListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ReviewListAdapter.ViewHolder();
            holder.shlterName = convertView.findViewById(R.id.reviewCardContactShelterN);
            holder.userName = convertView.findViewById(R.id.reviewCardContactUserN);
            holder.userEmail = convertView.findViewById(R.id.reviewCardContactUserE);
            holder.review = convertView.findViewById(R.id.reviewCardContactReview);
            holder.removeButton = convertView.findViewById(R.id.reviewCardRemoveButton);
            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ReviewListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext,
//                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
//        result.startAnimation(animation);
        lastPosition = position;

        final String shelterN = getItem(position).getShelterName();
        final String userN = getItem(position).getUserName();
        final String userE = getItem(position).getUserEmail();
        final String Review = getItem(position).getReview();
        Log.i(TAG, "Shelter name: " + shelterN + ", review: " + Review);
        holder.shlterName.setText(shelterN);
        holder.userName.setText(userN);
        holder.userEmail.setText(userE);
        holder.review.setText(Review);

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSelectedReviewFromReviewList(position);
                adapter.notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void removeSelectedReviewFromReviewList(final int position) {

        cards.remove(position);
        Toast.makeText(mContext, "Removed from review list", Toast.LENGTH_LONG).show();
    }
}
