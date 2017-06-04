package com.team12.navaait;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.util.ListenableList;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.indooratlas.android.sdk.IALocationListener;
import com.indooratlas.android.sdk.IALocationManager;
import com.indooratlas.android.sdk.IALocationRequest;
import com.indooratlas.android.sdk.IARegion;
import com.indooratlas.android.sdk.resources.IAResourceManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;
import com.team12.navaait.domain.Location;
import com.team12.navaait.listeners.MapViewOnTouchListener;
import com.team12.navaait.listeners.NavViewNavigationItemSelectedListener;
import com.team12.navaait.listeners.SearchViewOnFocusChangeListener;
import com.team12.navaait.listeners.SearchViewOnMenuItemClickListener;
import com.team12.navaait.listeners.SearchViewOnQueryChangeListener;
import com.team12.navaait.listeners.SearchViewOnSearchListener;
import com.team12.navaait.listeners.SlideUpPanelListener;
import com.team12.navaait.listeners.SlidingLayerOnInteractListener;
import com.team12.navaait.services.MapService;
import com.team12.navaait.services.UserService;
import com.team12.navaait.util.Indoor;
import com.team12.navaait.util.Outdoor;
import com.team12.navaait.util.SharedPref;
import com.wunderlist.slidinglayer.SlidingLayer;
import com.wunderlist.slidinglayer.transformer.SlideJoyTransformer;

