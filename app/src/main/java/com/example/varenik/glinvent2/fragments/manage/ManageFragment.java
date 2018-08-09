package com.example.varenik.glinvent2.fragments.manage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.database.mysql.MySQLConnect;
import com.example.varenik.glinvent2.database.sqlite.SQLiteConnect;
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.User;
import com.example.varenik.glinvent2.model.Values;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private Button savaButton, btnSync;
    private EditText etServerUrl;
    private TextView tvDeviceServer,
            tvUsersServer,
            tvDevicesPhone,
                     tvUsersRowInSQLite,
                     tvConnectStatus,
                     tvSyncStatus;
    //DataBase
    private SQLiteConnect sqLiteConnect;
    private MySQLConnect mySQLConnect;
    //Arrays
    private List<Device> devicesFromPhone, devicesFromServer;
    private List<User>   usersFromPhone,  eusersFromServer;


    /*========== INI ALL VIEW ELEMENTS===============*/
    private void initAllViews(View view) {
        savaButton = view.findViewById(R.id.btnSaveUrl);
        savaButton.setOnClickListener(this);

        btnSync= view.findViewById(R.id.btnSync);
        btnSync.setOnClickListener(this);

        etServerUrl = view.findViewById(R.id.etServerUrl);
        etServerUrl.setText(Values.host);

        tvConnectStatus = view.findViewById(R.id.tvConnectStatus);
        tvSyncStatus = view.findViewById(R.id.tvSyncStatus);

        tvDeviceServer = view.findViewById(R.id.tvDevicesRowInMYSQL);
        tvUsersServer = view.findViewById(R.id.tvUsersRowInMYSQL);

        tvDevicesPhone = view.findViewById(R.id.tvDevicesRowInSQLite);
        tvUsersRowInSQLite = view.findViewById(R.id.tvUsersRowInSQLite);

    }

    public ManageFragment() {
        // Required empty public constructor
    }

    public static ManageFragment newInstance(String param1, String param2) {
        ManageFragment fragment = new ManageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        loadAllValues();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container,false);
        initAllViews(view);

        return view;
    }

    private void loadAllValues() {
        loadURLHost();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void saveURLHost(String url) {
        if (mListener != null) {
            mListener.saveURLHost(url);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void loadURLHost() {
        if (mListener != null) {
            mListener.loadURLHost();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void saveDateSync() {
        if (mListener != null) {
            mListener.saveDateSync();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveUrl:
               saveURLHost(etServerUrl.getText().toString());
                break;
            case R.id.btnSync:
                syncItems();
                break;
        }
    }

    private void syncItems() {

    }


    //===================================================

    private List<Device> getAllItemsFromMySQL(){
        return devicesFromServer;
    }

    private List<User> getAllUsersFromMySQL() {
        return usersFromServer;
    }

    private void deleteAllFromSQLite() {
        if (SQLiteConnect.getInstance(getContext()).getNoSyncItemsFromSQLite().isEmpty()) {

            int result = SQLiteConnect.getInstance(getContext()).deleteALL();
            Toast.makeText(getContext(), result + " rows was deleted", Toast.LENGTH_LONG).show();
            devicesFromPhone.clear();
            usersFromSQLite.clear();
            showCountRowInSQLite();
            Log.d(Values.TAG_LOG, "deleteAllFromSQLite: result " + result);
        } else {
           // tvInfotmDelete.setTextColor(Color.RED);
            tvSyncStatus.setText("SYNC LIST in NOT EMPTY \n Please load items to server");
            Toast.makeText(getContext(), "SYNC LIST in NOT EMPTY", Toast.LENGTH_SHORT).show();
        }
    }


    // ============================= HELPER METHOD =================================================
    private ArrayList<Device> getArrayDevices(JSONObject response) {
        ArrayList<Device> devices = new ArrayList<Device>();
        try {
            if (response.get("success").equals(1)) {
                JSONArray products = (JSONArray) response.get("products");
                for (int i = 0; i < products.length(); i++) {
                    JSONObject JO = (JSONObject) products.get(i);
                    devices.add(new Device(
                            JO.getInt("id"),
                            JO.getString("number"),
                            JO.getString("item"),
                            JO.getString("name_wks"),
                            JO.getString("owner"),
                            JO.getString("location"),
                            JO.getString("status_invent"),
                            Values.STATUS_SYNC_ONLINE,
                            JO.getString("description")
                    ));
                }
                //  Log.d(TAG, "getArrayDevices: count row = " + products.length());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return devices;
    }

    private ArrayList<User> getArrayUsers(JSONObject response) {
        ArrayList<User> users = new ArrayList<User>();
        try {
            if (response.get("success").equals(1)) {
                JSONArray jsonUsers = (JSONArray) response.get("users");
                for (int i = 0; i < jsonUsers.length(); i++) {
                    users.add(new User(jsonUsers.get(i).toString()));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "ERROR " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return users;
    }

    private void showCountRowInSQLite() {
        if (devicesFromPhone.isEmpty()) {
            tvDevicesPhone.setText(String.valueOf(devicesFromPhone.size()));
         //   btnInsertToSQLite.setVisibility(View.VISIBLE);
        } else {
            tvDevicesPhone.setText(String.valueOf(devicesFromPhone.size()));
          //  btnInsertToSQLite.setVisibility(View.INVISIBLE);
        }
        tvUsersRowInSQLite.setText(String.valueOf(usersFromSQLite.size()));

    }

    private void showCountRowInMYSQL() {
        if (getAllItemsFromMySQL() != null) {
            tvDeviceServer.setText(String.valueOf(devicesFromServer.size()));
        }else {
            tvDeviceServer.setText("-NULL");
        }
        if(getAllUsersFromMySQL() !=null){
            tvUsersServer.setText(String.valueOf(usersFromServer.size()));
        }else {
           tvUsersServer.setText("-NULL");
        }
    }

    private void showCountRowSYNC() {

        tvSyncStatus.setText(Values.lastSyncDate);

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void saveURLHost(String url);

        void loadURLHost();

        void saveDateSync();
    }
}
