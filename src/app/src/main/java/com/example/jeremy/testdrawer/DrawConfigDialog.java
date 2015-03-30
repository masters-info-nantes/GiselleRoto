package com.example.jeremy.testdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeremy on 15/03/15.
 */
public class DrawConfigDialog extends DialogFragment implements View.OnClickListener{

    private HashMap<View, Boolean> colors;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_draw_title)
                .setPositiveButton(R.string.dialog_default_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
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

        for (Map.Entry<View, Boolean> val : colors.entrySet()) {
            if(!val.getKey().equals(v)){
                val.setValue(false);
                val.getKey().setScaleX(0.8f);
                val.getKey().setScaleY(0.8f);
            }
            else {
                val.setValue(true);
            }
        }
    }
    
}
