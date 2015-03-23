package com.example.jeremy.testdrawer;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle = getTitle();

        // Drawer configuration
        //mDrawerTitle = "Navigation Drawer";
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close)
        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                toolbar.setTitle(mTitle);
                invalidateOptionsMenu();
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle(mDrawerTitle);
                invalidateOptionsMenu();
                syncState();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Drawer list
        mDrawerListView = (ListView) findViewById(R.id.nav_list);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onDrawerItemSelected(position);
            }
        });

        DrawerAdapter adapter = new DrawerAdapter(getApplicationContext());
        String[] layerItems = {
                getString(R.string.title_section1),
                getString(R.string.title_section2)
        };

        String[] projectItems = {
                getString(R.string.title_section3),
                getString(R.string.title_section4),
                getString(R.string.title_section5),
                getString(R.string.title_section6),
                getString(R.string.title_section7),
                getString(R.string.title_section8),
                getString(R.string.title_section9)
        };

        adapter.addSectionHeaderItem(getString(R.string.header_section1));
        for(String str: layerItems){
            adapter.addItem(str);
        }

        adapter.addSectionHeaderItem(getString(R.string.header_section2));
        for(String str: projectItems){
            adapter.addItem(str);
        }

        mDrawerListView.setAdapter(adapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // This to ensure the navigation icon is displayed as
        // burger instead of arrow.
        // Call syncState() from your Activity's onPostCreate
        // to synchronize the indicator with the state of the
        // linked DrawerLayout after onRestoreInstanceState
        // has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // This method should always be called by your Activity's
        // onConfigurationChanged method.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle other action bar items...
        switch (item.getItemId()) {
            case R.id.action_palette:
                DrawConfigDialog dialog = new DrawConfigDialog();
                dialog.show(getFragmentManager(), getString(R.string.dialog_peelings_title));
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDrawerItemSelected(int position){
        switch(position){
            case 2:
                PeelingsCountDialog dialog = new PeelingsCountDialog();
                dialog.show(getFragmentManager(), getString(R.string.dialog_peelings_title));
            break;
        }
    }

    public void openProjectClick(View v) {
        Log.v("ah","Je suis le Docteur !");
    }
}
