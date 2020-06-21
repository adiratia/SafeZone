package com.e.shelter.adapers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.e.shelter.contactus.AddNewContactActivity;
import com.e.shelter.R;
import com.e.shelter.utilities.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ContactListAdapter extends ArrayAdapter<Contact> {
    /**
     * ContactListAdapter Fields
     */
    private static final String TAG = "CustomListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    private ContactListAdapter adapter;
    private ArrayList<Contact> cards;
    private String permission;


    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView name;
        TextView phoneNumber;
        MaterialButton callButton;
        MaterialButton removeButton;
        MaterialButton editButton;
    }

    /**
     * Default constructor for the PersonListAdapter
     *
     * @param context
     * @param resource
     * @param objects
     * @param permission
     */
    public ContactListAdapter(Context context, int resource, ArrayList<Contact> objects, String permission) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        adapter = this;
        cards = objects;
        this.permission = permission;
    }
    /**
    * Returns view for contact list
    * @param position
    * @param convertView
    * @param parent
    *
    */

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final View result;
        final ContactListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ContactListAdapter.ViewHolder();
            holder.name = convertView.findViewById(R.id.contactCardContactName);
            holder.phoneNumber = convertView.findViewById(R.id.contactCardContactPhone);
            holder.callButton = convertView.findViewById(R.id.contactCardCallButton);
            holder.removeButton = convertView.findViewById(R.id.contactCardRemoveButton);
            holder.editButton = convertView.findViewById(R.id.contactCardEditButton);
            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ContactListAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext,
//                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
//        result.startAnimation(animation);
        lastPosition = position;

        final String name = getItem(position).getName();
        final String nameInEnglish = getItem(position).getNameInEnglish();
        final String phoneNumber = getItem(position).getPhoneNumber();
        holder.name.setText((name + " | " + nameInEnglish));
        holder.phoneNumber.setText(phoneNumber);

        if (permission.equals("user")) {
            holder.removeButton.setVisibility(View.INVISIBLE);
            holder.editButton.setVisibility(View.INVISIBLE);
        } else {
            holder.removeButton.setVisibility(View.VISIBLE);
            holder.editButton.setVisibility(View.VISIBLE);
        }

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSelectedContactFromContactsList(position);
                adapter.notifyDataSetChanged();
            }
        });
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + getItem(position).getPhoneNumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddNewContactActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("nameInEnglish", nameInEnglish);
                intent.putExtra("phoneNumber", phoneNumber);
                Activity origin = (Activity) mContext;
                origin.startActivityForResult(intent, 2);
            }
        });

        return convertView;
    }
    /**
     * Removes contact from database
     * @param position
     *
     */
    private void removeSelectedContactFromContactsList(int position) {
        FirebaseFirestore.getInstance().collection("ContactUsInformation")
                .whereEqualTo("nameInEnglish", getItem(position).getNameInEnglish())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        FirebaseFirestore.getInstance().collection("ContactUsInformation")
                                .document(document.getId()).delete();
                        break;
                    }
                }
                Toast.makeText(mContext, "Removed from contacts list", Toast.LENGTH_LONG).show();
            }
        });
        cards.remove(position);
    }
}
