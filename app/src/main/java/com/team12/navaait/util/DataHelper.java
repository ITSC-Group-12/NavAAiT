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
                new LocationSuggestion(new Location(9.0410, 38.7630, "Library and Computer Center Complex",
                        "The Library is where Educational books and tools are found.It is open 24/7 so that students can study over night too.")),
                new LocationSuggestion(new Location(9.0409, 38.7627, "School of Chemical and Bio-Engineering")),
                new LocationSuggestion(new Location(9.0411, 38.7626, "Janitor",
                        "")),
                new LocationSuggestion(new Location(9.0409, 38.7626, "Dean, School of Chemical and Bio-Engineering")),
                new LocationSuggestion(new Location(9.0408, 38.7626, "Secretary, School of Chemical and Bio-Engineering")),
                new LocationSuggestion(new Location(9.0405, 38.7628, "SECE Project Room")),
                new LocationSuggestion(new Location(9.0404, 38.7627, "Instrumentation Lab")),
                new LocationSuggestion(new Location(9.0406, 38.7627, "Electrical Janitor")),
                new LocationSuggestion(new Location(9.0406, 38.7627, "Applied Electronics Lab")),
                new LocationSuggestion(new Location(9.0403, 38.7529, "Electrical Store")),
                new LocationSuggestion(new Location(9.0402, 38.7529, "Basic Electric Circuit Lab")),
                new LocationSuggestion(new Location(9.0401, 38.7628, "Janitor next to Conference Room")),
                new LocationSuggestion(new Location(9.0400, 38.7628, "Conference Room")),
                new LocationSuggestion(new Location(9.0400, 38.7629, "Staff Lounge")),
                new LocationSuggestion(new Location(9.0399, 38.7628, "Extension Coordinators")),
                new LocationSuggestion(new Location(9.0398, 38.7628, "Extension Office")),
                new LocationSuggestion(new Location(9.0398, 38.7630, "Property and Store")),
                new LocationSuggestion(new Location(9.0398, 38.7629, "Director of Research, Technology Transfer and Industry Linkage Office")),
                new LocationSuggestion(new Location(9.0397, 38.7628, "Telephone Operator")),
                new LocationSuggestion(new Location(9.0397, 38.7631, "AAiT Administration")),
                new LocationSuggestion(new Location(9.0397, 38.7634, "Registrar")),
                new LocationSuggestion(new Location(9.0398, 38.7636, "Budget, Finance and Procurement Department")),
                new LocationSuggestion(new Location(9.0398, 38.7636, "Duplication/Printing House")),
                new LocationSuggestion(new Location(9.0400, 38.7637, "Book Store")),
                new LocationSuggestion(new Location(9.0398, 38.7637, "AAiT Administration 2")),
                new LocationSuggestion(new Location(9.0401, 38.7636, "Thermal Laboratory")),
                new LocationSuggestion(new Location(9.0401, 38.7636, "Geo-Technical Engineering Laboratory")),
                new LocationSuggestion(new Location(9.0403, 38.7636, "Hydraulics Laboratory")),
                new LocationSuggestion(new Location(9.0406, 38.7635, "Mechanical Engineering Workshop")),
                new LocationSuggestion(new Location(9.0403, 38.7635, "General Service and Property Maintenance Department")),
                new LocationSuggestion(new Location(9.0402, 38.7634, "Cashier 1")),
                new LocationSuggestion(new Location(9.0402, 38.7633, "IT Support Office")),
                new LocationSuggestion(new Location(9.0402, 38.7632, "Thermal and Mass Transfer Laboratory")),
                new LocationSuggestion(new Location(9.0402, 38.7631, "Cost Sharing office")),
                new LocationSuggestion(new Location(9.0402, 38.7631, "Secretary, Pre-Engineering and Common Courses Office")),
                new LocationSuggestion(new Location(9.0402, 38.7631, "Head, Pre-Engineering and Common courses Office")),
                new LocationSuggestion(new Location(9.0402, 38.7630, "Cashier 2")),
                new LocationSuggestion(new Location(9.0402, 38.7630, "Cashier 3")),
                new LocationSuggestion(new Location(9.0402, 38.7630, "Mechanical Unit Operation Laboratory")),
                new LocationSuggestion(new Location(9.0402, 38.7630, "Chemistry Laboratory")),
                new LocationSuggestion(new Location(9.0401, 38.7630, "Reaction Engineering Laboratory")),
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
