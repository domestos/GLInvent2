package com.example.varenik.glinvent2.fragments.manage;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ManageFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    private Button savaButton, btnSync;
    private EditText etServerUrl;
    private TextView tvDevicesRowInMYSQL, tvUsersRowInMYSQL, tvDevicesRowInSQLite, tvUsersRowInSQLite, tvConnectStatus, tvSyncStatus;
    //DataBase
    private SQLiteConnect sqLiteConnect;
    private MySQLConnect mySQLConnect;
    //Arrays
    private List<Device> devicesFromPhone, devicesFromServer;
    private List<User>   usersFromPhone,   usersFromServer;
    private SharedPreferences sharedPreferences;


    public ManageFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadAllValues();
    }

    private void loadAllValues() {
      //  loadURLHost();
        loadDateSync();
        // init MYSQLConnect (single values
        // )
            mySQLConnect = MySQLConnect.getInstance(getContext());
        // get ALL Items from SQLite
        sqLiteConnect = SQLiteConnect.getInstance(getContext());
        devicesFromPhone = sqLiteConnect.getAllItemsFromSQLite();
        usersFromPhone = sqLiteConnect.getAllUsersFromSQLite();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        initAllViews(view);
        showAllInfo();
        return view;
    }

    private void initAllViews(View view) {
        savaButton = view.findViewById(R.id.btnSaveUrl);
        savaButton.setOnClickListener(this);

        btnSync = view.findViewById(R.id.btnSync);
        btnSync.setOnClickListener(this);

        etServerUrl = view.findViewById(R.id.etServerUrl);
        etServerUrl.setText(Values.host);

        tvConnectStatus = view.findViewById(R.id.tvConnectStatus);
        tvSyncStatus = view.findViewById(R.id.tvSyncStatus);

        tvDevicesRowInMYSQL = view.findViewById(R.id.tvDevicesRowInMYSQL);
        tvUsersRowInMYSQL = view.findViewById(R.id.tvUsersRowInMYSQL);

        tvDevicesRowInSQLite = view.findViewById(R.id.tvDevicesRowInSQLite);
        tvUsersRowInSQLite = view.findViewById(R.id.tvUsersRowInSQLite);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSaveUrl:
                saveURLHost(etServerUrl.getText().toString());
                loadDateSync();
                loadURLHost();
                showAllInfo();
                break;
            case R.id.btnSync:
                syncItems();
                break;
        }
    }

    private void syncItems() {


        if (devicesFromServer != null) {

            if (!deleteAllFromPhone()){
                return;
            }

            insertAllToPhone();
            if (devicesFromPhone.isEmpty()) {
                devicesFromPhone = sqLiteConnect.getAllItemsFromSQLite();
                usersFromPhone = sqLiteConnect.getAllUsersFromSQLite();
            }else{
                Toast.makeText(getContext(), "NEED DELETE", Toast.LENGTH_SHORT).show();
                return;
            }
            saveDateSync();
            showDateOfLastSync();
            showCountResponseFromPhone();
            showCountResponseFromServer();
        } else {
            Toast.makeText(getContext(), "Server is unavailable ", Toast.LENGTH_SHORT).show();
        }
    }

    //===================== Phone methods ================================
    private void insertAllToPhone() {
        SQLiteConnect.getInstance(getContext().getApplicationContext()).insertAllItemToSQList(devicesFromServer);
        SQLiteConnect.getInstance(getContext().getApplicationContext()).insertAllUsersToSQList(usersFromServer);
    }

    private Boolean deleteAllFromPhone() {
        Log.d(Values.TAG_LOG, "run deleteAllFromSQLite: ");
        if (SQLiteConnect.getInstance(getContext()).getNoSyncItemsFromSQLite().isEmpty()) {
            Log.d(Values.TAG_LOG, "getNoSyncItemsFromSQLite()  is EMPTY");
            int result = SQLiteConnect.getInstance(getContext()).deleteALL();
            Log.d(Values.TAG_LOG, "deleteAllFromSQLite: result " + result);

//            Toast.makeText(getContext(), result + " rows was deleted", Toast.LENGTH_LONG).show();
            devicesFromPhone.clear();
            usersFromPhone.clear();
            return true;
         } else {
            Log.d(Values.TAG_LOG, "SYNC LIST in NOT EMPTY \n Please load items to server");
            tvSyncStatus.setText("SYNC LIST in NOT EMPTY \n Please load items to server");
            tvSyncStatus.setTextColor(Color.RED);
            return false;
        }
    }

    //===================== Server methods ===============================
    private List<Device> getAllItemsFromMySQL() {
        Log.d(Values.TAG_LOG, "run getAllItemsFromMySQL");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Values.server_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                tvConnectStatus.setText("Connected");
                tvConnectStatus.setTextColor(Color.GREEN);
                if((devicesFromServer = getArrayDevices(response) ) != null){
                            tvDevicesRowInMYSQL.setText(String.valueOf(devicesFromServer.size()));
                        }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                devicesFromServer = null;
                        tvDevicesRowInMYSQL.setText("-NULL");
                        tvConnectStatus.setText("Host is unavailable. \n Check URL or Internet connection ");
                       tvConnectStatus.setTextColor(Color.RED);
            }
        });
        MySQLConnect.getInstance(getContext().getApplicationContext()).addToRequestque(jsonObjectRequest);
        return devicesFromServer;
    }


    private List<User> getAllUsersFromMySQL() {
        Log.d(Values.TAG_LOG, "run getAllUsersFromMySQL");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Values.get_all_users,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if( (usersFromServer = getArrayUsers(response) ) !=null){
                            tvUsersRowInMYSQL.setText(String.valueOf(usersFromServer.size()));
                        }else{
                            tvDevicesRowInMYSQL.setText("null");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       usersFromServer = null;
                       tvUsersRowInMYSQL.setText("-NULL");
                    }
                }
        );

        MySQLConnect.getInstance(getContext().getApplicationContext()).addToRequestque(jsonObjectRequest);
        return usersFromServer;
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
                            JO.getString("type"),
                            JO.getString("item"),
                            JO.getString("name_wks"),
                            JO.getString("owner"),
                            JO.getString("location"),
                            JO.getString("status_invent"),
                            Values.STATUS_SYNC_ONLINE,
                            JO.getString("description"),
                            JO.getString("url_info")
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

    // ============================= Save and Load Values ==========================================

    public void saveURLHost(String url) {
        Log.d(Values.TAG_LOG, "run saveUrlHost ||  " + url);
        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedPreferences.edit();
        spEdit.putString("URL", url);
        spEdit.commit();
        Values.host=url;
    }

    public void loadURLHost() {
        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        Values.host = sharedPreferences.getString("URL", "");
        Values.concatUrl(Values.host);
    }

    public void loadDateSync(){
        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        Values.lastSyncDate = sharedPreferences.getString("SyncDate", "");
    }

    public void saveDateSync() {
        Date date = new Date();
        Log.d(Values.TAG_LOG, "run saveDateSync ||  "+date.toString());
        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor spEditDate = sharedPreferences.edit();
        spEditDate.putString("SyncDate", date.toString());
        spEditDate.commit();
        Values.lastSyncDate = date.toString();
    }

    //============================== Show Values ===================================================
    private void showAllInfo(){
     showCountResponseFromServer();
     showCountResponseFromPhone();
     showDateOfLastSync();
    }
    /**
     * this method take the values that was get earlier with method initIAll() and show counts of items into database SQLite  */
    private void showCountResponseFromPhone() {
        if (devicesFromPhone.isEmpty()) {
            tvDevicesRowInSQLite.setText(String.valueOf(devicesFromPhone.size()));
        } else {
            tvDevicesRowInSQLite.setText(String.valueOf(devicesFromPhone.size()));
            //  btnInsertToSQLite.setVisibility(View.INVISIBLE);
        }
        tvUsersRowInSQLite.setText(String.valueOf(usersFromPhone.size()));
    }

    /**
     * this method make request to the server and show counts of items into database MySQL*/
    private void showCountResponseFromServer() {
        if (getAllItemsFromMySQL() != null) {
            tvDevicesRowInMYSQL.setText(String.valueOf(devicesFromServer.size()));
        } else {
            tvDevicesRowInMYSQL.setText("-NULL");
        }
        if (getAllUsersFromMySQL() != null) {
            tvUsersRowInMYSQL.setText(String.valueOf(usersFromServer.size()));
        } else {
            tvUsersRowInMYSQL.setText("-NULL");
        }
    }

    private void showDateOfLastSync() {
        tvSyncStatus.setText(Values.lastSyncDate);
    }

}