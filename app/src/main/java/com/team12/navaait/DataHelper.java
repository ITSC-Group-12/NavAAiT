package com.team12.navaait;

import android.content.Context;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zee on 5/17/2017.
 */

public class DataHelper {

    private static final String Location_File_Name = "locations.json";

    private static List<NameWrapper> sNameWrappers = new ArrayList<>();

    private static List<NameSuggestion> sNameSuggestions =  new ArrayList<>(Arrays.asList(
            new NameSuggestion(new Location(9.0410,38.7630, "library")),
            new NameSuggestion(new Location(9.0406,38.7631,"Something"))));



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

    public static void findLocations(Context context, String query, final OnFindNamesListener listener) {
        initNameWrapperList(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<NameWrapper> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (NameWrapper name : sNameWrappers) {
                        if (name.getName().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(name);
                        }
                    }

                }

                FilterResults results = new FilterResults();
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

    private static void initNameWrapperList(Context context) {

        if (sNameWrappers.isEmpty()) {
            String jsonString = loadJson(context);
            sNameWrappers = deserializeColors(jsonString);
        }
    }
    private static String loadJson(Context context) {

        String jsonString;

        try {
            InputStream is = context.getAssets().open(Location_File_Name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonString;
    }




    private static List<NameWrapper> deserializeColors(String jsonString) {

        Gson gson = new Gson();

        Type collectionType = new TypeToken<List<NameWrapper>>() {
        }.getType();
        return gson.fromJson(jsonString, collectionType);
    }

}
