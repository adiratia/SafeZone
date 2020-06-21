package com.e.shelter.search;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public interface SearchSuggestion extends Parcelable {

    /**
     * Returns the address that should be displayed in english
     * for the suggestion represented by this object.
     * @return the text for this suggestion
     */
    String getBody();

    /**
     * Returns the address that should be displayed in hebrew
     * for the suggestion represented by this object.
     * @return the text for this suggestion
     */
    String getBodyHebrew();


    LatLng getLocation();

}
