package com.example.varenik.glinvent2.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.Values;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class DialogInventSettingFragment extends DialogFragment {
    private Button btnResetInventStatus;
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

    private void resetAllStatusInvent() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Values.reset_invent_status,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int responseSuccess = getSuccess(response);
                        if (responseSuccess != 0) {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
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
