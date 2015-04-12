package fr.univnantes.giselleroto;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jeremy.testdrawer.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity launched after project opening
 * Heart of this application
 */
public class DrawActivity extends ActionBarActivity {
	
	private static final int DRAWER_HEADER_LAYERS_POS = 0;
	private static final int DRAWER_ITEM_BACKGROUND_MOVIE_POS = 1;
	private static final int DRAWER_ITEM_ONION_PEELING_POS = 2;
	private static final int DRAWER_HEADER_PROJECT_POS = 3;
	private static final int DRAWER_ITEM_SAVE_POS = 4;
	private static final int DRAWER_ITEM_SAVE_AS_POS = 5;
	private static final int DRAWER_ITEM_SHARE_POS = 6;
	private static final int DRAWER_ITEM_CLOSE_PROJECT_POS = 7;

    // Interface elements
	private DrawZone mDrawZone;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerListView;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

    // Images and video management
	private ArrayList<File> images;
	private int selectedImage;

    private Uri videoURI;
	private long lastBackPress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw);
		
		lastBackPress = Calendar.getInstance().getTimeInMillis();
		
        videoURI = getIntent().getParcelableExtra("videoURI");
		mDrawZone = (DrawZone)findViewById(R.id.drawZone);
		
		final Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
		setSupportActionBar(toolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mTitle = getTitle();

		// Drawer menu configuration
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

		// Fill drawer menu with items
		mDrawerListView = (ListView) findViewById(R.id.nav_list);

		mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					onDrawerItemSelected(position);
				}
			});
	
		DrawerAdapter adapter = new DrawerAdapter(getApplicationContext());
		// if change order change also final int drawer_item_*_pos 
		adapter.addSectionHeaderItem(getString(R.string.drawer_header_layers));
		adapter.addItem(getString(R.string.drawer_item_background_movie));
		adapter.addItem(getString(R.string.drawer_item_onion_peeling));

		adapter.addSectionHeaderItem(getString(R.string.drawer_header_project));
		adapter.addItem(getString(R.string.drawer_item_save));
		adapter.addItem(getString(R.string.drawer_item_save_as));
		adapter.addItem(getString(R.string.drawer_item_share));
		adapter.addItem(getString(R.string.drawer_item_close_project));

		mDrawerListView.setAdapter(adapter);

        // Change buttons size (on the first menu row)
        final ImageButton buttonFirst = (ImageButton) findViewById(R.id.buttonFirst);
        buttonFirst.setScaleX(0.8f);
        buttonFirst.setScaleY(0.8f);

        final ImageButton buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        buttonPlay.setScaleX(0.8f);
        buttonPlay.setScaleY(0.8f);

        final ImageButton buttonLast = (ImageButton) findViewById(R.id.buttonLast);
        buttonLast.setScaleX(0.8f);
        buttonLast.setScaleY(0.8f);

        // Fix drawzone size
        final ImageView imageDraw = (ImageView) findViewById(R.id.imageDraw);
        mDrawZone.setMinimumWidth(imageDraw.getWidth());
        mDrawZone.setMinimumHeight(imageDraw.getHeight());

		// Load first image
		Intent intent = getIntent();
		String dirPath = intent.getStringExtra("imagesDir");

		this.images = new ArrayList<File>();

		File imagesDir = new File(dirPath);
		if(imagesDir.isDirectory()){
			for(int i = 0; i < imagesDir.listFiles().length; i++){
				File curImage = imagesDir.listFiles()[i];

				if(curImage.getAbsolutePath().contains("image")
                        && !curImage.getAbsolutePath().contains("draw")){
					this.images.add(curImage);
				}
			}
		}

		this.setSelectedImage(0);
	}

	public void drawPopupCallback(float size, int color) {
		mDrawZone.setToolWidth(size);
		mDrawZone.setToolColor(color);
	}

    /**
     * Change the current image: saves the current draw
     * and load asked image and draw
     * @param index
     */
	private void setSelectedImage(int index) {

		// Check index out of bounds
		index = index < 0 ? 0 : index;
		index = index >= images.size() ? images.size() - 1 : index;

        // Save old draw
        final DrawZone drawZone = (DrawZone)findViewById(R.id.drawZone);
        Bitmap bitmap = drawZone.getmBitmap();

        if(bitmap != null) { // At begining drazone bitmap is null
            String oldDrawPath = this.images.get(selectedImage).getAbsolutePath().replaceAll(".png", "-draw.png");
            File dest = new File(oldDrawPath);
            try {
                FileOutputStream out = new FileOutputStream(dest);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

		// Change image
        //  -> background
        String imagePath = this.images.get(index).getAbsolutePath();
		bitmap = BitmapFactory.decodeFile(imagePath);
		final ImageView backImage = (ImageView)findViewById(R.id.imageDraw);
        backImage.setImageBitmap(bitmap);

        //  -> drawzone
        String drawPath = imagePath.replaceAll(".png", "-draw.png");
        bitmap = BitmapFactory.decodeFile(drawPath);
        drawZone.setmBitmap(bitmap);

        this.selectedImage = index;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

    /**
      * Callback for buttons in the title menu
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
			case R.id.action_palette:
                // Create popup
				DrawConfigDialog dialog = new DrawConfigDialog();
				dialog.setDrawActivity(this);

                // Give current color and size to the popup
				Bundle dialogArgs = new Bundle();
				dialogArgs.putFloat(DrawConfigDialog.VALUE_TOOL_WIDTH, mDrawZone.getToolWidth());
				dialogArgs.putInt(DrawConfigDialog.VALUE_TOOL_COLOR, mDrawZone.getToolColor());

                // Show popup
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

    /**
     * Callback for items in the drawer menu
     */
	public void onDrawerItemSelected(int position){
		switch(position){
			//~ case DRAWER_HEADER_LAYERS_POS:
			case DRAWER_ITEM_BACKGROUND_MOVIE_POS:
				final ImageView drawZone = (ImageView)findViewById(R.id.imageDraw);
				int visib = drawZone.getVisibility();

				if(visib == View.VISIBLE) {
					drawZone.setVisibility(View.INVISIBLE);
				} else {
					drawZone.setVisibility(View.VISIBLE);
				}
				break;
			case DRAWER_ITEM_ONION_PEELING_POS:
				PeelingsCountDialog dialog = new PeelingsCountDialog();
				dialog.show(getFragmentManager(), getString(R.string.dialog_peelings_title));
				// TODO finish implementation
				break;
			//~ case DRAWER_HEADER_PROJECT_POS:
			case DRAWER_ITEM_SAVE_POS:
				Log.w("DrawActivity","onDrawerItemSelected DRAWER_ITEM_SAVE_POS not yet implemented");
				Toast.makeText(this,"Prochainement",Toast.LENGTH_SHORT).show();
				break;
			case DRAWER_ITEM_SAVE_AS_POS:
				Log.w("DrawActivity","onDrawerItemSelected DRAWER_ITEM_SAVE_AS_POS not yet implemented");
				Toast.makeText(this,"Prochainement",Toast.LENGTH_SHORT).show();
				break;
			case DRAWER_ITEM_SHARE_POS:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                sharingIntent.setType(getContentResolver().getType(videoURI));
                sharingIntent.putExtra(Intent.EXTRA_STREAM, videoURI);
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));
				break;
			case DRAWER_ITEM_CLOSE_PROJECT_POS:
                Intent mainActivity = new Intent(this, MainActivity.class);

                // Clear other activities in stack
                mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainActivity);
			default:
				Log.v("DrawActivity","onDrawerItemSelected "+position);
		}
	}

    /**
     * Following methods are callback for buttons
     * in the first row of the drawer menu
     */
	public void onClickButtonFirst(View v) {
        this.setSelectedImage(0);
	}
	
	public void onClickButtonPlay(View v) {
        // TODO Play generated video instead
        Toast.makeText(this, "Vidéo originale", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(videoURI, "video/mp4");
        startActivity(intent);
	}
	
	public void onClickButtonLast(View v) {
        this.setSelectedImage(this.images.size() - 1);
	}

    /**
     * Following methods are callback for buttons
     * in the floating menu
     */
	public void onClickFloatingToolPen(View v) {

        // Change buttons background to highlight selected tool
        FloatingActionButton buttonPen = (FloatingActionButton) findViewById(R.id.floating_button_pen);
        FloatingActionButton buttonEraser = (FloatingActionButton) findViewById(R.id.floating_button_eraser);
        FloatingActionButton buttonLine = (FloatingActionButton) findViewById(R.id.floating_button_line);

        buttonPen.setColorNormalResId(R.color.custom_primary);
        buttonEraser.setColorNormalResId(R.color.white);
        buttonLine.setColorNormalResId(R.color.white);

        // Click action
        mDrawZone.setCurrentTool(DrawZone.TOOL_PEN);

        // Collapse menu (not automatic)
        FloatingActionsMenu floatingMenu = (FloatingActionsMenu) findViewById(R.id.floating_menu);
        floatingMenu.collapse();
	}
	
	public void onClickFloatingToolLine(View v) {
        //mDrawZone.setCurrentTool(DrawZone.TOOL_LINE);
		Log.w("DrawActivity","onClickFloatingToolLine not yet implemented");
		Toast.makeText(this,"Prochainement",Toast.LENGTH_SHORT).show();
	}
	
	public void onClickFloatingToolEraser(View v) {

        // Change buttons background to highlight selected tool
        FloatingActionButton buttonPen = (FloatingActionButton) findViewById(R.id.floating_button_pen);
        FloatingActionButton buttonEraser = (FloatingActionButton) findViewById(R.id.floating_button_eraser);
        FloatingActionButton buttonLine = (FloatingActionButton) findViewById(R.id.floating_button_line);

        buttonPen.setColorNormalResId(R.color.white);
        buttonEraser.setColorNormalResId(R.color.custom_primary);
        buttonLine.setColorNormalResId(R.color.white);

        // Click action
        mDrawZone.setCurrentTool(DrawZone.TOOL_ERASER);

        // Collapse menu (not automatic)
        FloatingActionsMenu floatingMenu = (FloatingActionsMenu) findViewById(R.id.floating_menu);
        floatingMenu.collapse();
	}
	
	
	@Override
	public void onBackPressed() {
		// close activity only if back press two time in less than 2 sec
		long currentTime = Calendar.getInstance().getTimeInMillis();
		if(currentTime - this.lastBackPress < 2000) {
			super.onBackPressed();
		} else {
			Toast.makeText(this,"Appuyez une autre fois pour quitter.",Toast.LENGTH_SHORT).show();
			this.lastBackPress = currentTime;
		}
	}
}
