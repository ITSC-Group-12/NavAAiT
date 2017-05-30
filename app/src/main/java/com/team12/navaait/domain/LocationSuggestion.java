package com.team12.navaait.domain;

/**
 * Created by Sam on 5/17/2017.
 */

public class LocationSuggestion extends NameSuggestion {

    private Location location = new Location();

    public LocationSuggestion(Location location) {
        super(location);
        this.location.setLatitude(location.getLatitude());
        this.location.setLongitude(location.getLongitude());
        this.location.setName(location.getName());
    }

}
