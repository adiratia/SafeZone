package com.e.shelter.search;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.e.shelter.utilities.AddressWrapper;
import com.google.android.gms.maps.model.LatLng;

public class AddressSuggestion implements SearchSuggestion {
    private AddressWrapper addressWrapper;
    private boolean isHistory = false;

    /**
     * AddressSuggestion constructor
     * @param addressWrapper
     */
    public AddressSuggestion(AddressWrapper addressWrapper) {
        this.addressWrapper = addressWrapper;
    }

    /**
     * AddressSuggestion constructor
     * @param source
     */
    public AddressSuggestion(Parcel source) {
        this.addressWrapper.setAddressInEnglish(source.readString());
        this.isHistory = source.readInt() != 0;
    }

    /**
     *
     * @param isHistory
     */
    public void setIsHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    /**
     *
     * @return boolean
     */
    public boolean getIsHistory() {
        return this.isHistory;
    }

    /**
     * call setting address in hebrew and english
     * @return String
     */
    @Override
    public String getBody() {
        return addressWrapper.getAddressInEnglish() + "\n" + addressWrapper.getAddressInHebrew();
    }

    /**
     * setting address in hebrew
     * @return
     */
    public String getBodyHebrew() {
        return addressWrapper.getAddressInHebrew();
    }


    public static final Creator<AddressSuggestion> CREATOR = new Creator<AddressSuggestion>() {
        @Override
        public AddressSuggestion createFromParcel(Parcel in) {
            return new AddressSuggestion(in);
        }

        @Override
        public AddressSuggestion[] newArray(int size) {
            return new AddressSuggestion[size];
        }
    };

    /**
     * counter
     * @return int
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(addressWrapper.getAddressInEnglish());
        dest.writeInt(isHistory ? 1 : 0);
    }

    /**
     * Get latitude and longitude of the the address
     * @return LatLng
     */
    public LatLng getLocation() {
        return new LatLng(Double.parseDouble(addressWrapper.getLat()), Double.parseDouble(addressWrapper.getLon()));
    }

    public AddressWrapper getAddressWrapper() {
        return addressWrapper;
    }
}
