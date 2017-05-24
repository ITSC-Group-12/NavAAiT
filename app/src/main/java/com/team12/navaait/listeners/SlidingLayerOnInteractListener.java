package com.team12.navaait.listeners;

import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.wunderlist.slidinglayer.SlidingLayer;

/**
 * Created by Sam on 5/24/2017.
 */

public class SlidingLayerOnInteractListener implements SlidingLayer.OnInteractListener {

    private FloatingActionsMenu menuMultipleActions;

    public SlidingLayerOnInteractListener(FloatingActionsMenu menuMultipleActions) {
        this.menuMultipleActions = menuMultipleActions;
    }

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
}
