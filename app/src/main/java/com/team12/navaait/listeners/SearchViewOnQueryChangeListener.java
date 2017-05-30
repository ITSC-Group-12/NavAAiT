package com.team12.navaait.listeners;

import android.content.Context;
import android.util.Log;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.team12.navaait.util.DataHelper;
import com.team12.navaait.domain.NameSuggestion;

import java.util.List;

/**
 * Created by Sam on 5/24/2017.
 */

public class SearchViewOnQueryChangeListener implements FloatingSearchView.OnQueryChangeListener {

    private static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private static final String TAG = "QueryChangeListener";
    private FloatingSearchView mSearchView;
    private Context context;

    public SearchViewOnQueryChangeListener(FloatingSearchView mSearchView, Context context) {
        this.mSearchView = mSearchView;
        this.context = context;
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        if (!oldQuery.equals("") && newQuery.equals("")) {
            mSearchView.clearSuggestions();
        } else {

            //this shows the top left circular progress you can call it where ever you want, but
            //it makes sense to do it when loading something in the background.
            mSearchView.showProgress();

            //simulates a query call to a data source with a new query.
            DataHelper.findSuggestions(context, newQuery, 5,
                    FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                        @Override
                        public void onResults(List<NameSuggestion> results) {

                            //this will swap the data and
                            //render the collapse/expand animations as necessary
                            mSearchView.swapSuggestions(results);

                            //let the users know that the background
                            //process has completed
                            mSearchView.hideProgress();
                        }
                    });
        }

        Log.d(TAG, "onSearchTextChanged()");
    }
}
