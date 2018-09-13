package com.example.varenik.glinvent2.fragments.scan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.database.mysql.MySQLConnect;
import com.example.varenik.glinvent2.database.sqlite.SQLiteConnect;
import com.example.varenik.glinvent2.fragments.RecyclerViewAdapter;
import com.example.varenik.glinvent2.fragments.dialog.DialogFragment;
import com.example.varenik.glinvent2.fragments.dialog.DialogInventSettingFragment;
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.Values;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.markushi.ui.CircleButton;

import static com.example.varenik.glinvent2.model.Values.STATUS_FINED;
import static com.example.varenik.glinvent2.model.Values.STATUS_SYNC_OFFLINE;
import static com.example.varenik.glinvent2.model.Values.STATUS_SYNC_ONLINE;


public class ScanFragment extends Fragment implements DialogFragment.OnDialogButtonSelected, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private View view;
    private EditText etNumber;
    private CircleButton btnScan;
    private Button btnSearch;
    private ImageButton btnInventSettings;
    private Switch swInventoryBtn;
    private List<Device> devices;
    private RecyclerView myrecyclerview;
    private OnFragmentInteractionListener mListener;
    private RecyclerViewAdapter recyclerViewAdapter;

    public ScanFragment() {
        // Required empty public constructor
    }

//    // TODO: Rename and change types and number of parameters
//    public static ScanFragment newInstance(String param1, String param2) {
//        ScanFragment fragment = new ScanFragment();
//        Bundle args = new Bundle();
//
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//       //     mParam1 = getArguments().getString(ARG_PARAM1);
//       //     mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan, container, false);


        myrecyclerview = view.findViewById(R.id.container_recyclerview_scan);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewAdapter = new RecyclerViewAdapter((Context) mListener, devices, this);
        myrecyclerview.setAdapter(recyclerViewAdapter);

        btnInventSettings  = view.findViewById(R.id.btn_invent_settings);
        btnInventSettings.setOnClickListener(this);

        btnScan = view.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);

        etNumber = view.findViewById(R.id.etNumber);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);

        swInventoryBtn = view.findViewById(R.id.sw_invntory_btn);
        swInventoryBtn.setOnCheckedChangeListener(this);

        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(swInventoryBtn.isChecked()){
            //on
            Values.sw_inventory_off_on = true;
            btnInventSettings.setVisibility(View.VISIBLE);
        }else {
            //of
            Values.sw_inventory_off_on = false;
            btnInventSettings.setVisibility(View.GONE);
        }
        if(!etNumber.getText().toString().isEmpty()){
            findDevices(etNumber.getText().toString());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnScan:
                IntentIntegrator integrator = new IntentIntegrator(this.getActivity()) {
                    @Override
                    protected void startActivityForResult(Intent integrator, int code) {
                        ScanFragment.this.startActivityForResult(integrator, 312); // REQUEST_CODE override
                    }
                };
                integrator.initiateScan();
                break;
            case R.id.btnSearch:
                findDevices(etNumber.getText().toString());
                break;

            case R.id.btn_invent_settings:
                DialogInventSettingFragment dialogInventSettingFragment = new DialogInventSettingFragment();
                dialogInventSettingFragment.show(getFragmentManager(), "DialogInventSettings");
                break;

        }
    }

    public void runCheckInvent(final Device device, final int adapterPosition) {
        Log.d(Values.TAG_LOG, "CheckInvent: ");
       // ic_server = myrecyclerview.findContainingViewHolder(view).itemView.findViewById(R.id.ic_server);

        if(device != null) {
            StringRequest  stringRequest = new StringRequest (Request.Method.POST, Values.update_status_invent_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                            int responseSuccess = getSuccess(response);
                            if(responseSuccess !=0){
                                // inset to SQLite SATATUS_ONLINE
                                SQLiteConnect.getInstance(getContext()).updateSyncStatus(device.getId(), STATUS_SYNC_ONLINE);
                                SQLiteConnect.getInstance(getContext()).updateStatusInvent(device.getId(), STATUS_SYNC_ONLINE);
                                device.setStatusSync(STATUS_SYNC_ONLINE);
                                device.setStatusInvent(STATUS_FINED);
                                myrecyclerview.getAdapter().notifyItemChanged(adapterPosition,device);
                                Toast.makeText(getActivity(),"MYSQL and SQLite are Success ",Toast.LENGTH_LONG).show();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(),"ERROR! \n Server unavailable",Toast.LENGTH_LONG).show();
                            SQLiteConnect.getInstance(getContext()).updateStatusInvent(device.getId(),STATUS_SYNC_OFFLINE);
                            device.setStatusSync(STATUS_SYNC_OFFLINE);
                            device.setStatusInvent(STATUS_FINED);
                            myrecyclerview.getAdapter().notifyItemChanged(adapterPosition,device);
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params  = new HashMap<String, String>();
                    Log.d(Values.TAG_LOG, "getParams: id = "+device.getId());
                    params.put("id", String.valueOf(device.getId()));
                    params.put("method", "method_fined");
                    params.put("status_invent", Values.STATUS_FINED);
                    return params;
                }
            };

            MySQLConnect.getInstance(getContext()).addToRequestque(stringRequest);

        }

    } //end CheckInvent

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Log.d(Values.TAG_LOG, "onActivityResult: Fragment  requestCode =" + requestCode + " resulte " + data.getStringExtra("SCAN_RESULT"));
            etNumber.setText(data.getStringExtra("SCAN_RESULT"));
            findDevices(data.getStringExtra("SCAN_RESULT"));
        } else {
            Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
        }
    }

    private void findDevices(String etNumber) {
        Log.d(Values.TAG_LOG, "run findDevices");
        devices = SQLiteConnect.getInstance(getContext()).getDevicesFromSQLite(etNumber);
        if (devices != null) {
            //Toast.makeText(getContext(),"find "+ devices.size()+ " items ", Toast.LENGTH_LONG).show();
            Log.d(Values.TAG_LOG, "find: "+devices.size());
            recyclerViewAdapter.setUpdatedListOfDevices(devices);
            myrecyclerview.getAdapter().notifyDataSetChanged();
          } else {
            Toast.makeText(getContext(), "No find", Toast.LENGTH_LONG).show();
        }
    }

    public void runDialog(Device device){
        DialogFragment dialogFragment = new DialogFragment(device);
        dialogFragment.setTargetFragment(this, 1);
        dialogFragment.show(getFragmentManager(), "MySyncFragment");
    }

    /**  HELPER METHODS */
    private int getSuccess(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            Log.d(Values.TAG_LOG, "getSuccess: "+ jsonObject.get("success") );
            return (Integer) jsonObject.get("success") ;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void dialogRespons(Device device) {
     //   myrecyclerview.getAdapter().notifyDataSetChanged();
        findDevices(etNumber.getText().toString());
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class OnScanFragmentInteractionListener {
    }
}
