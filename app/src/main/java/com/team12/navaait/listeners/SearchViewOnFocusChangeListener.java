package com.team12.navaait.listeners;

import android.util.Log;

import com.arlib.floatingsearchview.FloatingSearchView;

/**
 * Created by Sam on 5/24/2017.
 */

public class SearchViewOnFocusChangeListener implements FloatingSearchView.OnFocusChangeListener {

    private static final String TAG = "FocusChangeListener";
    private FloatingSearchView mSearchView;
    private CharSequence mLastQuery;

    public SearchViewOnFocusChangeListener(FloatingSearchView mSearchView, CharSequence mLastQuery) {
        this.mSearchView = mSearchView;
        this.mLastQuery = mLastQuery;
    }

    @Override
    public void onFocus() {

        //show suggestions when search bar gains focus (typically history suggestions)
//                mSearchView.swapSuggestions(DataHelper.getHistory(getActivity(), 3));

        Log.d(TAG, "onFocus()");
    }

    @Override
    public void onFocusCleared() {

        //set the title of the bar so that when focus is returned a new query begins
        mSearchView.setSearchBarTitle(mLastQuery);

        //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
        //mSearchView.setSearchText(searchSuggestion.getBody());

        Log.d(TAG, "onFocusCleared()");
    }
}
