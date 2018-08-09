package com.example.varenik.glinvent2.fragments.manage;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageFragmentCopy.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageFragmentCopy#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFragmentCopy extends android.support.v4.app.Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * My values
     */
    //View elements
    private Button savaButton;
    private Button btnSync;

    private EditText etServerUrl;
    private TextView tvDevicesRowInMYSQL;
    private TextView tvUsersRowInMYSQL;
    private TextView tvDevicesRowInSQLite;
    private TextView tvUsersRowInSQLite;
    private TextView tvConnectStatus;
    private TextView tvSyncStatus;
    //DataBase
    private SQLiteConnect sqLiteConnect;
    private MySQLConnect mySQLConnect;
    //Arrays
    private List<Device> devicesFromSQLite;
    private List<Device> devicesFromMySQL;
    private List<User> usersFromMySQL;
    private List<User> usersFromSQLite;

    private OnFragmentInteractionListener mListener;


    public ManageFragmentCopy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageFragmentCopy newInstance(String param1, String param2) {
        ManageFragmentCopy fragment = new ManageFragmentCopy();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        loadAllValues();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container,false);
        initAllViews(view);
        showCountRowSYNC();
        showCountRowInSQLite();
        showCountRowInMYSQL();
        return view;
    }

    private void loadAllValues() {
        loadURLHost();

        // get ALL Items and Users from MYSQL
        mySQLConnect = MySQLConnect.getInstance(getContext());
        devicesFromMySQL = getAllItemsFromMySQL();
        usersFromMySQL = getAllUsersFromMySQL();

        // get ALL Items from SQLite
        sqLiteConnect = SQLiteConnect.getInstance(getContext());
        devicesFromSQLite = sqLiteConnect.getAllItemsFromSQLite();
        usersFromSQLite = sqLiteConnect.getAllUsersFromSQLite();

    }

    private void initAllViews(View view) {
        savaButton = view.findViewById(R.id.btnSaveUrl);
        savaButton.setOnClickListener(this);

        btnSync= view.findViewById(R.id.btnSync);
        btnSync.setOnClickListener(this);

        etServerUrl = view.findViewById(R.id.etServerUrl);
        etServerUrl.setText(Values.host);

        tvConnectStatus = view.findViewById(R.id.tvConnectStatus);
        tvSyncStatus = view.findViewById(R.id.tvSyncStatus);



      //  tvInfotmDelete = view.findViewById(R.id.tvInfotmDelete);
        tvDevicesRowInMYSQL = view.findViewById(R.id.tvDevicesRowInMYSQL);
        tvUsersRowInMYSQL = view.findViewById(R.id.tvUsersRowInMYSQL);

        tvDevicesRowInSQLite = view.findViewById(R.id.tvDevicesRowInSQLite);
        tvUsersRowInSQLite = view.findViewById(R.id.tvUsersRowInSQLite);

      //  tvRowSYNC = view.findViewById(R.id.tvRowSYNC);

       // etURL = view.findViewById(R.id.etURL);
       // etURL.setText(Const.url_host);

       // btnInsertToSQLite = view.findViewById(R.id.btnIsertTOSQLite);
      //  btnInsertToSQLite.setOnClickListener(this);

//        btnSave = view.findViewById(R.id.btnSave);
//        btnSave.setOnClickListener(this);
//
//        btnDeletSQLite = view.findViewById(R.id.btnDeleteSQLite);
//        btnDeletSQLite.setOnClickListener(this);


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void saveURLHost(String url) {
        if (mListener != null) {
            mListener.saveURLHost(url);
            loadAllValues();

            showCountRowSYNC();
            showCountRowInSQLite();
            showCountRowInMYSQL();
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

        if (devicesFromMySQL != null) {

            deleteAllFromSQLite();
            SQLiteConnect.getInstance(getContext().getApplicationContext()).insertAllItemToSQList(devicesFromMySQL);
            SQLiteConnect.getInstance(getContext().getApplicationContext()).insertAllUsersToSQList(usersFromMySQL);
            if (devicesFromSQLite.isEmpty()) {
                devicesFromSQLite = SQLiteConnect.getInstance(getContext().getApplicationContext()).getAllItemsFromSQLite();
                usersFromSQLite = SQLiteConnect.getInstance(getContext().getApplicationContext()).getAllUsersFromSQLite();
            }
            saveDateSync();
            showCountRowSYNC();

            showCountRowInSQLite();
        }else {
            Toast.makeText(getContext(), "Server is unavailable ", Toast.LENGTH_SHORT).show();
        }
    }


    //===================================================

    private List<Device> getAllItemsFromMySQL() {
        Log.d(Values.TAG_LOG, "run getAllItemsFromMySQL");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Values.server_url,
                new Response.Listener<JSONObject>() {

                    public void onResponse(JSONObject response) {
                        //  Log.d(Const.TAG_LOG, response.toString());
                        devicesFromMySQL = getArrayDevices(response);

                        if (devicesFromMySQL != null) {
                            tvDevicesRowInMYSQL.setText(String.valueOf(devicesFromMySQL.size()));
                            Log.d(Values.TAG_LOG, " result: Devices From MySQL =" + devicesFromMySQL.size());
                            tvConnectStatus.setTextColor(Color.GREEN);
                            tvConnectStatus.setText("Connected");
                        } else {
                            Log.d(Values.TAG_LOG, "result:  Devices From MySQL NULL");
                            showCountRowInMYSQL();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        devicesFromMySQL =null;
                        showCountRowInMYSQL();
                        tvConnectStatus.setTextColor(Color.RED);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                     //       tvConnectStatus.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        }
                        tvConnectStatus.setText("Host is unavailable. \n Check URL or Internet connection ");
                        Toast.makeText(getContext(), "getAllItemsFromMySQL", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        MySQLConnect.getInstance(getContext().getApplicationContext()).addToRequestque(jsonObjectRequest);
        return devicesFromMySQL;
    }

    private List<User> getAllUsersFromMySQL() {
        Log.d(Values.TAG_LOG, "run getAllUsersFromMySQL");
        //  Log.d(Const.TAG_LOG, Const.get_all_users+"");


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Values.get_all_users,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //                 Log.d(Const.TAG_LOG, response.toString());
                        usersFromMySQL = getArrayUsers(response);

                        if (usersFromMySQL != null) {
                            tvUsersRowInMYSQL.setText(String.valueOf(usersFromMySQL.size()));
                            Log.d(Values.TAG_LOG, " result: Users From MySQL =" + usersFromMySQL.size());
                        } else {
                            Log.d(Values.TAG_LOG, "result:  Users From MySQL NULL");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                     //   tvUsersRowInMYSQL.setTextColor(Color.RED);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                       //     tvUsersRowInMYSQL.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        }
                    }
                }
        );
        MySQLConnect.getInstance(getContext().getApplicationContext()).addToRequestque(jsonObjectRequest);
        return usersFromMySQL;
    }

    private void deleteAllFromSQLite() {
        if (SQLiteConnect.getInstance(getContext()).getNoSyncItemsFromSQLite().isEmpty()) {

            int result = SQLiteConnect.getInstance(getContext()).deleteALL();
            Toast.makeText(getContext(), result + " rows was deleted", Toast.LENGTH_LONG).show();
            devicesFromSQLite.clear();
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
        if (devicesFromSQLite.isEmpty()) {
            tvDevicesRowInSQLite.setText(String.valueOf(devicesFromSQLite.size()));
         //   btnInsertToSQLite.setVisibility(View.VISIBLE);
        } else {
            tvDevicesRowInSQLite.setText(String.valueOf(devicesFromSQLite.size()));
          //  btnInsertToSQLite.setVisibility(View.INVISIBLE);
        }
        tvUsersRowInSQLite.setText(String.valueOf(usersFromSQLite.size()));

    }

    private void showCountRowInMYSQL() {
        if (getAllItemsFromMySQL() != null) {
            tvDevicesRowInMYSQL.setText(String.valueOf(devicesFromMySQL.size()));
        }else {
            tvDevicesRowInMYSQL.setText("-NULL");
        }
        if(getAllUsersFromMySQL() !=null){
            tvUsersRowInMYSQL.setText(String.valueOf(usersFromMySQL.size()));
        }else {
           tvUsersRowInMYSQL.setText("-NULL");
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
