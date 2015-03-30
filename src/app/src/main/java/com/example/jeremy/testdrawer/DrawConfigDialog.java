package com.example.jeremy.testdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeremy on 15/03/15.
 */
public class DrawConfigDialog extends DialogFragment implements View.OnClickListener{

    private HashMap<View, Boolean> colors;
    private int currentColor;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        this.currentColor = 0;

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_draw_title)
                .setPositiveButton(R.string.dialog_default_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DrawActivity.DrawPopupCallback(getSelectedSize(), getSelectedColor());
                    }
                })
                .setNegativeButton(R.string.dialog_default_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Bind colors
        this.colors = new HashMap<View, Boolean>();
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_draw, null);
        View first = layout.findViewById(R.id.pickerBlack);
        first.setScaleX(0.98f);
        first.setScaleY(0.98f);
        colors.put(first, true);

        colors.put(layout.findViewById(R.id.pickerWhite), false);
        colors.put(layout.findViewById(R.id.pickerGray), false);
        colors.put(layout.findViewById(R.id.pickerBlue), false);
        colors.put(layout.findViewById(R.id.pickerRed), false);
        colors.put(layout.findViewById(R.id.pickerYellow), false);
        colors.put(layout.findViewById(R.id.pickerGreen), false);
        colors.put(layout.findViewById(R.id.pickerOrange), false);


        for (Map.Entry<View, Boolean> v : colors.entrySet()) {
            v.getKey().setOnClickListener(this);
            if(!v.getKey().equals(first)) {
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

        if(active) return;

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
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_draw, null);
        final SeekBar seekBar = (SeekBar)layout.findViewById(R.id.seekBar2);
        return seekBar.getProgress();
    }
}
