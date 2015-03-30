package com.example.jeremy.testdrawer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DrawActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerListView;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private ArrayList<File> images;
    private int selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTitle = getTitle();

        // Drawer configuration
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
        for(String str: layerItems) {
            adapter.addItem(str);
        }

        adapter.addSectionHeaderItem(getString(R.string.header_section2));
        for(String str: projectItems) {
            adapter.addItem(str);
        }

        mDrawerListView.setAdapter(adapter);

        // Load first image
        Intent intent = getIntent();
        String dirPath = intent.getStringExtra("imagesDir");

        this.images = new ArrayList<File>();

        File imagesDir = new File(dirPath);
        if(imagesDir.isDirectory()){
            for(int i = 0; i < imagesDir.listFiles().length; i++){
                File curImage = imagesDir.listFiles()[i];

                if(curImage.getAbsolutePath().contains("image")){
                    this.images.add(curImage);
                }
            }
        }

        this.setSelectedImage(0);
    }

    private void setSelectedImage(int index) {
        // checks
        index = index < 0 ? 0 : index;
        index = index >= images.size() ? images.size() - 1 : index;

        this.selectedImage = index;

        // change image
        Bitmap bitmap = BitmapFactory.decodeFile(this.images.get(index).getAbsolutePath());
        final ImageView drawZone = (ImageView)findViewById(R.id.imageView);
        drawZone.setImageBitmap(bitmap);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
                Bundle dialogArgs = new Bundle();
				dialogArgs.putInt("num", 1);
				dialog.setArguments(dialogArgs);
                dialog.show(getFragmentManager(), getString(R.string.dialog_peelings_title));
            break;

            case R.id.action_back:
                this.setSelectedImage(this.selectedImage - 1);
            break;

            case R.id.action_next:
                this.setSelectedImage(this.selectedImage + 1);
            break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDrawerItemSelected(int position){
        switch(position){
            case 1:
                final ImageView drawZone = (ImageView)findViewById(R.id.imageView);
                int visib = drawZone.getVisibility();

                if(visib == View.VISIBLE) drawZone.setVisibility(View.INVISIBLE);
                else drawZone.setVisibility(View.VISIBLE);

                break;
            case 2:
                PeelingsCountDialog dialog = new PeelingsCountDialog();
                dialog.show(getFragmentManager(), getString(R.string.dialog_peelings_title));
				break;
			case 10:
				this.finish();
				break;
            default:
				Log.v("DrawActivity","onDrawerItemSelected "+position);
        }
    }
}
