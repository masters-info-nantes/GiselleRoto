package fr.univnantes.giselleroto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jeremy.testdrawer.R;

/**
 * Created by jeremy on 08/03/15.
 */
public class PeelingsCountDialog extends DialogFragment {

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

		View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_peelings, null);

        // Bind seekbar and text
        final TextView textView = (TextView) layout.findViewById(R.id.textViewPeelings);
        final SeekBar mySeekBar = ((SeekBar) layout.findViewById(R.id.seekBarPeelings));

        SeekBar.OnSeekBarChangeListener yourSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                textView.setText("Pelures: " + progress);
            }
        };
        mySeekBar.setOnSeekBarChangeListener(yourSeekBarListener);

		// Create the AlertDialog object and return it
        builder.setView(layout);
		return builder.create();
	}
}
