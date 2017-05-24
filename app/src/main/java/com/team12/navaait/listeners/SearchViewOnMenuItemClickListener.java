package com.team12.navaait.listeners;

import android.content.Context;
import android.view.MenuItem;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.team12.navaait.R;

/**
 * Created by Sam on 5/24/2017.
 */

public class SearchViewOnMenuItemClickListener implements FloatingSearchView.OnMenuItemClickListener {

    private LocationDisplay mLocationDisplay;
    private Context context;

    public SearchViewOnMenuItemClickListener(LocationDisplay mLocationDisplay, Context context) {
        this.mLocationDisplay = mLocationDisplay;
        this.context = context;
    }

    @Override
    public void onActionMenuItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_voice_rec) {

            //  TODO add speech recognition

        } else if (item.getItemId() == R.id.action_location) {
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
            if (!mLocationDisplay.isStarted())
                mLocationDisplay.startAsync();
        } else {
            Toast.makeText(context, "NOT SURE WHICH SEARCH VIEW ACTION BUTTON IS PRESSED.", Toast.LENGTH_LONG).show();
        }
    }
}
