package com.team12.navaait.domain;

/**
 * Created by Blen on 5/20/2017.
 */

public class Location {

    private String id;

    private double latitude;
    private double longitude;
    private String name;
    private String description;

    public Location(double latitude, double longitude, String name) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public Location(double latitude, double longitude, String name, String description) {

        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
    }

    public Location() {
    }

    public Location(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
