package com.example.varenik.glinvent2.fragments.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.Values;

public class DialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    private Button btnCancel, btnSave;
    private Device device;
    private TextView txNumber, txItem;
    private EditText edLocation, edOwner, edDescription;

    public DialogFragment(){
        }

    @SuppressLint("ValidFragment")
    public DialogFragment(Device device) {
        Log.d(Values.TAG_LOG, "run constructor DialogFragment");
            this.device = device;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.dialog_item, container,false);
        Log.d(Values.TAG_LOG, "run onCreateView on DialogFragment");
        txNumber  = view.findViewById(R.id.tx_dialog_number);
        txItem = view.findViewById(R.id.tx_dialog_item);
        edLocation = view.findViewById(R.id.ed_dialog_location);
        edOwner = view.findViewById(R.id.ed_dialog_owner);
        edDescription = view.findViewById(R.id.ed_dialog_description);

        //will NEED refactoring
        if(device != null){
            Log.d(Values.TAG_LOG, device.getNumber());
            txNumber.setText(device.getNumber());
            txItem.setText(device.getItem());
            edLocation.setText(device.getLocation());
            edOwner.setText(device.getOwner());
            edDescription.setText(device.getDescription());
        }else {
            Log.d(Values.TAG_LOG, "device is NULL");
        }

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
