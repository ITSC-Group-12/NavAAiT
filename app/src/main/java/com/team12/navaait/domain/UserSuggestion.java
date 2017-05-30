package com.team12.navaait.domain;

/**
 * Created by zee on 5/30/2017.
 */

public class UserSuggestion extends NameSuggestion {


    private User user = new User();

    public UserSuggestion(User user) {
        super(user);
//        this.location.setLatitude(location.getLatitude());
//        this.location.setLongitude(location.getLongitude());
//        this.location.setName(location.getName());
    }
}
