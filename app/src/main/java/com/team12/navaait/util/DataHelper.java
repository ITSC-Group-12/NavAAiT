package com.team12.navaait.util;

import android.content.Context;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.team12.navaait.domain.Location;
import com.team12.navaait.domain.LocationSuggestion;
import com.team12.navaait.domain.NameSuggestion;

/**
 * Created by zee on 5/17/2017.
 */

public class DataHelper {


    private static List<NameSuggestion> sNameSuggestions = new ArrayList<NameSuggestion>(Arrays.asList(
            new LocationSuggestion(new Location(9.0410, 38.7630, "library")),
            new LocationSuggestion(new Location(9.0406, 38.7631, "Something"))));


    public interface OnFindNamesListener {
        void onResults(List<NameSuggestion> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<NameSuggestion> results);
    }

    public static List<NameSuggestion> getHistory(Context context, int count) {

        List<NameSuggestion> suggestionList = new ArrayList<>();
        NameSuggestion nameSuggestion;
        for (int i = 0; i < sNameSuggestions.size(); i++) {
            nameSuggestion = sNameSuggestions.get(i);
            nameSuggestion.setIsHistory(true);
            suggestionList.add(nameSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (NameSuggestion nameSuggestion : sNameSuggestions) {
            nameSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                List<NameSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (NameSuggestion suggestion : sNameSuggestions) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<NameSuggestion>() {
                    @Override
                    public int compare(NameSuggestion lhs, NameSuggestion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<NameSuggestion>) results.values);
                }
            }
        }.filter(query);

    }

}
