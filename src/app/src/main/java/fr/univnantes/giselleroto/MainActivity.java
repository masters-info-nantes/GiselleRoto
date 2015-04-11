package fr.univnantes.giselleroto;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jeremy.testdrawer.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * First activity launched with the application
 * Display the home page which ask the user to create
 * or open a project
 */
public class MainActivity extends ActionBarActivity {

	// Unique identifier for intent callback
    private static final int REQUEST_VIDEO_FROM_GALLERY = 1;
	private static final int REQUEST_VIDEO_CAPTURE = 2;
    private static final int REQUEST_PICK_FILE = 3;

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

    /**
     * Following methods are callbacks for interface buttons
     */
	public void openProjectClick(View v) {
        Intent theIntent = new Intent(Intent.ACTION_GET_CONTENT);
        theIntent.addCategory(Intent.CATEGORY_OPENABLE);
        theIntent.setType("file/*");
        try {
             startActivityForResult(theIntent,MainActivity.REQUEST_PICK_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public void openGalleryClick(View v) {
		Intent takeVideoFromGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(takeVideoFromGallery, MainActivity.REQUEST_VIDEO_FROM_GALLERY); 
	}
	
	public void openCameraClick(View v) {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

		// TODO : force camera capture in landscape
		takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takeVideoIntent, MainActivity.REQUEST_VIDEO_CAPTURE);
		}
	}

    /**
     * Callback method for each intent launched from this class
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Create a new project from an existing video
		if((requestCode == REQUEST_VIDEO_CAPTURE || requestCode == REQUEST_VIDEO_FROM_GALLERY)
                && resultCode == RESULT_OK)
        {

            // Clean working dir
            File[] directory = getCacheDir().listFiles();
            if(directory != null){
                for (File file : directory ){
                    file.delete();
                }
            }

            // Extract frames from video
			Uri videoUri = data.getData();
			MediaMetadataRetriever player = new MediaMetadataRetriever();
			player.setDataSource(this, videoUri);

			int duration = Integer.parseInt(player.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
			duration /= 1000; // ms -> s

			int framegap = 1000 / FPS;

			// TODO Remove this dirty debug thing
			int max = /*duration * FPS*/2;

			for(int i = 0; i < max; i++) {

                // Video image
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

                // Draw image
                Bitmap drawFrame = Bitmap.createBitmap(videoFrame.getWidth(), videoFrame.getHeight(), Bitmap.Config.ARGB_8888);
                dest = new File(getCacheDir(), "image" + i + "-draw.png");
                try {
                    FileOutputStream out = new FileOutputStream(dest);
                    drawFrame.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, "Wait... Image " + (i+1) + "/" + max, Toast.LENGTH_SHORT).show();
				Log.d("Wait", "Image " + (i+1) + "/" + max);
			}

            // The project is loaded, go to the draw view
			Intent intentDraw = new Intent(this, DrawActivity.class);
			intentDraw.putExtra("imagesDir", getCacheDir().getAbsolutePath());
            intentDraw.putExtra("videoURI", videoUri);
			startActivity(intentDraw);
		}
        
        // Open an existing project
        else if(requestCode == MainActivity.REQUEST_PICK_FILE && resultCode == RESULT_OK){
            String theFolderPath = data.getData().getPath();
            Toast.makeText(this, "Not yet implemented", Toast.LENGTH_SHORT).show();
            Log.d("Pick file", theFolderPath);
        }
	}
}
