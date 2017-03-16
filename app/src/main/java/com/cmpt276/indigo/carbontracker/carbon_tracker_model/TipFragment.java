package com.cmpt276.indigo.carbontracker.carbon_tracker_model;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cmpt276.indigo.carbontracker.JourneyMenu;
import com.cmpt276.indigo.carbontracker.R;

/**
 * Created by parmis on 2017-03-15.
 */

public class TipFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("I need to work on this part.");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();

            }

        });
        builder.setPositiveButton("next tip", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                FragmentManager manager = getFragmentManager();
                TipFragment dialog2 = new TipFragment();
                dialog2.setCancelable(false);
                dialog2.show(manager,"message dialog");

            }

        });




        AlertDialog alert = builder.show();
        alert.setCancelable(false);
        return alert;

    }

    }


