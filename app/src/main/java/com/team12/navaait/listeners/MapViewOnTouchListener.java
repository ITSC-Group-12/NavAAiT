package com.team12.navaait.listeners;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * Created by Sam on 5/24/2017.
 */

public class MapViewOnTouchListener extends DefaultMapViewOnTouchListener {

    private MapView mMapView;
    private Context context;
    private Callout mCallout;
    private Point[] points;
    private static final String sTag = "Gesture";

    public MapViewOnTouchListener(MapView mMapView, Context context, Callout mCallout, Point[] points) {
        super(context, mMapView);
        this.mMapView = mMapView;
        this.context = context;
        this.mCallout = mCallout;
        this.points = points;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        Log.d(sTag, "onSingleTapConfirmed: " + motionEvent.toString());

        // get the point that was clicked and convert it to a point in map coordinates
        android.graphics.Point screenPoint = new android.graphics.Point(Math.round(motionEvent.getX()),
                Math.round(motionEvent.getY()));
        // create a map point from screen point
        Point mapPoint = mMapView.screenToLocation(screenPoint);
        // convert to WGS84 for lat/lon format
        Point wgs84Point = (Point) GeometryEngine.project(mapPoint, SpatialReferences.getWgs84());

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                points[i] = wgs84Point;
                break;
            }
        }
        // create a textview for the callout
        TextView calloutContent = new TextView(context);
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

        return true;
    }
}
