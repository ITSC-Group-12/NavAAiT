package com.team12.navaait.listeners;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.team12.navaait.domain.LocationSuggestion;
import com.team12.navaait.domain.NameSuggestion;

/**
 * Created by Sam on 5/24/2017.
 */

public class SearchViewOnSearchListener implements FloatingSearchView.OnSearchListener {

    private static final String TAG = "SearchListener";
    private LocationDisplay mLocationDisplay;
    private String mLastQuery;
    private Callout mCallout;
    private MapView mMapView;

    public SearchViewOnSearchListener(LocationDisplay mLocationDisplay, String mLastQuery, MapView mMapView) {
        this.mLocationDisplay = mLocationDisplay;
        this.mLastQuery = mLastQuery;
        this.mMapView = mMapView;
    }

    @Override
    public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {


        LocationSuggestion nameSuggestion = (LocationSuggestion) searchSuggestion;

        Point mapPoint = new Point(nameSuggestion.getLocation().getLatitude(), nameSuggestion.getLocation().getLongitude(), SpatialReferences.getWgs84());
        // convert to WGS84 for lat/lon format
        Point wgs84Point = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());


        TextView calloutContent = new TextView(mMapView.getContext());
        calloutContent.setTextColor(Color.BLACK);
        calloutContent.setSingleLine();
        // format coordinates to 4 decimal places
        calloutContent.setText("Lat: " + String.format("%.4f", wgs84Point.getY()) +
                ", Lon: " + String.format("%.4f", wgs84Point.getX()));

        // get callout, set content and show
        mCallout = mMapView.getCallout();
        mCallout.setLocation(mapPoint);
        mCallout.setContent(calloutContent);
        mCallout.show();
        // center on tapped point
        mMapView.setViewpointCenterAsync(mapPoint);


        mLastQuery = searchSuggestion.getBody();


    }

    @Override
    public void onSearchAction(String query) {
        mLastQuery = query;

        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();

//
        Log.d(TAG, "onSearchAction()");
    }
}
