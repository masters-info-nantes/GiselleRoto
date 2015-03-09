package com.example.jeremy.giselleroto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by jeremy on 08/03/15.
 */
public class PeelingsCountDialog extends DialogFragment {

    private LayoutInflater inflater;

    public PeelingsCountDialog(){
        super();
    }

    public PeelingsCountDialog(LayoutInflater layout){
        super();
        this.inflater = layout;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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

        View layout = inflater.inflate(R.layout.dialog_peelings, null);
        builder.setView(layout);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}