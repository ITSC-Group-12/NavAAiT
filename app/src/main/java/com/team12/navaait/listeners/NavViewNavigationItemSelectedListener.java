package com.team12.navaait.listeners;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.team12.navaait.R;
import com.wunderlist.slidinglayer.SlidingLayer;

/**
 * Created by Sam on 5/27/2017.
 */

public class NavViewNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {
    private SlidingLayer mSlidingLayer;
    private FloatingActionsMenu menuMultipleActions;
    private android.support.v4.widget.DrawerLayout drawer;

    public NavViewNavigationItemSelectedListener(SlidingLayer mSlidingLayer, FloatingActionsMenu menuMultipleActions, DrawerLayout drawer) {
        this.mSlidingLayer = mSlidingLayer;
        this.menuMultipleActions = menuMultipleActions;
        this.drawer = drawer;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {

            mSlidingLayer.openLayer(true);
            if (menuMultipleActions.isShown()) {
                menuMultipleActions.setVisibility(View.INVISIBLE);
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
