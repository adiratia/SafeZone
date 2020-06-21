package com.e.shelter.adapers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.e.shelter.R;
import com.e.shelter.utilities.AddressWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomSuggestionsAdapter extends SuggestionsAdapter<AddressWrapper, CustomSuggestionsAdapter.SuggestionHolder> {
    /**
     * class CustomSuggestionsAdapter fields
     */
    private static final String ADDRESSES_FILE_NAME = "addresses2.json";
    private List<AddressWrapper> addressWrapperList = new ArrayList<>();

    /**
     * CustomSuggestionsAdapter constructor
     * @param inflater
     * @param context
     */
    public CustomSuggestionsAdapter(LayoutInflater inflater, Context context) {
        super(inflater);
    }

    /**
     *
     * @return view height
     */
    @Override
    public int getSingleViewHeight() {
        return 80;
    }

    /**
     *
     * @param parent
     * @param viewType
     * @return SuggestionHolder
     */
    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_custom_suggestion, parent, false);
        return new SuggestionHolder(view);
    }

    /**
     *
     * @param suggestion
     * @param holder
     * @param position
     */
    @Override
    public void onBindSuggestionHolder(AddressWrapper suggestion, SuggestionHolder holder, int position) {
        holder.firstAddress.setText(suggestion.getAddressInEnglish());
        holder.secondAddress.setText(suggestion.getAddressInHebrew());
    }

    /**
     * <b>Override to customize functionality</b>
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p>
     * <p>This method is usually implemented by {@link androidx.recyclerview.widget.RecyclerView.Adapter}
     * classes.</p>
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if (term.isEmpty())
                    suggestions = suggestions_clone;
                else {
                    suggestions = new ArrayList<>();
                    int MAX = 0;
                    for (AddressWrapper item: suggestions_clone)
                        if (item.getAddressInHebrew().contains(term.toLowerCase()) || item.getAddressInEnglish().toLowerCase().contains(term.toLowerCase())) {
                            suggestions.add(item);
                            MAX++;
                            if (MAX == 4) break;
                        }
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (ArrayList<AddressWrapper>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    /**
     *
     * @param context
     * @return List<AddressWrapper>
     */
    public List<AddressWrapper> initSuggestions(Context context) {
        if (addressWrapperList.isEmpty()) {
            String jsonString = loadJson(context);
            addressWrapperList = deserializeColors(jsonString);
        }
        return addressWrapperList;
    }

    /**
     *
     * @param context
     * @return
     */
    private static String loadJson(Context context) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(ADDRESSES_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonString;
    }

    /**
     *
     * @param jsonString
     * @return List<AddressWrapper>
     */
    private static List<AddressWrapper> deserializeColors(String jsonString) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<AddressWrapper>>() {
        }.getType();
        return gson.fromJson(jsonString, collectionType);
    }

    /**
     *
     */
    static class SuggestionHolder extends RecyclerView.ViewHolder{
        protected TextView firstAddress;
        protected TextView secondAddress;

        public SuggestionHolder(View itemView) {
            super(itemView);
            firstAddress = (TextView) itemView.findViewById(R.id.first_address_search);
            secondAddress = (TextView) itemView.findViewById(R.id.second_address_search);
        }
    }

}
