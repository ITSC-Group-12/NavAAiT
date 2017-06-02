package com.team12.navaait.listeners;

import android.content.Context;
import android.util.Log;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.team12.navaait.domain.NavSearchSuggestion;
import com.team12.navaait.services.UserService;
import com.team12.navaait.util.DataHelper;

import java.util.List;

/**
 * Created by Sam on 5/24/2017.
 */

public class SearchViewOnQueryChangeListener implements FloatingSearchView.OnQueryChangeListener {

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
            UserService.search(context, newQuery, new DataHelper.OnFindSuggestionsListener() {

                @Override
                public void onResults(List<NavSearchSuggestion> results) {

                    //this will swap the data and render the collapse/expand animations as necessary
                    mSearchView.swapSuggestions(results);

                    //let the users know that the background process has completed
                    mSearchView.hideProgress();
                }
            });

        }

        Log.d(TAG, "onSearchTextChanged()");
    }
}
