package com.team12.navaait.listeners;

import android.util.Log;
import android.view.View;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by Sam on 5/27/2017.
 */

public class SlideUpPanelListener implements SlidingUpPanelLayout.PanelSlideListener, View.OnClickListener {

    private static final String TAG = "SlideUpPanelListener";
    private SlidingUpPanelLayout mSlideUpPanel;

    public SlideUpPanelListener(SlidingUpPanelLayout mSlideUpPanel) {
        this.mSlideUpPanel = mSlideUpPanel;
    }

    public SlideUpPanelListener() {
    }

    @Override
    public void onClick(View v) {
        mSlideUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        Log.i(TAG, "onPanelSlide, offset " + slideOffset);
    }

    @Override
    public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

    }
}
