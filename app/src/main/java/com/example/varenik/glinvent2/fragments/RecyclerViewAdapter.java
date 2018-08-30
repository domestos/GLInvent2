package com.example.varenik.glinvent2.fragments;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.Values;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<Device> mData;
    Dialog myDialog;

    public RecyclerViewAdapter(Context mContext, List<Device> mData){
        Log.d(Values.TAG_LOG, "run class.RecyclerViewAdapter");
        this.mContext = mContext;
        this.mData = mData;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(Values.TAG_LOG, "run onCreateViewHolder");
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
           Log.d(Values.TAG_LOG, "run onBindViewHolder" + mData.get(i).getNumber() );

           myViewHolder.txItem.setText(mData.get(i).getItem());
           myViewHolder.txNumber.setText(mData.get(i).getNumber());
           myViewHolder.txLocation.setText(mData.get(i).getLocation());
           myViewHolder.txOwner.setText(mData.get(i).getOwner());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


       private TextView txItem;
       private TextView txNumber;
       private TextView txLocation;
       private TextView txOwner;
       private LinearLayout item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        item = itemView.findViewById(R.id.item_id);
        txItem = itemView.findViewById(R.id.tx_item);
        txNumber = itemView.findViewById(R.id.tx_number);
        txLocation = itemView.findViewById(R.id.tx_location);
        txOwner = itemView.findViewById(R.id.tx_owner);
        }
    }
}
