package com.team12.navaait.util;

import android.content.Context;

import com.team12.navaait.domain.Location;
import com.team12.navaait.domain.LocationSuggestion;
import com.team12.navaait.domain.NavSearchSuggestion;
import com.team12.navaait.domain.User;
import com.team12.navaait.domain.UserSuggestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by zee on 5/17/2017.
 */

public class DataHelper {

    public interface OnFindNamesListener {
        void onResults(List<NavSearchSuggestion> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<NavSearchSuggestion> results);
    }

    public static void findSuggestions(Context context, String query, final int limit, Set<User> users, final OnFindSuggestionsListener listener) {

        List<NavSearchSuggestion> sNavSearchSuggestions = new ArrayList<NavSearchSuggestion>(Arrays.asList(
                new LocationSuggestion(new Location(9.0410, 38.7630, "library")),
                new LocationSuggestion(new Location(9.0406, 38.7631, "Something"))));

        for (User s : users) {
            sNavSearchSuggestions.add(new UserSuggestion(s));
        }
        new SearchFilter(sNavSearchSuggestions, limit, users, listener).filter(query);

    }

}
