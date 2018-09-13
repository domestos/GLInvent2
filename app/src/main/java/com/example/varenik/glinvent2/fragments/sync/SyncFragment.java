package com.example.varenik.glinvent2.fragments.sync;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.Values;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SyncFragment extends Fragment {

    private View view;
    private RecyclerView myrecyclerview;
    private List<Device> devices;
    private Button btnSaveToServer;
    private RecyclerViewAdapter recyclerViewAdapter;

    public SyncFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       ;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sync, container, false);

        myrecyclerview = view.findViewById(R.id.container_recyclerview);
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), getNoSyncItems(), this);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter);

        btnSaveToServer = view.findViewById(R.id.btn_save_to_server);
        btnSaveToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncAll(getNoSyncItems(), myrecyclerview.getAdapter() );
            }
        });
        return view;
    }

    public void runDialog(Device device) {
        DialogFragment dialogFragment = new DialogFragment(device);
        dialogFragment.show(getFragmentManager(), "MySyncFragment");
    }

    private List<Device> getNoSyncItems() {
        Log.d(Values.TAG_LOG, "run getNoSyncItems ");
        devices = SQLiteConnect.getInstance(getContext()).getNoSyncItemsFromSQLite();

        if (devices == null) {
            Toast.makeText(getContext(), "SQLite => Tabele isEmpty", Toast.LENGTH_SHORT).show();
        }
        return devices;
    }

    private void syncAll(List<Device> noSyncItems, RecyclerView.Adapter adapter) {
        for (int i=0; noSyncItems.size()>i; i++){
            updateItem(noSyncItems.get(i), adapter);
        }
    }

    private void updateItem(final Device device, final RecyclerView.Adapter adapter) {
        if(device != null) {
            StringRequest stringRequest = new StringRequest (Request.Method.POST, Values.update_item,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            int responseSuccess = getSuccess(response);
                            if(responseSuccess !=0){
                                SQLiteConnect.getInstance(getContext()).updateSyncStatus(device.getId(), Values.STATUS_SYNC_ONLINE);
                                recyclerViewAdapter.setUpdatedListOfDevices(getNoSyncItems());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(),"ERROR "+error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params  = new HashMap<String, String>();
                    Log.d(TAG, "syncAll: ID)"+String.valueOf(device.getId()));
                    params.put("id", String.valueOf(device.getId()));

                    Log.d(TAG, "syncAll: Status Invent "+device.getStatusInvent());
                    params.put("status_invent",device.getStatusInvent());

                    Log.d(TAG, "syncAll: Owner "+device.getOwner());
                    params.put("owner", device.getOwner());

                    Log.d(TAG, "syncAll: Location "+device.getLocation());
                    params.put("location", device.getLocation());

                    Log.d(TAG, "syncAll: Description "+device.getDescription());
                    params.put("description", device.getDescription());
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
            Log.d(TAG, "getSuccess: "+ jsonObject.get("success") );
            return (Integer) jsonObject.get("success") ;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
