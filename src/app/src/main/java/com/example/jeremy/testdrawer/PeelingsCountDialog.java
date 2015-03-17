package com.example.jeremy.testdrawer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by jeremy on 08/03/15.
 */
public class PeelingsCountDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Bind seekbar and text
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_peelings, null, false);
        final SeekBar mySeekBar = ((SeekBar) view.findViewById(R.id.seekBarPeelings));

        mySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                Log.d("toto", "tatatta");
                final TextView textView = (TextView) view.findViewById(R.id.textViewPeelings);
                textView.setText("Pelures: " + arg1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("toto", "tatatta");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("toto", "tatatta");
                final TextView textView = (TextView) view.findViewById(R.id.textViewPeelings);
                textView.setText("Pelures: " + seekBar.getProgress());
            }
        });

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_peelings_title)
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

        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_peelings, null);
        builder.setView(layout);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}