import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    // CONSTANTS
    private static final int OFFSET_DISTANCE = 0;
    private static final int PREVIEW_OFFSET = -1;
    private static final int REQUEST_CODE = 1234;

    // VIEWS
    @BindView(R.id.update_card_view)
    CardView updateCardView;
    @BindView(R.id.update_text)
    TextView updateText;
    @BindView(R.id.downloadProgressBar)
    ProgressBar downloadProgressBar;
    @BindView(R.id.visibility_card_view)
    CardView visibilityCardView;
    @BindView(R.id.visibility_text)
    TextView visibilityText;
    @BindView(R.id.visibility_switch)
    Switch visibilitySwitch;
    @BindView(R.id.close)
    FloatingActionButton closeAction;
    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.map_view2)
    org.osmdroid.views.MapView mMapView2;
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;
    @BindView(R.id.slidingLayer1)
    SlidingLayer mSlidingLayer;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlideUpPanel;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.swipe_down)
    TextView mSwipeText;
    @BindView(R.id.location_name)
    TextView slideUpLocationLabel;
    @BindView(R.id.show_directions)
    Button slideUpShowLocationButton;
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
    private LocationDisplay mLocationDisplay;

    private Snackbar locationSnackbar;
    private Point startingPoint = null;
    private Point endingPoint = null;

    private IALocationManager mIALocationManager;
    private IAResourceManager mResourceManager;

    private MainActivity activity = this;

    private Indoor indoor;
    private Outdoor outdoor;

    // Search Stuff
    private String mLastQuery = "";

    // Permission List
    String[] reqPermission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final int requestCode1 = 2;
    private static final int requestCode2 = 3;

    private IALocationListener mListener;
    private IARegion.Listener mRegionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        Bundle extras = new Bundle(2);
        extras.putString(IALocationManager.EXTRA_API_KEY, getString(R.string.indooratlas_api_key));
        extras.putString(IALocationManager.EXTRA_API_SECRET, getString(R.string.indooratlas_api_secret));

        // instantiate IALocationManager and IAResourceManager
        mIALocationManager = IALocationManager.create(this, extras);
        mResourceManager = IAResourceManager.create(this);

        setupUI();

        // get the MapView's LocationDisplay
        mLocationDisplay = mMapView.getLocationDisplay();
        // start showing location
        if (!mLocationDisplay.isStarted()) {
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
            // try to set the starting point to the user's location
            mLocationDisplay.startAsync();
            startingPoint = mLocationDisplay.getLocation().getPosition();
        } else {
            locationSnackbar.show();
        }


        outdoor = new Outdoor(mMapView, getApplicationContext());
        indoor = new Indoor(mMapView2, getApplicationContext(), mResourceManager);
        mListener = indoor.getmListener();
        mRegionListener = indoor.getmRegionListener();

        // start receiving location updates & monitor region changes
        mIALocationManager.requestLocationUpdates(IALocationRequest.create(), mListener);
        mIALocationManager.registerRegionListener(mRegionListener);

        mSearchView.setOnQueryChangeListener(new SearchViewOnQueryChangeListener(mSearchView, getApplicationContext()));
        mSearchView.setOnSearchListener(new SearchViewOnSearchListener(mLocationDisplay, mLastQuery, mMapView, slideUpLocationLabel, mSlideUpPanel, getApplicationContext(), this));
        mSearchView.setOnFocusChangeListener(new SearchViewOnFocusChangeListener(mSearchView, mLastQuery));
        mSearchView.setOnMenuItemClickListener(new SearchViewOnMenuItemClickListener(mLocationDisplay, getApplicationContext(), activity));
        mSlidingLayer.setOnInteractListener(new SlidingLayerOnInteractListener(menuMultipleActions));
        mMapView.setOnTouchListener(new MapViewOnTouchListener(mMapView, getApplicationContext(), endingPoint));
        navigationView.setNavigationItemSelectedListener(new NavViewNavigationItemSelectedListener(mSlidingLayer, menuMultipleActions, drawer));
        mSlideUpPanel.addPanelSlideListener(new SlideUpPanelListener(menuMultipleActions));
        mSlideUpPanel.setFadeOnClickListener(new SlideUpPanelListener(mSlideUpPanel));

        // For API level 23+ request permission at runtime
        if (ContextCompat.checkSelfPermission(MainActivity.this, reqPermission[0]) == PackageManager.PERMISSION_GRANTED) {
            outdoor.loadMobileMapPackage();
        } else {
            // request permission
            ActivityCompat.requestPermissions(MainActivity.this, reqPermission, requestCode1);
        }

        // Listen to changes in the status of the location data source.
        mLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {

                // If LocationDisplay started OK, then continue.
                if (dataSourceStatusChangedEvent.isStarted()) {
                    locationSnackbar.dismiss();
                    return;
                }

                // No error is reported, then continue.
                if (dataSourceStatusChangedEvent.getError() == null) {
                    locationSnackbar.dismiss();
                    return;
                }

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
                    locationSnackbar.show();

                }
            }
        });

        mLocationDisplay.addLocationChangedListener(new LocationDisplay.LocationChangedListener() {
            @Override
            public void onLocationChanged(LocationDisplay.LocationChangedEvent locationChangedEvent) {
                if (mLocationDisplay.getLocation().getPosition() != null) {
                    locationSnackbar.dismiss();
                    Point currentLocation = mLocationDisplay.getLocation().getPosition();
                    startingPoint = currentLocation;
                    UserService.setLocation(getApplicationContext(), new Location("", currentLocation.getX(), currentLocation.getY()));
                }
            }
        });


    }

    @OnClick(R.id.update_card_view)
    public void download() {

        MapService.checkVersion(getApplicationContext(), downloadProgressBar);
    }

    @OnClick({R.id.action_a, R.id.action_b, R.id.action_c, R.id.action_d, R.id.close})
    public void fabAction(FloatingActionButton fab) {
        if (fab.getId() == R.id.close) {
            if (mSlideUpPanel.getPanelState() != PanelState.HIDDEN) {
                mSlideUpPanel.setPanelState(PanelState.HIDDEN);
                slideUpLocationLabel.setText("");
                menuMultipleActions.setVisibility(View.VISIBLE);
                endingPoint = null;
                ListenableList<GraphicsOverlay> overlays = mMapView.getGraphicsOverlays();
                for (GraphicsOverlay g : overlays) {
                    overlays.removeAll(overlays);
                }
                closeAction.setVisibility(View.INVISIBLE);

            } else if (fab.getId() == R.id.action_a) {

                actionA.setTitle("Action A clicked");
            } else if (fab.getId() == R.id.action_b) {
                viewFlipper.showNext();
            } else if (fab.getId() == R.id.action_c) {

            } else if (fab.getId() == R.id.action_d) {

            }

        }
    }

    @OnClick(R.id.show_directions)
    public void action(Button button) {
        if (button.getId() == R.id.show_directions) {
            if (startingPoint != null) {
                if (endingPoint != null) {
                    outdoor.solveRoute(startingPoint, endingPoint);
                    if (!closeAction.isShown()) {
                        closeAction.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Ending Point Not Set", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Starting Point Not Set", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Handle user visibility
     */
    @OnCheckedChanged(R.id.visibility_switch)
    public void checkSwitch(boolean checked) {

//        visibilitySwitch.setChecked(UserService.toggleVisibility(getApplicationContext()));
    }

    private void setupUI() {

        locationSnackbar = Snackbar.make(mMapView, "Please turn location on for routing!", Snackbar.LENGTH_INDEFINITE);
        visibilitySwitch.setChecked(SharedPref.getBooleanPref(getApplicationContext(), SharedPref.USER_VISIBILITY));
        mMapView2.setTilesScaledToDpi(true);
        mMapView2.setBuiltInZoomControls(true);
        mMapView2.getController().setZoom(18);

        View headerView = navigationView.getHeaderView(0);
        TextView welcomeNameLabel = (TextView) headerView.findViewById(R.id.welcome_name_label);
        welcomeNameLabel.setText("Hello " + SharedPref.getStringPref(getApplicationContext(), SharedPref.USER_FIRST_NAME)
                + " " + SharedPref.getStringPref(getApplicationContext(), SharedPref.USER_LAST_NAME));

        MapTileProviderBasic mProvider = new MapTileProviderBasic(getApplicationContext());
        mProvider.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        TilesOverlay mTilesOverlay = new TilesOverlay(mProvider, getBaseContext());
        mMapView2.getOverlays().add(mTilesOverlay);
        mSearchView.attachNavigationDrawerToMenuButton(drawer);
        mSlidingLayer.setStickTo(SlidingLayer.STICK_TO_BOTTOM);
        mSlidingLayer.setLayerTransformer(new SlideJoyTransformer());
        mSlidingLayer.setShadowSizeRes(R.dimen.shadow_size);
        mSlidingLayer.setShadowDrawable(R.drawable.sidebar_shadow);
        mSlidingLayer.setOffsetDistance(OFFSET_DISTANCE);
        mSlidingLayer.setPreviewOffsetDistance(PREVIEW_OFFSET);
        mSlidingLayer.closeLayer(true);
        mSlidingLayer.setChangeStateOnTap(false);

        // has to be 0.6 to avoid FloatingSearchView hanging from top
        mSlideUpPanel.setAnchorPoint(0.6f);
        mSlideUpPanel.setPanelState(PanelState.ANCHORED);
        mSlideUpPanel.setPanelState(PanelState.HIDDEN);
    }

    /**
     * Handle the permissions request response
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case requestCode1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    outdoor.loadMobileMapPackage();
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            if (mSlideUpPanel != null &&
                    (mSlideUpPanel.getPanelState() == PanelState.EXPANDED || mSlideUpPanel.getPanelState() == PanelState.ANCHORED)) {
                mSlideUpPanel.setPanelState(PanelState.COLLAPSED);
            } else {
                super.onBackPressed();
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // remember to clean up after ourselves
        mIALocationManager.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister location & region changes
        mIALocationManager.removeLocationUpdates(mListener);
        mIALocationManager.registerRegionListener(mRegionListener);
    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if(!matches.isEmpty()){

                mSearchView.setSearchFocused(true);
                mSearchView.setSearchText(matches.get(0));
                Log.d("MATCHES", matches.toString());
            }else{
                Toast.makeText(getApplicationContext(), "No Result from Speech Recognizer", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setEndingPoint(Point endingPoint) {
        this.endingPoint = endingPoint;
    }
}
