package com.team12.navaait.domain;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Copyright (C) 2015 Ari C.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class NavSearchSuggestion implements SearchSuggestion {

    private boolean isLocation;
    private boolean isUser;
    private boolean mIsHistory = false;

    public NavSearchSuggestion(Location location, boolean isLocation) {
        this.isLocation = isLocation;
    }

    public NavSearchSuggestion(User user, boolean isUser) {
        this.isUser = isUser;
    }

    public NavSearchSuggestion(Parcel source) {

    }

    public boolean isLocation() {
        return isLocation;
    }

    public boolean isUser() {
        return isUser;
    }

    @Override
    public String getBody() {
        return null;
    }

    public boolean getIsHistory() {
        return mIsHistory;
    }

    public void setIsHistory(boolean history) {
        mIsHistory = history;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<NavSearchSuggestion> CREATOR = new Creator<NavSearchSuggestion>() {
        @Override
        public NavSearchSuggestion createFromParcel(Parcel in) {
            return new NavSearchSuggestion(in);
        }

        @Override
        public NavSearchSuggestion[] newArray(int size) {
            return new NavSearchSuggestion[size];
        }
    };
}