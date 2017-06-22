package com.team12.navaait.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.team12.navaait.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sam on 5/26/2017.
 */

public class Outdoor {

    private static final String FILE_EXTENSION = ".mmpk";
    private static final String TAG = "Outdoor";
    private static String extSDCardDirName;
    private static File extStorDir;
    private static String filename;
    private MobileMapPackage mapPackage;
    private MapView mMapView;
    private GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
    private RouteTask mRouteTask;
    private Graphic mRouteGraphic;
    private final SimpleLineSymbol mSolvedRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 4.0f);
    private Context context;

    public Outdoor(MapView mMapView, Context context) {
        this.mMapView = mMapView;
        this.context = context;

        // get sdcard resource name
        // get the directory
        // get mobile map package filename
        extStorDir = Environment.getExternalStorageDirectory();
        extSDCardDirName = context.getString(R.string.config_data_sdcard_offline_dir);
        filename = context.getResources().getString(R.string.config_mmpk_name);
    }

    public String createMobileMapPackageFilePath() {
        return extStorDir.getAbsolutePath() + File.separator + extSDCardDirName + File.separator + filename + FILE_EXTENSION;
    }


    /**
     * Load a mobile map package into a MapView
     */
    public void loadMobileMapPackage() {
        // create the mobile map package
        mapPackage = new MobileMapPackage(createMobileMapPackageFilePath());
        // load the mobile map package asynchronously
        mapPackage.loadAsync();

        // add done listener which will invoke when mobile map package has loaded
        mapPackage.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                // check load status and that the mobile map package has maps
                if (mapPackage.getLoadStatus() == LoadStatus.LOADED && mapPackage.getMaps().size() > 0) {
                    // add the map from the mobile map package to the MapView
                    mMapView.setMap(mapPackage.getMaps().get(0));
                    mMapView.setViewpointScaleAsync(3400.00);
                    mapPackage.getMaps().get(0).addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {

                            if (mapPackage.getLoadStatus() != LoadStatus.LOADED) {
                                Snackbar.make(mMapView, String.format(context.getString(R.string.object_not_loaded), "Map"),
                                        Snackbar.LENGTH_SHORT).show();
                                return;
                            }

                            setupOfflineNetwork(mapPackage.getMaps().get(0));
                            mapPackage.getMaps().get(0).removeDoneLoadingListener(this);
                            mMapView.getGraphicsOverlays().add(graphicsOverlay);
                            mMapView.getMap().setBasemap(Basemap.createStreetsVector());
                        }
                    });


                } else {
                    // Log an issue if the mobile map package fails to load
                    Log.e(TAG, mapPackage.getLoadError().getMessage());
                }
            }
        });
    }
    // Routing functions

    /**
     * Sets up a RouteTask from the NetworkDatasets in the current map. Shows a message to user if network dataset is
     * not found.
     */
    public void setupOfflineNetwork(ArcGISMap mMap) {

        if ((mMap.getTransportationNetworks() == null) || (mMap.getTransportationNetworks().size() < 1)) {
            Snackbar.make(mMapView, context.getString(R.string.network_dataset_not_found), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Create the RouteTask from network data set using same map used in display
        mRouteTask = new RouteTask(context, mMap.getTransportationNetworks().get(0));
        mRouteTask.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (mRouteTask.getLoadStatus() != LoadStatus.LOADED) {
                    Snackbar.make(mMapView, String.format(context.getString(R.string.object_not_loaded), "RouteTask"), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        mRouteTask.loadAsync();
    }

    /**
     * Solves a route using the existing geocoded address and hydrant locations, and displays a graphic of the route, and
     * message with distance and time. Shows messages to user if locations are not set.
     */
    public void solveRoute(Point starting, Point ending) {

        if (mRouteTask == null) {
            Snackbar.make(mMapView, context.getString(R.string.route_task_not_set), Snackbar.LENGTH_SHORT).show();
            return;
        }

        graphicsOverlay = new GraphicsOverlay();
        mMapView.getGraphicsOverlays().add(graphicsOverlay);
        // clear all drawn paths
        graphicsOverlay.clearSelection();

        RouteParameters routeParams;
        try {
            routeParams = mRouteTask.createDefaultParametersAsync().get();
            routeParams.setReturnDirections(true);

            Stop start = new Stop(starting);
            routeParams.getStops().add(start);

            Stop finish = new Stop(ending);
            routeParams.getStops().add(finish);

            final ListenableFuture<RouteResult> routeFuture = mRouteTask.solveRouteAsync(routeParams);

            routeFuture.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    // Show results of solved route.
                    RouteResult routeResult;
                    try {
                        if (routeFuture.isDone()) {
                            routeResult = routeFuture.get();
                            if (routeResult.getRoutes().size() > 0) {
                                //  Add first result to the map as a graphic.
                                Route topRoute = routeResult.getRoutes().get(0);
                                mRouteGraphic = new Graphic(topRoute.getRouteGeometry(), mSolvedRouteSymbol);
                                mRouteGraphic.setSelected(true);
                                mRouteGraphic.setVisible(true);

                                mRouteGraphic.setGeometry(topRoute.getRouteGeometry());
                                mRouteGraphic.setZIndex(5);
                                graphicsOverlay.getGraphics().add(mRouteGraphic);
                                graphicsOverlay.setVisible(true);
                                graphicsOverlay.setOpacity(1.0f);
                                graphicsOverlay.setSelectionColor(0x56467839);


                                // Display route distance and time.
                                Snackbar.make(mMapView, topRoute.getRouteName(), Snackbar.LENGTH_SHORT).show();

                                mMapView.setViewpointGeometryAsync(topRoute.getRouteGeometry());

                            }

                        }
                    } catch (InterruptedException | ExecutionException e) {
                        Snackbar.make(mMapView, String.format(context.getString(R.string.route_error), e.getMessage()),
                                Snackbar.LENGTH_SHORT).show();
                        Log.e("SOLVE", e.getMessage());
                    }
                }
            });
        } catch (InterruptedException | ExecutionException e) {
//            Snackbar.make(mMapView, String.format(context.getString(R.string.route_params_error), e.getMessage()),
//                    Snackbar.LENGTH_SHORT).show();
        }
    }
}
