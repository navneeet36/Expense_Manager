package com.example.hp.expense_manager.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.expense_manager.R;


/**
 * Created by hp on 22-Mar-17.
 */
public class CustomDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View rootView = inflater.inflate(R.layout.progressbar, container,
                    false);
        return rootView;
    }

}
