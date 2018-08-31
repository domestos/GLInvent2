package com.example.varenik.glinvent2.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.model.Values;

public class DialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    private Button btnCancel, btnSave;

    public DialogFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.dialog_item, container,false);

       btnCancel = view.findViewById(R.id.btn_cancel);
       btnCancel.setOnClickListener(this);

       btnSave = view.findViewById(R.id.btn_save);
       btnSave.setOnClickListener(this);

       return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.btn_cancel):
                Log.d(Values.TAG_LOG, "onClick: closing dialog");
                getDialog().dismiss();
                break;
            case (R.id.btn_save):
                Log.d(Values.TAG_LOG, "onClick: save values from dialog");
                //save values
                getDialog().dismiss();
                break;

        }

    }
}
