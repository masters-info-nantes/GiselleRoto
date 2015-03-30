package com.example.jeremy.testdrawer;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
import java.io.FileOutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int FPS = 3;

    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setLogo(R.mipmap.ic_launcher);
		
        mTitle = getTitle();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void openProjectClick(View v) {
        startActivity(new Intent(this,DrawActivity.class));
    }

    public void openCameraClick(View v) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        // TODO : force camera capture in landscape
        takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

            Toast.makeText(this, "Video saved to:\n" +
                    data.getData(), Toast.LENGTH_LONG).show();

            Uri videoUri = data.getData();
            MediaMetadataRetriever player = new MediaMetadataRetriever();
            player.setDataSource(this, videoUri);

            int duration = Integer.parseInt(player.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            duration /= 1000; // ms -> s

            int framegap = 1000 / FPS;

            for(int i = 0; i < duration * FPS; i++) {
                Bitmap videoFrame = player.getFrameAtTime(i * framegap * 1000);

                File dest = new File(getCacheDir(), "image" + i + ".png");
                try {
                    FileOutputStream out = new FileOutputStream(dest);
                    videoFrame.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Intent intentDraw = new Intent(this, DrawActivity.class);
            intentDraw.putExtra("imagesDir", getCacheDir().getAbsolutePath());
            startActivity(intentDraw);
        }
    }
}
