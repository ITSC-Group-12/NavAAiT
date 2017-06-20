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
                new LocationSuggestion(new Location(9.0410, 38.7630, "Library and computer center complex","The Library is where Educational books and tools are found.It is open 24/7 so that students can study over night too.")),
                new LocationSuggestion(new Location(9.0409, 38.7627, "School of Chemical and bio-engineering")),
                new LocationSuggestion(new Location(9.0411, 38.7626, "Janator", "")),
                new LocationSuggestion(new Location(9.0409, 38.7626, "Dean, School of chemical and bio engineering")),
                new LocationSuggestion(new Location(9.0408, 38.7626, "Secretary, school of chemical and bio engineering")),
                new LocationSuggestion(new Location(9.0405, 38.7528, "Sece project room")),
                new LocationSuggestion(new Location(9.0404, 38.7627, "Instrumentation lab")),
                new LocationSuggestion(new Location(9.0406, 38.7627, "Elec janator")),
                new LocationSuggestion(new Location(9.0406, 38.7627, "Applied electronics lab")),
                new LocationSuggestion(new Location(9.0403, 38.7529, "Electrical store")),
                new LocationSuggestion(new Location(9.0402, 38.7529, "Basic electric circuit lab")),
                new LocationSuggestion(new Location(9.0401, 38.7628, "Janator next to conference room")),
                new LocationSuggestion(new Location(9.0400, 38.7628, "Conference room")),
                new LocationSuggestion(new Location(9.0400, 38.7629, "Staff lounge")),
                new LocationSuggestion(new Location(9.0399, 38.7628, "Extension coordinators")),
                new LocationSuggestion(new Location(9.0398, 38.7628, "Extension office")),
                new LocationSuggestion(new Location(9.0398, 38.7630, "Property and store")),
                new LocationSuggestion(new Location(9.0398, 38.7629, "Director of research, technology transfer and industry linkage office")),
                new LocationSuggestion(new Location(9.0397, 38.7628, "Telephone operator")),
                new LocationSuggestion(new Location(9.0397, 38.7631, "Aait administration")),
                new LocationSuggestion(new Location(9.0397, 38.7634, "Registral")),
                new LocationSuggestion(new Location(9.0398, 38.7636, "Budget finance and procurement department ")),
                new LocationSuggestion(new Location(9.0398, 38.7636, "Duplication/printing house")),
                new LocationSuggestion(new Location(9.0400, 38.7637, "Book store")),
                new LocationSuggestion(new Location(9.0398, 38.7637, "Aait adminstration 2")),
                new LocationSuggestion(new Location(9.0401, 38.7636, "Thermal laboratory")),
                new LocationSuggestion(new Location(9.0401, 38.7636, "Geotechnical engineering laboratory")),
                new LocationSuggestion(new Location(9.0403, 38.7636, "Hydraulics laboratory")),
                new LocationSuggestion(new Location(9.0406, 38.7635, "Mechanical engineering workshop")),
                new LocationSuggestion(new Location(9.0403, 38.7635, "General service and property maintenance department")),
                new LocationSuggestion(new Location(9.0402, 38.7634, "Cashier 1")),
                new LocationSuggestion(new Location(9.0402, 38.7633, "It support office")),
                new LocationSuggestion(new Location(9.0402, 38.7632, "Thermal and mass transfer laboratory")),
                new LocationSuggestion(new Location(9.0402, 38.7631, "Cost sharing office")),
                new LocationSuggestion(new Location(9.0402, 38.7631, "Secretary, pre engineering and Common courses office")),
                new LocationSuggestion(new Location(9.0402, 38.7631, "Head, pre engineering and Common courses office")),
                new LocationSuggestion(new Location(9.0402, 38.7630, "Cashier 2")),
                new LocationSuggestion(new Location(9.0402, 38.7630, "Cashier 3")),
                new LocationSuggestion(new Location(9.0402, 38.7630, "Mechanical unit operation laboratory")),
                new LocationSuggestion(new Location(9.0402, 38.7630, "Chemistry laboratory")),
                new LocationSuggestion(new Location(9.0401, 38.7630, "Reaction engineering laboratory")),
                new LocationSuggestion(new Location(9.0401, 38.7633, "Green Area")),
                new LocationSuggestion(new Location(9.0406, 38.7632, "Green Area 1")),
                new LocationSuggestion(new Location(9.0414, 38.7636, "Samsung Building"))
                ));

        for (User s : users) {
            sNavSearchSuggestions.add(new UserSuggestion(s));
        }
        new SearchFilter(sNavSearchSuggestions, limit, users, listener).filter(query);

    }

}
