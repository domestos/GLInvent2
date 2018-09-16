package com.example.varenik.glinvent2.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.varenik.glinvent2.model.Values;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DialogInventSettingFragment extends DialogFragment {
    private Button btnResetInventStatus;
    private TextView tvDialogAllMonitorsCount,tvDialogAllComputersCount,tvDialogFindComputersCount, tvDialogFindMonitorsCount;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_invent_settings, container, false);
        Log.d(Values.TAG_LOG, "run onCreateView on DialogFragment");
        initAllViews(view);
        return view;
    }

    private void initAllViews(View view) {
        btnResetInventStatus = view.findViewById(R.id.btn_reset_status_invent);
        btnResetInventStatus.setOnClickListener(this);
        tvDialogAllMonitorsCount = view.findViewById(R.id.tv_dialog_all_monitors_count);
        tvDialogAllComputersCount = view.findViewById(R.id.tv_dialog_all_computers_count);
        tvDialogFindComputersCount = view.findViewById(R.id.tv_dialog_find_computers_count);
        tvDialogFindMonitorsCount = view.findViewById(R.id.tv_dialog_find_monitors_count);

        getCountALLMonitors();
        getCountAllComputers();
        getCountFindMonitors();
        getCountFindComputers();

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btn_reset_status_invent:
                // for this method need have connect
                resetAllStatusInvent();

                break;
        }
    }

    private void getCountALLMonitors(){
      tvDialogAllMonitorsCount.setText(String.valueOf(SQLiteConnect.getInstance(getContext()).getCountItems("Monitor")));

    }
    private void getCountFindMonitors(){
        tvDialogFindMonitorsCount.setText(String.valueOf(SQLiteConnect.getInstance(getContext()).getFindCountItems("Monitor")));
    }

    private void getCountAllComputers(){
        tvDialogAllComputersCount.setText(String.valueOf(SQLiteConnect.getInstance(getContext()).getCountItems("Computer")));
    }
    private void getCountFindComputers(){
        tvDialogFindComputersCount.setText(String.valueOf(SQLiteConnect.getInstance(getContext()).getFindCountItems("Computer")));
    }


    private void resetAllStatusInvent() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Values.reset_invent_status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int responseSuccess = getSuccess(response);
                        if (responseSuccess != 0) {
                            SQLiteConnect.getInstance(getContext()).resetStatusInvent();
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();

                            getCountALLMonitors();
                            getCountAllComputers();
                            getCountFindMonitors();
                            getCountFindComputers();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "ERROR " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", "1");
                return params;
            }
        };

        MySQLConnect.getInstance(getContext()).addToRequestque(stringRequest);
    }

    private int getSuccess(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            Log.d(TAG, "getSuccess: " + jsonObject.get("success"));
            return (Integer) jsonObject.get("success");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
