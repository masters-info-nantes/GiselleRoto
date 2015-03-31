package com.example.jeremy.testdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeremy on 15/03/15.
 */
public class DrawConfigDialog extends DialogFragment implements View.OnClickListener{
	
	public final static String VALUE_TOOL_WIDTH = "DrawConfigDialog.value.TOOL_WIDTH";
	public final static String VALUE_TOOL_COLOR = "DrawConfigDialog.value.TOOL_COLOR";
	
	private HashMap<View, Boolean> colors;
	private HashMap<Integer, View> colorsToPickers;
	private int currentColor;
	private DrawActivity mDrawActivity;
	
	private int seekBarProgress;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		this.currentColor = 0;
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_draw_title)
				.setPositiveButton(R.string.dialog_default_positive, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mDrawActivity.drawPopupCallback(getSelectedSize(), getSelectedColor());
					}
				})
				.setNegativeButton(R.string.dialog_default_negative, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});

		// Bind colors
		this.colors = new HashMap<View, Boolean>();
		this.colorsToPickers = new HashMap<Integer, View>();
		View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_draw, null);
		
		//~ View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_draw, null);
		final TextView textSeekBar = (TextView)layout.findViewById(R.id.textViewToolWidth);
		final SeekBar seekBar = (SeekBar)layout.findViewById(R.id.seekBarToolWidth);
		final DrawConfigDialog mDrawConfigDialog = this;
		OnSeekBarChangeListener yourSeekBarListener = new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
				mDrawConfigDialog.setSelectedSize(progress);
				String str = textSeekBar.getText().toString();
				textSeekBar.setText(str.substring(0,str.lastIndexOf('('))+"("+progress+")");
			}
		};
		seekBar.setOnSeekBarChangeListener(yourSeekBarListener);
		
		float bundleToolWidth = getArguments().getFloat(DrawConfigDialog.VALUE_TOOL_WIDTH,-1.0f);
		seekBar.setProgress(bundleToolWidth > -1.0f ? Float.valueOf(bundleToolWidth).intValue() : 0);
		
		Resources res = mDrawActivity.getResources();
		
		View tmp = layout.findViewById(R.id.pickerBlack);
		colors.put(tmp, true);
		colorsToPickers.put(Integer.valueOf(res.getColor(R.color.material_black)),tmp);
		
		tmp = layout.findViewById(R.id.pickerWhite);
		colors.put(tmp, false);
		colorsToPickers.put(Integer.valueOf(res.getColor(R.color.material_white)),tmp);
		
		tmp = layout.findViewById(R.id.pickerGray);
		colors.put(tmp, false);
		colorsToPickers.put(Integer.valueOf(res.getColor(R.color.material_gray)),tmp);
		
		tmp = layout.findViewById(R.id.pickerBlue);
		colors.put(tmp, false);
		colorsToPickers.put(Integer.valueOf(res.getColor(R.color.material_dark_blue)),tmp);
		
		tmp = layout.findViewById(R.id.pickerRed);
		colors.put(tmp, false);
		colorsToPickers.put(Integer.valueOf(res.getColor(R.color.material_red)),tmp);
		
		Log.v("DrawConfigDialog","R.color.material_red = "+(R.color.material_red)
						+" | "+res.getColor(R.color.material_red)
						+" | "+Integer.valueOf(res.getColor(R.color.material_red)));// TODO remove just for testing color
		mDrawActivity.drawPopupCallback(60, res.getColor(R.color.material_red));// TODO remove just for testing color
		
		tmp = layout.findViewById(R.id.pickerYellow);
		colors.put(tmp, false);
		colorsToPickers.put(Integer.valueOf(res.getColor(R.color.material_yellow)),tmp);
		
		tmp = layout.findViewById(R.id.pickerGreen);
		colors.put(tmp, false);
		colorsToPickers.put(Integer.valueOf(res.getColor(R.color.material_green)),tmp);
		
		tmp = layout.findViewById(R.id.pickerOrange);
		colors.put(tmp, false);
		colorsToPickers.put(Integer.valueOf(res.getColor(R.color.material_orange)),tmp);
		
		int bundleColor = getArguments().getInt(DrawConfigDialog.VALUE_TOOL_COLOR,0);
		View selected = bundleColor != 0 ? colorsToPickers.get(Integer.valueOf(bundleColor)) : layout.findViewById(R.id.pickerBlack);
		Log.v("DrawConfigDialog","bundleColor="+bundleColor+" | "+Integer.valueOf(bundleColor)+" selected"+selected);// TODO remove just for testing color
		
		for (Map.Entry<View, Boolean> v : colors.entrySet()) {
			v.getKey().setOnClickListener(this);
			if(v.getKey().equals(selected)) {
				v.getKey().setScaleX(0.98f);
				v.getKey().setScaleY(0.98f);
				v.setValue(true);
			} else {
				v.getKey().setScaleX(0.8f);
				v.getKey().setScaleY(0.8f);
			}
		}

		// Create the AlertDialog object and return it
		builder.setView(layout);
		return builder.create();
	}

	@Override
	public void onClick(View v) {
		Boolean active = colors.get(v);

		if(active) {
			return;
		}
		v.setScaleX(0.98f);
		v.setScaleY(0.98f);

		int i = 0;
		for (Map.Entry<View, Boolean> val : colors.entrySet()) {
			if(!val.getKey().equals(v)){
				val.setValue(false);
				val.getKey().setScaleX(0.8f);
				val.getKey().setScaleY(0.8f);
			}
			else {
				val.setValue(true);
				this.currentColor = HashMapIndexToColor(i);
			}
			i++;
		}
	}

	// see values/colors
	private int HashMapIndexToColor(int index) {
		switch (index){
			//case 0: return 0x000000;
			//case 1: return 0xFFFFFF;
			//case 2: return 0x9E9E9E;

			case 0: return Color.BLACK;
			case 1: return Color.BLUE;
			case 2: return Color.RED;


			case 3: return 0x303F9F;
			case 4: return 0xFF5252;
			case 5: return 0xFFEB3B;
			case 6: return 0x4CAF50;
			case 7: return 0xFF9800;
		}
		return 0;
	}

	public int getSelectedColor(){
		return this.currentColor;
	}

	public int getSelectedSize(){
		//~ View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_draw, null);
		//~ final SeekBar seekBar = (SeekBar)layout.findViewById(R.id.seekBarToolWidth);
		//~ return seekBar.getProgress();
		return this.seekBarProgress;
	}
	
	private void setSelectedSize(int progress){
		this.seekBarProgress = progress;
	}
	
	public void setDrawActivity(DrawActivity drawActivity) {
		this.mDrawActivity = drawActivity;
	}
}
