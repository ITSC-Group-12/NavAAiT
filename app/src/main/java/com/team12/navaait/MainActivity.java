package com.team12.navaait;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.MobileMapPackage;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.wunderlist.slidinglayer.transformer.SlideJoyTransformer;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // TAGS
    public static final String loginTAG = "Logged in";
    private static final String sTag = "Gesture";
    private static final String TAG = "MMPK";

    // CONSTANTS
    private static final int OFFSET_DISTANCE = 0;
    private static final int PREVIEW_OFFSET = -1;
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private static final String FILE_EXTENSION = ".mmpk";
    private static final String[] SETTINGS_ITEMS = new String[]{"Visibility", "Map Update"};

    // VIEWS
    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.map_view2)
    org.osmdroid.views.MapView mMapView2;
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.my_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.slidingLayer1)
    SlidingLayer mSlidingLayer;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.swipe_down)
    TextView mSwipeText;
    @BindView(R.id.location_name)
    TextView t;
    @BindView(R.id.show_location)
    Button f1;
    @BindView(R.id.show_directions)
    Button f2;
    @BindView(R.id.list)
    ListView lv;
    @BindView(R.id.multiple_actions)
    FloatingActionsMenu menuMultipleActions;
    @BindView(R.id.action_a)
    FloatingActionButton actionA;
    @BindView(R.id.action_b)
    FloatingActionButton actionB;
    @BindView(R.id.action_c)
    FloatingActionButton actionC;
    @BindView(R.id.action_d)
    FloatingActionButton actionD;

    //Map Stuff
    private static File extStorDir;
    private static String extSDCardDirName;
    private static String filename;
    private static String mmpkFilePath;
    private MobileMapPackage mapPackage;
    private Callout mCallout;
    private LocationDisplay mLocationDisplay;
    private GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
    private final SimpleLineSymbol mSolvedRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 4.0f);
    private RouteTask mRouteTask = null;
    private Graphic mRouteGraphic;

    private Point[] points = new Point[]{null, null};

    // Search Stuff
    private String mLastQuery = "";

    // Permission List
    String[] reqPermission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final int requestCode1 = 2;
    private static final int requestCode2 = 3;

    private static String createMobileMapPackageFilePath() {
        return extStorDir.getAbsolutePath() + File.separator + extSDCardDirName + File.separator + filename + FILE_EXTENSION;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mMapView2.setTilesScaledToDpi(true);
        mMapView2.setBuiltInZoomControls(true);
        mMapView2.getController().setZoom(18);

        TilesOverlay mTilesOverlay;
        MapTileProviderBasic mProvider;

        mProvider = new MapTileProviderBasic(getApplicationContext());
        mProvider.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);

        mTilesOverlay = new TilesOverlay(mProvider, getBaseContext());
        mMapView2.getOverlays().add(mTilesOverlay);

        navigationView.setNavigationItemSelectedListener(this);
        mSearchView.attachNavigationDrawerToMenuButton(drawer);
        mSwipeText.setText(getResources().getString(R.string.swipe_down_label));

        mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_BOTTOM);
        mSlidingLayer.setLayerTransformer(new SlideJoyTransformer());
        mSlidingLayer.setShadowSizeRes(R.dimen.shadow_size);
        mSlidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        mSlidingLayer.setOffsetDistance(OFFSET_DISTANCE);
        mSlidingLayer.setPreviewOffsetDistance(PREVIEW_OFFSET);
        mSlidingLayer.closeLayer(true);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new MyAdapter(SETTINGS_ITEMS));

        // has to be 0.6 to avoid FloatingSearchView hanging from top
        mLayout.setAnchorPoint(0.6f);
        mLayout.setPanelState(PanelState.ANCHORED);
        mLayout.setPanelState(PanelState.HIDDEN);

        // get the MapView's LocationDisplay
        mLocationDisplay = mMapView.getLocationDisplay();

        // get sdcard resource name
        extStorDir = Environment.getExternalStorageDirectory();
        // get the directory
        extSDCardDirName = this.getResources().getString(R.string.config_data_sdcard_offline_dir);
        // get mobile map package filename
        filename = this.getResources().getString(R.string.config_mmpk_name);
        // create the full path to the mobile map package file
        mmpkFilePath = createMobileMapPackageFilePath();

        // start showing location
        if (!mLocationDisplay.isStarted()) {
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            mLocationDisplay.startAsync();
        }

        List<String> your_array_list = Arrays.asList(
                "This",
                "Is",
                "An",
                "Example",
                "ListView"
        );
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                your_array_list);

        lv.setAdapter(arrayAdapter);

        t.setText("The Library");
        f1.setText("Show on Map");
        f2.setText("Show Directions");

        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("Action A clicked");
            }
        });

        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionB.setTitle("Action B clicked");
                viewFlipper.showNext();
            }
        });

        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLayout != null) {
                    if (mLayout.getPanelState() != PanelState.HIDDEN) {
                        mLayout.setPanelState(PanelState.HIDDEN);
                    } else {
                        mLayout.setPanelState(PanelState.COLLAPSED);
                    }
                }
            }
        });

        actionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionD.setTitle("Action D clicked");
                solveRoute();
            }
        });

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress you can call it where ever you want, but
                    //it makes sense to do it when loading something in the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data sourcewith a new query.
                    DataHelper.findSuggestions(getApplicationContext(), newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<NameSuggestion> results) {

                                    //this will swap the data and render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                if (!mLocationDisplay.isStarted())
                    mLocationDisplay.startAsync();

//                 LocationSuggestion colorSuggestion = (LocationSuggestion) searchSuggestion;
//                DataHelper.findColors(getActivity(), colorSuggestion.getBody(),
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                mSearchResultsAdapter.swapData(results);
//                            }
//
//                        });
                Log.d(TAG, "onSuggestionClicked()");

                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;

                mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                if (!mLocationDisplay.isStarted())
                    mLocationDisplay.startAsync();

//                DataHelper.findColors(getActivity(), query,
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                mSearchResultsAdapter.swapData(results);
//                            }
//
//                        });
                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
//                mSearchView.swapSuggestions(DataHelper.getHistory(getActivity(), 3));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.action_voice_rec) {

                    //  TODO add speech recognition

                } else if (item.getItemId() == R.id.action_location) {
                    mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                    if (!mLocationDisplay.isStarted())
                        mLocationDisplay.startAsync();
                } else {
                    Toast.makeText(MainActivity.this, "NOT SURE WHICH SEARCH VIEW ACTION BUTTON IS PRESSED.", Toast.LENGTH_LONG).show();
                }
            }
        });


        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress you can call it where ever you want, but
                    //it makes sense to do it when loading something in the background.
                    mSearchView.showProgress();

                    //simulates a query call to a data source with a new query.
                    DataHelper.findSuggestions(MainActivity.this, newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<NameSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });


        mSlidingLayer.setOnInteractListener(new SlidingLayer.OnInteractListener() {
            @Override
            public void onOpen() {
            }

            @Override
            public void onShowPreview() {
            }

            @Override
            public void onClose() {
                if (!menuMultipleActions.isShown()) {
                    menuMultipleActions.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onOpened() {
            }

            @Override
            public void onPreviewShowed() {

            }

            @Override
            public void onClosed() {

            }
        });


        // For API level 23+ request permission at runtime
        if (ContextCompat.checkSelfPermission(MainActivity.this, reqPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            loadMobileMapPackage(mmpkFilePath);
        } else {
            // request permission
            ActivityCompat.requestPermissions(MainActivity.this, reqPermission, requestCode1);
        }


        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this, mMapView) {


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
                TextView calloutContent = new TextView(getApplicationContext());
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
        });

        // Listen to changes in the status of the location data source.
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK, then continue.
                if (dataSourceStatusChangedEvent.isStarted())
                    return;

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null)
                    return;

                // If an error is found, handle the failure to start.
                // Check permissions to see if failure may be due to lack of permissions.
                boolean permissionCheck1 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermission[1]) ==
                        PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(MainActivity.this, reqPermission[2]) ==
                        PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1 && permissionCheck2)) {
                    // If permissions are not already granted, request permission from the user.
                    ActivityCompat.requestPermissions(MainActivity.this, reqPermission, requestCode2);
                } else {
                    // Report other unknown failure types to the user - for example, location services may not
                    // be enabled on the device.
                    String message = String.format("Error in DataSourceStatusChangedListener: %s", dataSourceStatusChangedEvent
                            .getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                }
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "onItemClick", Toast.LENGTH_SHORT).show();
            }
        });


        mLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, PanelState previousState, PanelState newState) {

            }

        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        f2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    /**
     * Handle the permissions request response
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case requestCode1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadMobileMapPackage(mmpkFilePath);
                } else {
                    // report to user that permission was denied
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.location_permission_denied),
                            Toast.LENGTH_SHORT).show();
                }
            }

            case requestCode2: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission was granted. This would have been triggered in response to failing to start the
                    // LocationDisplay, so try starting this again.
                    mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                    mLocationDisplay.startAsync();
                } else {
                    // If permission was denied, show toast to inform user what was chosen. If LocationDisplay is started again,
                    // request permission UX will be shown again, option should be shown to allow never showing the UX again.
                    // Alternative would be to disable functionality so request is not shown again.
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.location_permission_denied), Toast
                            .LENGTH_SHORT).show();
                }
            }


        }
    }

    /**
     * Load a mobile map package into a MapView
     *
     * @param mmpkFile Full path to mmpk file
     */
    private void loadMobileMapPackage(String mmpkFile) {
        // create the mobile map package
        mapPackage = new MobileMapPackage(mmpkFile);
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
                    mapPackage.getMaps().get(0).addDoneLoadingListener(new Runnable() {
                        @Override
                        public void run() {

                            if (mapPackage.getLoadStatus() != LoadStatus.LOADED) {
                                Snackbar.make(mMapView, String.format(getString(R.string.object_not_loaded), "Map"),
                                        Snackbar.LENGTH_SHORT).show();
                                return;
                            }

                            setupOfflineNetwork(mapPackage.getMaps().get(0));
                            mapPackage.getMaps().get(0).removeDoneLoadingListener(this);
                            mMapView.getGraphicsOverlays().add(graphicsOverlay);
                        }
                    });


                } else {
                    // Log an issue if the mobile map package fails to load
                    Log.e(TAG, mapPackage.getLoadError().getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);


        } else {

            if (mLayout != null &&
                    (mLayout.getPanelState() == PanelState.EXPANDED || mLayout.getPanelState() == PanelState.ANCHORED)) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            } else {
                super.onBackPressed();
            }
        }


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {

            mSlidingLayer.openLayer(true);
            if (menuMultipleActions.isShown()) {
                menuMultipleActions.setVisibility(View.INVISIBLE);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Routing functions

    /**
     * Sets up a RouteTask from the NetworkDatasets in the current map. Shows a message to user if network dataset is
     * not found.
     */
    private void setupOfflineNetwork(ArcGISMap mMap) {

        if ((mMap.getTransportationNetworks() == null) || (mMap.getTransportationNetworks().size() < 1)) {
            Snackbar.make(mMapView, getString(R.string.network_dataset_not_found), Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Create the RouteTask from network data set using same map used in display
        mRouteTask = new RouteTask(MainActivity.this, mMap.getTransportationNetworks().get(0));
        mRouteTask.addDoneLoadingListener(new Runnable() {
            @Override
            public void run() {
                if (mRouteTask.getLoadStatus() != LoadStatus.LOADED) {
                    Snackbar.make(mMapView, String.format(getString(R.string.object_not_loaded), "RouteTask"), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        mRouteTask.loadAsync();
    }

    /**
     * Solves a route using the existing geocoded address and hydrant locations, and displays a graphic of the route, and
     * message with distance and time. Shows messages to user if locations are not set.
     */
    private void solveRoute() {

        if (mRouteTask == null) {
            Snackbar.make(mMapView, getString(R.string.route_task_not_set), Snackbar.LENGTH_SHORT).show();
            return;
        }

        RouteParameters routeParams;
        try {
            routeParams = mRouteTask.createDefaultParametersAsync().get();
            routeParams.setReturnDirections(true);

            Stop start = new Stop(points[0]);
            routeParams.getStops().add(start);

            Stop finish = new Stop(points[1]);
            routeParams.getStops().add(finish);

            final ListenableFuture<RouteResult> routeFuture = mRouteTask.solveRouteAsync(routeParams);

            routeFuture.addDoneListener(new Runnable() {
                @Override
                public void run() {
                    // Show results of solved route.
                    RouteResult routeResult;
                    try {
                        if (routeFuture.isDone()) {
                            int i = 0;
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
                                Snackbar.make(mMapView,
                                        topRoute.getRouteName(), Snackbar.LENGTH_SHORT).show();

                                mMapView.setViewpointGeometryAsync(topRoute.getRouteGeometry());

                            }

                        }
                    } catch (InterruptedException | ExecutionException e) {
                        Snackbar.make(mMapView, String.format(getString(R.string.route_error), e.getMessage()),
                                Snackbar.LENGTH_SHORT).show();
                        Log.e("SOLVE", e.getMessage());
                    }
                }
            });
        } catch (InterruptedException | ExecutionException e) {
            Snackbar.make(mMapView, String.format(getString(R.string.route_params_error), e.getMessage()),
                    Snackbar.LENGTH_SHORT).show();
        }
    }

}
