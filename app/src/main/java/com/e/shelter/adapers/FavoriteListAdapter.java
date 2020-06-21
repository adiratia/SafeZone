package com.e.shelter.adapers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.e.shelter.R;
import com.e.shelter.utilities.FavoriteCard;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class FavoriteListAdapter extends ArrayAdapter<FavoriteCard> {
    /**
     * class FavoriteListAdapter fields
     */
    private static final String TAG = "FavoriteListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private String mUserEmail;
    FavoriteListAdapter adapter;
    ArrayList<FavoriteCard> cards;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView address;
        MaterialButton navigateButton;
        MaterialButton removeButton;
    }

    /**
     * Default constructor for the PersonListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     */
    public FavoriteListAdapter(Context context, int resource, ArrayList<FavoriteCard> objects, String userEmail) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        mUserEmail = userEmail;
        adapter = this;
        cards = objects;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final View result;
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.favCardShelterName);
            holder.address = convertView.findViewById(R.id.favCardAddress);
            holder.navigateButton = convertView.findViewById(R.id.favCardNavigateButton);
            holder.removeButton = convertView.findViewById(R.id.favCardRemoveButton);
            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext,
//                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
//        result.startAnimation(animation);
//        lastPosition = position;

        String name = getItem(position).getName();
        String address = getItem(position).getAddress();
        Log.i(TAG, "Shelter name: " + name + ", Address: " + address);
        holder.name.setText(name);
        holder.address.setText(address);
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSelectedShelterFromFavorites(position);
                adapter.notifyDataSetChanged();
            }
        });
        holder.navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri navigationIntentUri = Uri.parse("google.navigation:q=" + getItem(position).getAddress());//creating intent with latlng
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mapIntent.setPackage("com.google.android.apps.maps");
                mContext.startActivity(mapIntent);
            }
        });

        return convertView;
    }

    /**
     * removes shelther from faviorite list
     * @param position
     */
    public void removeSelectedShelterFromFavorites(int position) {
        FavoriteCard shelterToRemove = new FavoriteCard(getItem(position).getName(),
                getItem(position).getAddress(),
                getItem(position).getLatitude(),
                getItem(position).getLongitude());

        FirebaseFirestore.getInstance().collection("FavoriteShelters").document(FirebaseAuth.getInstance().getUid()).update("favoriteShelters", FieldValue.arrayRemove(shelterToRemove));

        cards.remove(position);

        Toast.makeText(mContext, "Removed from favorites", Toast.LENGTH_LONG).show();
    }
}
