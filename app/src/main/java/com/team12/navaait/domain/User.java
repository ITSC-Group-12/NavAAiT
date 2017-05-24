package com.team12.navaait.domain;

/**
 * Created by Blen on 5/20/2017.
 */

public class User {
    private String id;

    private String firstName;
    private String lastName;
    private boolean isVisible = true;
    private String deviceId;

    private Location location;

    public User() {
    }

    public User(String id, String firstName, String lastName, String deviceId, boolean isVisible, Location location) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deviceId = deviceId;
        this.isVisible = isVisible;
        this.location = location;
    }

    public User(String id, String firstName, String lastName, String deviceId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
