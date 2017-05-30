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

public class NameSuggestion implements SearchSuggestion {
    private boolean mIsHistory = false;

    public NameSuggestion(Location source) {

    }

    public NameSuggestion(User user) {

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

    public static final Creator<NameSuggestion> CREATOR = new Creator<NameSuggestion>() {
        @Override
        public NameSuggestion createFromParcel(Parcel in) {
            return new NameSuggestion(in);
        }

        @Override
        public NameSuggestion[] newArray(int size) {
            return new NameSuggestion[size];
        }
    };
}