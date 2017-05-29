package com.team12.navaait.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.indooratlas.android.sdk.IALocation;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAFloorPlan;
import com.indooratlas.android.sdk.resources.IALatLng;
import com.indooratlas.android.sdk.resources.IALocationListenerSupport;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.indooratlas.android.sdk.resources.IAResult;
import com.indooratlas.android.sdk.resources.IAResultCallback;
import com.indooratlas.android.sdk.resources.IATask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * Created by Sam on 5/26/2017.
 */

public class Indoor {

    private static final int MAX_DIMENSION = 0;
    private static final String TAG = "Indoor";
    private GroundOverlay mGroundOverlay = null;
    private MapView mMapView2;
    private Target mLoadTarget;
    private Context context;
    private IARegion mOverlayFloorPlan = null;
    private IAResourceManager mResourceManager;
    private IATask<IAFloorPlan> mFetchFloorPlanTask;
    private GroundOverlay mBlueDot = null;
    private boolean mCameraPositionNeedsUpdating = true; // update on first location
    /**
     * Listener that handles location change events.
     */
    private IALocationListener mListener;
    /**
     * Listener that changes overlay if needed
     */
    private IARegion.Listener mRegionListener;

    public Indoor(MapView mMV, Context c, IAResourceManager mRM) {
        this.mMapView2 = mMV;
        this.context = c;
        this.mResourceManager = mRM;

        mListener = new IALocationListenerSupport() {

            /**
             * Location changed, move marker and camera position.
             */
            @Override
            public void onLocationChanged(IALocation location) {

                Log.d(TAG, "new location received with coordinates: " + location.getLatitude()
                        + "," + location.getLongitude());

                if (mMapView2 == null) {
                    // location received before map is initialized, ignoring update here
                    return;
                }

                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());

                if (mBlueDot == null) {

                    mBlueDot = new GroundOverlay();
//                mBlueDot.setImage(getResources().getDrawable(R.drawable.circle));
                    mBlueDot.setTransparency(0.5f);

                } else {
                    // move existing markers position to received location
                    //mBlueDot.setPosition(geoPoint);
                    mMapView2.getOverlays().remove(mBlueDot);
                }
                mBlueDot.setPosition(geoPoint);
                mBlueDot.setDimensions(location.getAccuracy(), location.getAccuracy());

                // add to top
                mMapView2.getOverlays().add(mBlueDot);

                // our camera position needs updating if location has significantly changed

                if (mCameraPositionNeedsUpdating) {
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.5f));
                    mMapView2.getController().setCenter(geoPoint);
                    mCameraPositionNeedsUpdating = false;
                }

                mMapView2.invalidate();
            }
        };

        mRegionListener = new IARegion.Listener() {

            @Override
            public void onEnterRegion(IARegion region) {
                if (region.getType() == IARegion.TYPE_FLOOR_PLAN) {
                    final String newId = region.getId();
                    // Are we entering a new floor plan or coming back the floor plan we just left?
                    if (mGroundOverlay == null || !region.equals(mOverlayFloorPlan)) {
                        mCameraPositionNeedsUpdating = true; // entering new fp, need to move camera
                        if (mGroundOverlay != null) {
                            mMapView2.getOverlays().remove(mGroundOverlay);
                            mGroundOverlay = null;
                        }
                        mOverlayFloorPlan = region; // overlay will be this (unless error in loading)
                        fetchFloorPlan(newId);
                    } else {
                        mGroundOverlay.setTransparency(0.0f);
                    }
                    Toast.makeText(context, region.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onExitRegion(IARegion region) {
                if (mGroundOverlay != null) {
                    // Indicate we left this floor plan but leave it there for reference
                    // If we enter another floor plan, this one will be removed and another one loaded
                    mGroundOverlay.setTransparency(0.5f);
                }
            }

        };


    }

    public IALocationListener getmListener() {
        return mListener;
    }

    public void setmListener(IALocationListener mListener) {
        this.mListener = mListener;
    }

    public IARegion.Listener getmRegionListener() {
        return mRegionListener;
    }

    public void setmRegionListener(IARegion.Listener mRegionListener) {
        this.mRegionListener = mRegionListener;
    }


    /**
     * Sets bitmap of floor plan as ground overlay on Open Street Map
     */
    private void setupGroundOverlay(IAFloorPlan floorPlan, Bitmap bitmap) {

        if (mGroundOverlay != null) {
            mMapView2.getOverlays().remove(mGroundOverlay);
        }

        if (mMapView2 != null) {
            IALatLng iaLatLng = floorPlan.getCenter();

            GroundOverlay overlay = new GroundOverlay();
            overlay.setImage(new BitmapDrawable(context.getResources(), bitmap));
            overlay.setPosition(new GeoPoint(iaLatLng.latitude, iaLatLng.longitude));
            overlay.setDimensions(floorPlan.getWidthMeters(), floorPlan.getHeightMeters());
            overlay.setBearing(floorPlan.getBearing());

            mGroundOverlay = overlay;

            mMapView2.getOverlays().add(mGroundOverlay);
        }
    }

    /**
     * Download floor plan using Picasso library.
     */
    private void fetchFloorPlanBitmap(final IAFloorPlan floorPlan) {

        final String url = floorPlan.getUrl();

        if (mLoadTarget == null) {
            mLoadTarget = new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d(TAG, "onBitmap loaded with dimensions: " + bitmap.getWidth() + "x"
                            + bitmap.getHeight());
                    setupGroundOverlay(floorPlan, bitmap);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                    // N/A
                }

                @Override
                public void onBitmapFailed(Drawable placeHolderDraweble) {
                    Toast.makeText(context, "Failed to load bitmap",
                            Toast.LENGTH_SHORT).show();
                    mOverlayFloorPlan = null;
                }
            };
        }

        RequestCreator request = Picasso.with(context).load(url);

        final int bitmapWidth = floorPlan.getBitmapWidth();
        final int bitmapHeight = floorPlan.getBitmapHeight();

        if (bitmapHeight > MAX_DIMENSION) {
            request.resize(0, MAX_DIMENSION);
        } else if (bitmapWidth > MAX_DIMENSION) {
            request.resize(MAX_DIMENSION, 0);
        }

        request.into(mLoadTarget);
    }


    /**
     * Fetches floor plan data from IndoorAtlas server.
     */
    private void fetchFloorPlan(String id) {

        // if there is already running task, cancel it
        cancelPendingNetworkCalls();

        final IATask<IAFloorPlan> task = mResourceManager.fetchFloorPlanWithId(id);

        task.setCallback(new IAResultCallback<IAFloorPlan>() {

            @Override
            public void onResult(IAResult<IAFloorPlan> result) {

                if (result.isSuccess() && result.getResult() != null) {
                    // retrieve bitmap for this floor plan metadata
                    fetchFloorPlanBitmap(result.getResult());
                } else {
                    // ignore errors if this task was already canceled
                    if (!task.isCancelled()) {
                        // do something with error
                        Toast.makeText(context,
                                "loading floor plan failed: " + result.getError(), Toast.LENGTH_LONG)
                                .show();
                        mOverlayFloorPlan = null;
                    }
                }
            }
        }, Looper.getMainLooper()); // deliver callbacks using main looper

        // keep reference to task so that it can be canceled if needed
        mFetchFloorPlanTask = task;

    }

    /**
     * Helper method to cancel current task if any.
     */
    private void cancelPendingNetworkCalls() {
        if (mFetchFloorPlanTask != null && !mFetchFloorPlanTask.isCancelled()) {
            mFetchFloorPlanTask.cancel();
        }
    }
}
