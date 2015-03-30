package com.example.jeremy.testdrawer;

import android.content.Intent;
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
import java.util.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends ActionBarActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
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

        Uri fileUri = getOutputMediaFileUri();  // create a file to save the video
        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);


        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {

            Toast.makeText(this, "Video saved to:\n" +
                    data.getData(), Toast.LENGTH_LONG).show();

            Log.d("MainActivity", "Video saved to : " + data.getData().toString());


            //~ ImageView capturedImageView = (ImageView)findViewById(R.id.capturedimage);

            //~ MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//~ 
            //~ mediaMetadataRetriever.setDataSource(data.getData().normalizeScheme());
            //~ Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(2*1000*1000); //unit in microsecond
            //~ capturedImageView.setImageBitmap(bmFrame);

            //Intent intent = new Intent(Intent.ACTION_VIEW, data.getData().normalizeScheme());
            //startActivity(intent);
        }
    }

    private Uri getOutputMediaFileUri(){

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(
                getFilesDir().getPath() + File.separator +
                "VID_"+ timeStamp + ".mp4"
        );

        return Uri.fromFile(mediaFile);
    }
}
