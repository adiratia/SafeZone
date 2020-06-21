package com.e.shelter.adapers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.shelter.R;
import com.e.shelter.utilities.News;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class NewsListAdapter extends ArrayAdapter<News> {
    /**
     * class NewsListAdapter fields
     */
    private static final String TAG = "NewsListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    NewsListAdapter adapter;
    ArrayList<News> cards;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView title;
        TextView date;
        TextView description;
        ImageView newsImageView;
        MaterialButton shareButton;
        MaterialButton showMoreButton;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public NewsListAdapter(Context context, int resource, ArrayList<News> objects) {
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
        final NewsListAdapter.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.news_title);
            holder.date = convertView.findViewById(R.id.news_date);
            holder.description = convertView.findViewById(R.id.news_description);
            holder.shareButton = convertView.findViewById(R.id.news_share_button);
            holder.showMoreButton = convertView.findViewById(R.id.news_show_more_button);
            holder.newsImageView = convertView.findViewById(R.id.news_image);
            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext,
//                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
//        result.startAnimation(animation);
        lastPosition = position;

        holder.title.setText(getItem(position).getTitle());
        holder.date.setText(getItem(position).getDate());
        holder.description.setText(getItem(position).getDescription());
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share(position, holder);
            }
        });
        holder.showMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore(position);
            }
        });
        try {
            if (!getItem(position).getUrlToImage().equals("null")) {
                Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(getItem(position).getUrlToImage()).getContent());
                holder.newsImageView.setImageBitmap(bitmap);
            } else {
                holder.newsImageView.setImageDrawable(mContext.getDrawable(R.drawable.googleg_standard_color_18));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    /**
     *
     * @param position
     */
    private void showMore(final int position) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
        builder.setTitle("");
        builder.setMessage("Open in browser?");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = getItem(position).getUrl();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "https://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                mContext.startActivity(browserIntent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /**
     *
     * @param position
     * @param holder
     */
    private void share(int position, ViewHolder holder) {
        //get image from image view
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getItem(position).getTitle() + "\n" + getItem(position).getUrl());
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(intent);

    }
}
