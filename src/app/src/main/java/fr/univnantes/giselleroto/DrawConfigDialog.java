package fr.univnantes.giselleroto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.jeremy.testdrawer.R;

import java.util.HashMap;
import java.util.Map;

public class DrawConfigDialog extends DialogFragment implements View.OnClickListener{
	
	public final static String VALUE_TOOL_WIDTH = "DrawConfigDialog.value.TOOL_WIDTH";
	public final static String VALUE_TOOL_COLOR = "DrawConfigDialog.value.TOOL_COLOR";
	
	private HashMap<View, Boolean> colors;
	private SparseArray<View> colorsToPickers;// like HashMap but int -> Object
	
	private DrawActivity mDrawActivity;
	
	private int seekBarProgress;
	private int currentColorId;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_draw_title)
				.setPositiveButton(R.string.dialog_default_positive, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mDrawActivity.drawPopupCallback(
                                getSelectedSize(),
                                getSelectedColorId()
                        );
					}
				})
				.setNegativeButton(R.string.dialog_default_negative, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
					}
				});

		// Bind colors
		this.colors = new HashMap<View, Boolean>();
		this.colorsToPickers = new SparseArray<View>();
		View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_draw, null);
		
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
		colorsToPickers.put(R.color.material_black,tmp);
		
		tmp = layout.findViewById(R.id.pickerWhite);
		colors.put(tmp, false);
		colorsToPickers.put(R.color.material_white,tmp);
		
		tmp = layout.findViewById(R.id.pickerGray);
		colors.put(tmp, false);
		colorsToPickers.put(R.color.material_gray,tmp);
		
		tmp = layout.findViewById(R.id.pickerBlue);
		colors.put(tmp, false);
		colorsToPickers.put(R.color.material_dark_blue,tmp);
		
		tmp = layout.findViewById(R.id.pickerRed);
		colors.put(tmp, false);
		colorsToPickers.put(R.color.material_red,tmp);
		
		tmp = layout.findViewById(R.id.pickerYellow);
		colors.put(tmp, false);
		colorsToPickers.put(R.color.material_yellow,tmp);
		
		tmp = layout.findViewById(R.id.pickerGreen);
		colors.put(tmp, false);
		colorsToPickers.put(R.color.material_green,tmp);
		
		tmp = layout.findViewById(R.id.pickerOrange);
		colors.put(tmp, false);
		colorsToPickers.put(R.color.material_orange,tmp);

		int bundleColor = getArguments().getInt(DrawConfigDialog.VALUE_TOOL_COLOR,0);
		View selected = bundleColor != 0 ? colorsToPickers.get(Integer.valueOf(bundleColor)) : layout.findViewById(R.id.pickerBlack);
        this.currentColorId = bundleColor;

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

		for (Map.Entry<View, Boolean> val : colors.entrySet()) {
			if(!val.getKey().equals(v)){
				val.setValue(false);
				val.getKey().setScaleX(0.8f);
				val.getKey().setScaleY(0.8f);
			}
			else {
				val.setValue(true);
				for(int i=0;i<this.colorsToPickers.size();i++) {// SparseArray not iterable
					int color = this.colorsToPickers.keyAt(i);
					View picker = this.colorsToPickers.valueAt(i);
					if(picker.equals(val.getKey())) {
						this.currentColorId = color;
						break;
					}
				}
			}
		}
	}

	private int getSelectedColorId(){
		return this.currentColorId;
	}

	private int getSelectedSize(){
		return this.seekBarProgress;
	}
	
	private void setSelectedSize(int progress){
		this.seekBarProgress = progress;
	}
	
	public void setDrawActivity(DrawActivity drawActivity) {
		this.mDrawActivity = drawActivity;
	}
}
