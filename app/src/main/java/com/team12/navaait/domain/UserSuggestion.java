package com.team12.navaait.domain;

/**
 * Created by zee on 5/30/2017.
 */

public class UserSuggestion extends NavSearchSuggestion {

    private User user = new User();

    public UserSuggestion(User user) {
        super(user, true);
        this.user.setFirstName(user.getFirstName());
        this.user.setLastName(user.getLastName());
        this.user.setDeviceId(user.getDeviceId());
        this.user.setLocation(user.getLocation());
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getBody() {
        return user.getFirstName() + " " + user.getLastName();
    }
}
