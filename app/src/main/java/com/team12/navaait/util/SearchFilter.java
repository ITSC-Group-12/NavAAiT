package com.team12.navaait.util;

import android.widget.Filter;

import com.team12.navaait.domain.NavSearchSuggestion;
import com.team12.navaait.domain.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Created by Sam on 5/30/2017.
 */

public class SearchFilter extends Filter {

    private List<NavSearchSuggestion> sNavSearchSuggestions;
    private int limit;
    private DataHelper.OnFindSuggestionsListener listener;
    private Set<User> users;

    public SearchFilter(List<NavSearchSuggestion> sNavSearchSuggestions, int limit, Set<User> users, DataHelper.OnFindSuggestionsListener listener) {
        this.sNavSearchSuggestions = sNavSearchSuggestions;
        this.limit = limit;
        this.users = users;
        this.listener = listener;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        List<NavSearchSuggestion> suggestionList = new ArrayList<>();
        for (NavSearchSuggestion navSearchSuggestion : sNavSearchSuggestions) {
            navSearchSuggestion.setIsHistory(false);
        }
        if (!(constraint == null || constraint.length() == 0)) {

            for (NavSearchSuggestion suggestion : sNavSearchSuggestions) {
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
        Collections.sort(suggestionList, new Comparator<NavSearchSuggestion>() {
            @Override
            public int compare(NavSearchSuggestion lhs, NavSearchSuggestion rhs) {
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
            listener.onResults((List<NavSearchSuggestion>) results.values);
        }
    }
}
