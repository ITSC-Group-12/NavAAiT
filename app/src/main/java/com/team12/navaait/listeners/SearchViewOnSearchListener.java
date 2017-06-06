package com.team12.navaait.listeners;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.team12.navaait.MainActivity;
import com.team12.navaait.domain.LocationSuggestion;
import com.team12.navaait.domain.NavSearchSuggestion;
import com.team12.navaait.domain.UserSuggestion;

/**
 * Created by Sam on 5/24/2017.
 */

public class SearchViewOnSearchListener implements FloatingSearchView.OnSearchListener {

    private static final String TAG = "SearchListener";
    private final TextView slideUpLocationLabel;
    private LocationDisplay mLocationDisplay;
    private String mLastQuery;
    private Callout mCallout;
    private MapView mMapView;
    private SlidingUpPanelLayout mSlideUpPanel;
    private FloatingActionButton closeAction;
    private Context context;
    private MainActivity mainActivity;

    public SearchViewOnSearchListener(LocationDisplay mLocationDisplay, String mLastQuery, MapView mMapView, TextView slideUpLocationLabel, SlidingUpPanelLayout mSlideUpPanel, FloatingActionButton closeAction, Context context, MainActivity mainActivity) {
        this.mLocationDisplay = mLocationDisplay;
        this.mLastQuery = mLastQuery;
        this.mMapView = mMapView;
        this.slideUpLocationLabel = slideUpLocationLabel;
        this.mSlideUpPanel = mSlideUpPanel;
        this.closeAction = closeAction;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

        NavSearchSuggestion suggestion = (NavSearchSuggestion) searchSuggestion;
        LocationSuggestion locationSuggestion;
        UserSuggestion userSuggestion;
        Point mapPoint = null;

        TextView calloutContent = new TextView(mMapView.getContext());
        calloutContent.setTextColor(Color.BLACK);
        calloutContent.setSingleLine();

        if (suggestion.isLocation()) {

            locationSuggestion = (LocationSuggestion) searchSuggestion;
            mapPoint = new Point(locationSuggestion.getLocation().getLongitude(), locationSuggestion.getLocation().getLatitude(), SpatialReferences.getWgs84());
            slideUpLocationLabel.setText(locationSuggestion.getLocation().getName());

            calloutContent.setText(locationSuggestion.getLocation().getName());

        } else if (suggestion.isUser()) {

            userSuggestion = (UserSuggestion) searchSuggestion;
            if (userSuggestion.getUser().getLocation() != null){

                mapPoint = new Point(userSuggestion.getUser().getLocation().getLatitude(), userSuggestion.getUser().getLocation().getLongitude(), SpatialReferences.getWgs84());
                slideUpLocationLabel.setText(userSuggestion.getUser().getFirstName() + " " + userSuggestion.getUser().getLastName() + "'s Last Known Location");
                calloutContent.setText(userSuggestion.getUser().getFirstName() + " " + userSuggestion.getUser().getLastName() + "'s Last Known Location");
            }
            else{
                Toast.makeText(context, "User location could not be determined!", Toast.LENGTH_SHORT).show();
            }
        }

        // convert to WGS84 for lat/lon format
        Point wgs84Point = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());


        // format coordinates to 4 decimal places


        // get callout, set content and show
        mCallout = mMapView.getCallout();
        mCallout.setLocation(mapPoint);
        mCallout.setContent(calloutContent);
        mCallout.show();
        // center on tapped point
        mMapView.setViewpointCenterAsync(mapPoint);
        mainActivity.setEndingPoint(mapPoint);
        mLastQuery = searchSuggestion.getBody();
        mSlideUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        if (!closeAction.isShown()) {
            closeAction.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSearchAction(String query) {
        mLastQuery = query;

        mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
        if (!mLocationDisplay.isStarted())
            mLocationDisplay.startAsync();

        Log.d(TAG, "onSearchAction()");
    }
}
