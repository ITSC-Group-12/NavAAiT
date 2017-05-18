package com.team12.navaait.domain;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by Sam on 5/17/2017.
 */

public class LocationSuggestion implements SearchSuggestion {

    public LocationSuggestion(Parcel source) {

    }

    @Override
    public String getBody() {
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<LocationSuggestion> CREATOR = new Creator<LocationSuggestion>() {
        @Override
        public LocationSuggestion createFromParcel(Parcel in) {
            return new LocationSuggestion(in);
        }

        @Override
        public LocationSuggestion[] newArray(int size) {
            return new LocationSuggestion[size];
        }
    };
}
