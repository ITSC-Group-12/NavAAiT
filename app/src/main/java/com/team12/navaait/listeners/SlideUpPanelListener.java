package com.team12.navaait.listeners;

import android.util.Log;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by Sam on 5/27/2017.
 */

public class SlideUpPanelListener implements SlidingUpPanelLayout.PanelSlideListener, View.OnClickListener {

    private static final String TAG = "SlideUpPanelListener";
    private FloatingActionsMenu floatingActionsMenu;
    private SlidingUpPanelLayout mSlideUpPanel;

    public SlideUpPanelListener(SlidingUpPanelLayout mSlideUpPanel) {
        this.mSlideUpPanel = mSlideUpPanel;
    }

    public SlideUpPanelListener(FloatingActionsMenu floatingActionsMenu) {
        this.floatingActionsMenu = floatingActionsMenu;
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
        Log.i(TAG, previousState.toString());
        Log.i(TAG, newState.toString());
        if (previousState == SlidingUpPanelLayout.PanelState.HIDDEN && newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
            floatingActionsMenu.setVisibility(View.INVISIBLE);
        } else if (previousState == SlidingUpPanelLayout.PanelState.DRAGGING && newState == SlidingUpPanelLayout.PanelState.HIDDEN) {
            floatingActionsMenu.setVisibility(View.VISIBLE);
        }

    }
}
