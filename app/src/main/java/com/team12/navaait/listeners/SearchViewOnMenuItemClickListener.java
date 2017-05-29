package com.team12.navaait.listeners;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.team12.navaait.MainActivity;
import com.team12.navaait.R;

import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by Sam on 5/24/2017.
 */

public class SearchViewOnMenuItemClickListener implements FloatingSearchView.OnMenuItemClickListener {

    private static final int REQUEST_CODE = 1234;
    private LocationDisplay mLocationDisplay;
    private Context context;
    private MainActivity activity;

    public SearchViewOnMenuItemClickListener(LocationDisplay mLocationDisplay, Context context, MainActivity activity) {
        this.mLocationDisplay = mLocationDisplay;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public void onActionMenuItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_voice_rec) {

            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(
                    new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
            if (activities.size() == 0) {

                Toast.makeText(context, "Recognizer not present", Toast.LENGTH_SHORT).show();

            } else {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
                startActivityForResult(activity, intent, REQUEST_CODE, null);
            }

        } else if (item.getItemId() == R.id.action_location) {
            mLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
            if (!mLocationDisplay.isStarted())
                mLocationDisplay.startAsync();
        } else {
            Toast.makeText(context, "NOT SURE WHICH SEARCH VIEW ACTION BUTTON IS PRESSED.", Toast.LENGTH_LONG).show();
        }
    }
}
