package com.morganwalkup.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.morganwalkup.router2018.R;
import com.morganwalkup.support.ParentActivity;

/**
 * Created by morganwalkup on 2/8/18.
 * Creates a dialog allowing users to enter new adjacency records
 */

public class AddAdjacencyDialog extends DialogFragment {

    /** On screen widget where user enters ip address */
    private EditText ipAddressEditText;
    /** On screen widget where user enters ll2p address */
    private EditText ll2pAddressEditText;
    /** On screen button to add adjacency record */
    private Button addAdjacencyButton;
    /** On screen button to cancel adjacency record creation */
    private Button cancelButton;

    /** Provides a connection back to the main activity */
    public interface AdjacencyPairListener {
        void onFinishedEditDialog(String ipAddress, String ll2pAddress);
    }

    /** Default constructor */
    public AddAdjacencyDialog() {

    }

    /**
     * Called on the creation of the dialog
     * @param savedInstanceState - The saved state of the app
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Set dialog title
        builder.setTitle("Add Adjacency Record");

        // 3. Set dialog content and save references to screen widgets
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.adjacency_dialog, null);
        ll2pAddressEditText = rootView.findViewById(R.id.adjacencyDialogLL2PAddress);
        ipAddressEditText = rootView.findViewById(R.id.adjacencyDialogIPAddress);
        builder.setView(rootView);

        // 4. Add action buttons and click listeners
        builder.setPositiveButton(R.string.adjacency_dialog_confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                AdjacencyPairListener activity = (AdjacencyPairListener) ParentActivity.getParentActivity();
                activity.onFinishedEditDialog(
                        ipAddressEditText.getText().toString(),
                        ll2pAddressEditText.getText().toString()
                        );
                dismiss();
            }
        });
        builder.setNegativeButton(R.string.adjacency_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dismiss();
            }
        });

        // 5. Create the alert dialog
        AlertDialog dialog = builder.create();

        // 6. Return the alert dialog
        return dialog;
    }
}
