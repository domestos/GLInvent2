package com.example.varenik.glinvent2.fragments.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.database.mysql.MySQLConnect;
import com.example.varenik.glinvent2.database.sqlite.SQLiteConnect;
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.User;
import com.example.varenik.glinvent2.model.Values;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ArrayAdapter<String> adapterUsers;
    private WebView webView;
    private Switch sw_more;
    private ProgressBar progressBar;
    private String[] arrayLocation;
    private String[] arrayUsers;
    private List<User> allUsers;
    private ListView listSearchUser;
    private Spinner spLocation;
    private  View view;
    private LinearLayout infoError, ll_more_info;
    private ImageButton btnOpenUrlInfo;
    private Button btnCancel, btnSave;
    private Device device;
    private TextView txNumber, txItem, txUrlInfo;
    private EditText edWksName, edOwner, edDescription;
    private OnDialogButtonSelected onDialogButtonSelected;


    public interface OnDialogButtonSelected{
        void dialogResponse(Device device);
    }

    public DialogFragment() {
    }

    @SuppressLint("ValidFragment")
    public DialogFragment(Device device) {
        Log.d(Values.TAG_LOG, "run constructor DialogFragment");
        this.device = device;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allUsers = getAllUsers();
        arrayLocation = getAllLocation();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_item, container, false);
        Log.d(Values.TAG_LOG, "run onCreateView on DialogFragment");
        initAllViewElements(view);
        showProgress(false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onDialogButtonSelected = (OnDialogButtonSelected) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(Values.TAG_LOG, "onAttach: ClassCastException : " +e.getMessage() );
        }
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
                device.setOwner(edOwner.getText().toString());
                device.setDescription(edDescription.getText().toString());
                editItem(device);
                break;
            case (R.id.btn_opne_url_info):
                openUrlInfo(device.getUrlInfo());
                break;
        }

    }

    private void openUrlInfo(String urlInfo) {
        Toast.makeText(getContext(), urlInfo, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlInfo));
        startActivity(intent);


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(sw_more.isChecked()){
            showMoreInfo(true );
        }else {
             showMoreInfo(false );
        }
    }

    private void showMoreInfo(boolean b) {
        if(b){
            ll_more_info.setVisibility(View.VISIBLE);
            btnOpenUrlInfo.setOnClickListener(this);
            edWksName.setText(device.getNameWks());
            txUrlInfo.setText(device.getUrlInfo());
        }else{
            ll_more_info.setVisibility(View.GONE);
            }
    }


    /**
     * ================ HELPER METHODS
     */

    private void initAllViewElements(View view) {
        ll_more_info = view.findViewById(R.id.ll_more_info);
        btnOpenUrlInfo = view.findViewById(R.id.btn_opne_url_info);
        txUrlInfo = view.findViewById(R.id.tx_url_info);
        edWksName = view.findViewById(R.id.ed_wks_name);
        sw_more = view.findViewById(R.id.sw_more);
        listSearchUser = view.findViewById(R.id.listSearchUsers);
        txNumber = view.findViewById(R.id.tx_dialog_number);
        txItem = view.findViewById(R.id.tx_dialog_item);
        progressBar = view.findViewById(R.id.progressBar);
        edOwner = view.findViewById(R.id.ed_dialog_owner);
        edDescription = view.findViewById(R.id.ed_dialog_description);
        infoError = view.findViewById(R.id.id_error_info);
        //will NEED refactoring
        if (device != null) {
            Log.d(Values.TAG_LOG, device.getNumber());
            if(device.getType().equals("Computer")){
                sw_more.setVisibility(View.VISIBLE);
                sw_more.setOnCheckedChangeListener(this);
            }else{
                sw_more.setVisibility(View.GONE);
            }
            txNumber.setText(device.getNumber());
            txItem.setText(device.getItem());
            // edLocation.setText(device.getLocation());
            edOwner.setText(device.getOwner());
            edDescription.setText(device.getDescription());
        } else {
            Log.d(Values.TAG_LOG, "device is NULL");
        }

        viewEditTextLocation(view);
        viewEditTextUser(view);

        btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

    }

    private void viewEditTextLocation(View view) {
        spLocation = (Spinner) view.findViewById(R.id.spLocation);
        ArrayAdapter<String> adapterLocations = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayLocation);
        adapterLocations.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spLocation.setAdapter(adapterLocations);
        setSelectItem(spLocation, device);
        spLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                device.setLocation(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setSelectItem(Spinner spinner, Device mDevice) {
        Log.d("TAG_location", "select Item");
        mDevice.getLocation();
        int i = 0;
        String localtion;
        String chek;
        while (arrayLocation.length > i) {
            localtion = mDevice.getLocation();
            chek = arrayLocation[i];
            if (localtion.equals(chek)) {
                spinner.setSelection(i);
            }
            i++;
        }
    }

    private void viewEditTextUser(View view) {

        if (arrayUsers == null) {
            arrayUsers[0] = "none";
        }
        adapterUsers = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayUsers);
        listSearchUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(Values.TAG_LOG, "onItemClick: " + adapterView.getItemAtPosition(i));
                view.setSelected(true);
                edOwner.setText("" + adapterView.getItemAtPosition(i));
                listSearchUser.setVisibility(View.GONE);
            }
        });

        listSearchUser.setAdapter(adapterUsers);

        edOwner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (!b) {
                    listSearchUser.setVisibility(View.GONE);
                }
            }
        });

        edOwner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapterUsers.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!edOwner.isActivated()) {
                    listSearchUser.setVisibility(View.GONE);
                }
                listSearchUser.setVisibility(View.VISIBLE);
            }


        });

    }

    public List<User> getAllUsers() {
        allUsers = (ArrayList<User>) SQLiteConnect.getInstance(getContext()).getAllUsersFromSQLite();
        arrayUsers = getArrayFromList(allUsers);
        Log.d(Values.TAG_LOG, "onCreate: " + allUsers.size());
        Log.d(Values.TAG_LOG, "onCreate: " + arrayUsers.length);
        return allUsers;
    }

    /**
     * convert List<Users> to String[]<Users>
     */
    private String[] getArrayFromList(List<User> allUsers) {
        String[] users = new String[allUsers.size()];
        for (int i = 0; allUsers.size() > i; i++) {
            users[i] = allUsers.get(i).getName().toString().trim();
            Log.d(Values.TAG_LOG, "getArrayFtomList: " + allUsers.get(i).getName().toString());
        }
        return users;
    }

    public String[] getAllLocation() {
        arrayLocation = SQLiteConnect.getInstance(getContext()).getAllLocationFromSQLite();
        return arrayLocation;
    }

    private void editItem(final Device mDevice) {
        if (mDevice != null) {
            showProgress(true);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Values.update_item,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int responseSuccess = getSuccess(response);
                            if (responseSuccess != 0) {
                                // inset to SQLite SATATUS_ONLINE
                                SQLiteConnect.getInstance(getContext()).updateItem(mDevice, Values.STATUS_SYNC_ONLINE);
                            }
                            dialogResponse(mDevice);
                            showProgress(false);
                            getDialog().dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            SQLiteConnect.getInstance(getContext()).updateItem(mDevice, Values.STATUS_SYNC_OFFLINE);
                            infoError.setVisibility(View.VISIBLE);
                           // Toast.makeText(getContext(), "MYSQL ERROR " + error.getMessage(), LENGTH_LONG).show();
                            dialogResponse(mDevice);
                            showProgress(false);
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", String.valueOf(mDevice.getId()));
                    params.put("owner", mDevice.getOwner());
                    params.put("location", mDevice.getLocation());
                    params.put("description", mDevice.getDescription());
                    return params;
                }
            };

            MySQLConnect.getInstance(getContext()).addToRequestque(stringRequest);
        }

    }

    private void dialogResponse(Device mDevice) {

       switch (this.getTargetRequestCode()){
           //ScanFragment
           case 1:
               onDialogButtonSelected.dialogResponse(mDevice);
               break;
           //SyncFragment
           case 2:
               onDialogButtonSelected.dialogResponse(mDevice);
               break;
       }
    }

    private int getSuccess(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            Log.d(Values.TAG_LOG, "getSuccess: " + jsonObject.get("success"));
            return (Integer) jsonObject.get("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void showProgress(boolean show) {
        btnSave.setEnabled(!show);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
