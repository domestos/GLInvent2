package com.example.varenik.glinvent2.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.model.Values;

public class DialogInventSettingFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_invent_settings, container, false);
        Log.d(Values.TAG_LOG, "run onCreateView on DialogFragment");

        return view;
    }
}
