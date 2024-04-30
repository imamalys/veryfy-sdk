package com.example.veryfy.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AskMoreImageDialog extends DialogFragment {
    private DialogInterface.OnClickListener noListener;
    private DialogInterface.OnClickListener yesListener;

    public AskMoreImageDialog(DialogInterface.OnClickListener noListener, DialogInterface.OnClickListener yesListener) {
        this.noListener = noListener;
        this.yesListener = yesListener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("ID Back")
                .setMessage("Do you need to capture ID back?")
                .setPositiveButton("Yes", yesListener)
                .setNegativeButton("No", noListener);

        AlertDialog dialog = builder.create();

        // Customize the button layout
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            // Set layout parameters to move the buttons
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 64, 0);  // Adjust the margins as needed

            positiveButton.setLayoutParams(params);
            negativeButton.setLayoutParams(params);
        });

        // Return the customized AlertDialog object
        return dialog;
    }
}
