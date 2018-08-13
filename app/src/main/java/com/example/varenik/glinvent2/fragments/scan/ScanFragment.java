package com.example.varenik.glinvent2.fragments.scan;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.database.sqlite.SQLiteConnect;
import com.example.varenik.glinvent2.fragments.RecyclerViewAdapter;
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.Values;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;

import at.markushi.ui.CircleButton;

import static android.content.ContentValues.TAG;


public class ScanFragment extends Fragment implements View.OnClickListener {
    private EditText etNumber;
    private CircleButton btnScan;
    private Button btnSearch;
    private List<Device> devices;
    private RecyclerView myrecyclerview;
    private OnFragmentInteractionListener mListener;

    public ScanFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
       //     mParam1 = getArguments().getString(ARG_PARAM1);
       //     mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        myrecyclerview = view.findViewById(R.id.container_recyclerview_scan);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnScan = view.findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);

        etNumber = view.findViewById(R.id.etNumber);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);


        return  view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        }
    }

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
            Toast.makeText(getContext(),"find "+ devices.size()+ " items ", Toast.LENGTH_LONG).show();
            Log.d(Values.TAG_LOG, "find: "+devices.size());

            myrecyclerview.setAdapter(new RecyclerViewAdapter((Context) mListener, devices));
        } else {
            Toast.makeText(getContext(), "No find", Toast.LENGTH_LONG).show();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
