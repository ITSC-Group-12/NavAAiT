package com.team12.navaait.listeners;

import android.util.Log;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;

/**
 * Created by Sam on 5/24/2017.
 */

public class SearchViewOnSearchListener implements FloatingSearchView.OnSearchListener {

    private static final String TAG = "SearchListener";
    private LocationDisplay mLocationDisplay;
    private String mLastQuery;

    public SearchViewOnSearchListener(LocationDisplay mLocationDisplay, String mLastQuery) {
        this.mLocationDisplay = mLocationDisplay;
        this.mLastQuery = mLastQuery;
    }

    @Override
    public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();

//                 LocationSuggestion colorSuggestion = (LocationSuggestion) searchSuggestion;
//                DataHelper.findColors(getActivity(), colorSuggestion.getBody(),
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                mSearchResultsAdapter.swapData(results);
//                            }
//
//                        });
        Log.d(TAG, "onSuggestionClicked()");

        mLastQuery = searchSuggestion.getBody();
    }

    @Override
    public void onSearchAction(String query) {
        mLastQuery = query;

        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();

//                DataHelper.findColors(getActivity(), query,
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                mSearchResultsAdapter.swapData(results);
//                            }
//
//                        });
        Log.d(TAG, "onSearchAction()");
    }
}
