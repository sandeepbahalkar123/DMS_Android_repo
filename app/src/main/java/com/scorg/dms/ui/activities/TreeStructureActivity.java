package com.scorg.dms.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.scorg.dms.R;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 14/3/17.
 */

public class TreeStructureActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    @BindView(R.id.fab)
    FloatingActionButton mOpenFilterViewFAB;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    DrawerLayout mDrawer;
    NavigationView mLeftNavigationView;
    NavigationView mRightNavigationView;
    View mHeaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compare_screen_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        initializeVariables();
        bindView();

    }


    private void bindView() {
        int width = getResources().getDisplayMetrics().widthPixels / 2;

        //---------
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mLeftNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mRightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        mHeaderView = mRightNavigationView.getHeaderView(0);

        //---------------
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mRightNavigationView.getLayoutParams();
        params.width = width;

        DrawerLayout.LayoutParams leftParams = (DrawerLayout.LayoutParams) mLeftNavigationView.getLayoutParams();
        leftParams.width = width;

        mRightNavigationView.setLayoutParams(params);
        mLeftNavigationView.setLayoutParams(leftParams);
        mOpenFilterViewFAB.setOnClickListener(this);


        mRightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawer.closeDrawer(GravityCompat.END);
                return true;
            }
        });

        // left navigation drawer clickListener
        mLeftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void initializeVariables() {
        mContext = getApplicationContext();


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //onclick on floating button
            case R.id.fab:
                mDrawer.openDrawer(GravityCompat.END);
                break;


        }
    }
}
