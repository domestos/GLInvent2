package com.example.varenik.glinvent2.fragments;


import android.app.Dialog;
import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
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
import com.example.varenik.glinvent2.fragments.scan.ScanFragment;
import com.example.varenik.glinvent2.fragments.sync.SyncFragment;
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.Values;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements View.OnClickListener {

    Context mContext;
    List<Device> mData;
    Dialog myDialog;
    ScanFragment scanFragment;
    SyncFragment syncFragment;

    public RecyclerViewAdapter(Context mContext, List<Device> mData, ScanFragment scanFragment){
        Log.d(Values.TAG_LOG, "run class.RecyclerViewAdapter");
        this.scanFragment = scanFragment;
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setUpdatedListOfDevices(List<Device> mData) {
        this.mData = mData;
    }


    public RecyclerViewAdapter(Context mContext, List<Device> mData, SyncFragment syncFragment){
        Log.d(Values.TAG_LOG, "run class.RecyclerViewAdapter");
        this.syncFragment = syncFragment;
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(Values.TAG_LOG, "run onCreateViewHolder");
        View view;

        view = LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false);

        final MyViewHolder myViewHolder = new MyViewHolder(view);



        myViewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "selected : "+String.valueOf(myViewHolder.getAdapterPosition()+ ""), Toast.LENGTH_LONG).show();
                Log.d(Values.TAG_LOG, "run onClick: select item: " + myViewHolder.getAdapterPosition());

               if(scanFragment !=null) {
                   scanFragment.runDialog(mData.get(myViewHolder.getAdapterPosition()));
               }

               if (syncFragment !=null){
                    syncFragment.runDialog(mData.get(myViewHolder.getAdapterPosition()));
                }
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
           Log.d(Values.TAG_LOG, "run onBindViewHolder" + mData.get(i).getNumber() );
           if(!Values.sw_inventory_off_on){
               myViewHolder.ll_inventory.setVisibility(View.GONE);
           }else {
               myViewHolder.ll_inventory.setVisibility(View.VISIBLE);
           }
           myViewHolder.txItem.setText(mData.get(i).getItem());
           myViewHolder.txNumber.setText(mData.get(i).getNumber());
           myViewHolder.txLocation.setText(mData.get(i).getLocation());
           myViewHolder.txOwner.setText(mData.get(i).getOwner());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onClick(View v) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{


       private TextView txItem;
       private TextView txNumber;
       private TextView txLocation;
       private TextView txOwner;
       private LinearLayout item;
       private LinearLayout ll_inventory;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

        ll_inventory = itemView.findViewById(R.id.ll_inventory);
        item = itemView.findViewById(R.id.item_id);
        txItem = itemView.findViewById(R.id.tx_item);
        txNumber = itemView.findViewById(R.id.tx_number);
        txLocation = itemView.findViewById(R.id.tx_location);
        txOwner = itemView.findViewById(R.id.tx_owner);
        }
    }
}
