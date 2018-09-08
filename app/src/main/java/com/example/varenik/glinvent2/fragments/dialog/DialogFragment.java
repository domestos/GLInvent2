package com.example.varenik.glinvent2.fragments.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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

import static android.widget.Toast.LENGTH_LONG;

public class DialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    private ArrayAdapter<String> adapterUsers;
    private String[] arrayLocation;
    private String[] arrayUsers;
    private List<User> allUsers;
    private ListView listSearchUser;
    private Spinner spLocation;


    private Button btnCancel, btnSave;
    private Device device;
    private TextView txNumber, txItem;
    private EditText edLocation, edOwner, edDescription;

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
        View view = inflater.inflate(R.layout.dialog_item, container, false);
        Log.d(Values.TAG_LOG, "run onCreateView on DialogFragment");
        initAllViewElements(view);

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
                device.setOwner(edOwner.getText().toString());
                device.setDescription(edDescription.getText().toString());
                editItem(device);
                //save values
                getDialog().dismiss();
                break;
        }

    }

    /**
     * ================ HELPER METHODS
     */

    private void initAllViewElements(View view) {
        // edLocation = view.findViewById(R.id.ed_dialog_location);
        listSearchUser = view.findViewById(R.id.listSearchUsers);
        txNumber = view.findViewById(R.id.tx_dialog_number);
        txItem = view.findViewById(R.id.tx_dialog_item);

        edOwner = view.findViewById(R.id.ed_dialog_owner);
        edDescription = view.findViewById(R.id.ed_dialog_description);

        //will NEED refactoring
        if (device != null) {
            Log.d(Values.TAG_LOG, device.getNumber());
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

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Values.update_item,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), response.toString(), LENGTH_LONG).show();
                            int responseSuccess = getSuccess(response);
                            if (responseSuccess != 0) {
                                // inset to SQLite SATATUS_ONLINE
                                SQLiteConnect.getInstance(getContext()).updateItem(mDevice, Values.STATUS_SYNC_ONLINE);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "MYSQL ERROR " + error.getMessage(), LENGTH_LONG).show();
                            SQLiteConnect.getInstance(getContext()).updateItem(mDevice, Values.STATUS_SYNC_OFFLINE);

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


}
