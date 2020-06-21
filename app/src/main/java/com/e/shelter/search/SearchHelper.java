package com.e.shelter.search;

import android.content.Context;
import android.util.Log;
import android.widget.Filter;

import com.e.shelter.utilities.AddressWrapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchHelper {
    private static final String ADDRESSES_FILE_NAME = "addresses.json";

    private static List<AddressWrapper> addressWrapperList = new ArrayList<>();

    public SearchHelper(Context context) {
        initAddressesList(context);
    }

    private static List<AddressSuggestion> addressSuggestions = new ArrayList<>();

    public interface OnFindAddressListener {
        void onResults(List<AddressWrapper> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<AddressSuggestion> results);
    }

    /**
     * getting all addresses
     * @param context
     * @param count
     * @return
     */
    public static List<AddressSuggestion> getHistory(Context context, int count) {
        List<AddressSuggestion> suggestionList = new ArrayList<>();
        AddressSuggestion colorSuggestion;
        for (int i = 0; i < addressSuggestions.size(); i++) {
            colorSuggestion = addressSuggestions.get(i);
            colorSuggestion.setIsHistory(true);
            suggestionList.add(colorSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }


    public static void resetSuggestionsHistory() {
        for (AddressSuggestion colorSuggestion : addressSuggestions) {
            colorSuggestion.setIsHistory(false);
        }
    }

    /**
     *
     * @param context
     * @param query
     * @param limit
     * @param simulatedDelay
     * @param listener
     */
    public void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //SearchHelper.resetSuggestionsHistory();
                List<AddressSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {
                    for (AddressSuggestion suggestion : addressSuggestions) {
                        if (suggestion.getBody().toLowerCase().contains(constraint.toString().toLowerCase())
                                || suggestion.getBodyHebrew().contains(constraint.toString())) {
                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                //Collections.sort(suggestionList, new Comparator<AddressSuggestion>() {
                //    @Override
                //    public int compare(AddressSuggestion lhs, AddressSuggestion rhs) {
                //        return lhs.getIsHistory() ? -1 : 0;
                //    }
                //});
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (listener != null) {
                    listener.onResults((List<AddressSuggestion>) results.values);
                }
            }
        }.filter(query);

    }

    /**
     * Finding address after search input.
     * @param context
     * @param query
     * @param limit
     * @param listener
     */
    public void findAddresses(Context context, String query, final int limit, final OnFindAddressListener listener) {
        new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<AddressWrapper> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {
                    for (AddressSuggestion addressSuggestion : addressSuggestions) {
                        if (addressSuggestion.getBody().toLowerCase().equals(constraint.toString().toLowerCase())
                        || addressSuggestion.getBodyHebrew().equals(constraint.toString())) {
                            suggestionList.add(addressSuggestion.getAddressWrapper());
                        }
                        if (suggestionList.size() == limit) break;
                    }
                }
                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (listener != null) {
                    listener.onResults((List<AddressWrapper>) results.values);
                }
            }
        }.filter(query);

    }

    /**
     *
     * @param context
     */
    public static void initAddressesList(Context context) {
        if (addressWrapperList.isEmpty()) {
            String jsonString = loadJson(context);
            addressWrapperList = deserializeAddresses(jsonString);
            for (int i = 0 ; i < addressWrapperList.size() ; i++) {
                addressSuggestions.add(new AddressSuggestion(addressWrapperList.get(i)));
            }
            Log.i("Init address list", "done");

        }
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
     * @return
     */
    private static List<AddressWrapper> deserializeAddresses(String jsonString) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<AddressWrapper>>() {
        }.getType();
        return gson.fromJson(jsonString, collectionType);
    }
}
