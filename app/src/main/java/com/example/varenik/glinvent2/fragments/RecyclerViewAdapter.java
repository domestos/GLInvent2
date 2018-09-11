package com.example.varenik.glinvent2.fragments;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.fragments.scan.ScanFragment;
import com.example.varenik.glinvent2.fragments.sync.SyncFragment;
import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.Values;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{
    View view;
    Context mContext;
    List<Device> mData;
    ScanFragment scanFragment;
    SyncFragment syncFragment;

    public RecyclerViewAdapter(Context mContext, List<Device> mData, ScanFragment scanFragment) {
        Log.d(Values.TAG_LOG, "run class.RecyclerViewAdapter");
        this.scanFragment = scanFragment;
        this.mContext = mContext;
        this.mData = mData;
    }

    public RecyclerViewAdapter(Context mContext, List<Device> mData, SyncFragment syncFragment) {
        Log.d(Values.TAG_LOG, "run class.RecyclerViewAdapter");
        this.syncFragment = syncFragment;
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setUpdatedListOfDevices(List<Device> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(Values.TAG_LOG, "run onCreateViewHolder");
        view = LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "selected : " + String.valueOf(myViewHolder.getAdapterPosition() + ""), Toast.LENGTH_LONG).show();
                Log.d(Values.TAG_LOG, "run onClick: select item: " + myViewHolder.getAdapterPosition());

               if(scanFragment !=null) {
                   scanFragment.runDialog(mData.get(myViewHolder.getAdapterPosition()));
               }

               if (syncFragment !=null){
                    syncFragment.runDialog(mData.get(myViewHolder.getAdapterPosition()));
                }
            }
        });

        myViewHolder.btnCheckInvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scanFragment != null) {
                    scanFragment.runCheckInvent(mData.get(myViewHolder.getAdapterPosition()), myViewHolder.getAdapterPosition());
                    myViewHolder.checkInventStatus(mData.get(myViewHolder.getAdapterPosition()));
                }


                if (syncFragment != null) {
                    Toast.makeText(mContext, "This button dose not work here" , Toast.LENGTH_LONG).show();
                }



            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Log.d(Values.TAG_LOG, "run onBindViewHolder" + mData.get(i).getNumber());
        if (!Values.sw_inventory_off_on) {
            myViewHolder.ll_inventory.setVisibility(View.GONE);
        } else {
            myViewHolder.ll_inventory.setVisibility(View.VISIBLE);
            myViewHolder.ico_phone.setColorFilter(Color.BLACK);
            myViewHolder.ico_server.setColorFilter(Color.BLACK);
            myViewHolder.checkInventStatus(mData.get(i));
        }

        myViewHolder.txItem.setText(mData.get(i).getItem());
        myViewHolder.txNumber.setText(mData.get(i).getNumber());
        myViewHolder.txLocation.setText(mData.get(i).getLocation());
        myViewHolder.txOwner.setText(mData.get(i).getOwner());
    }

    @Override
    public int getItemCount() {
        if(mData !=null) {
            return mData.size();
        }
        return 0;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private Button btnCheckInvent;
        private ImageView ico_phone, ico_server;
        private TextView txItem , txNumber, txLocation, txOwner;
        private LinearLayout item, ll_inventory;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCheckInvent = itemView.findViewById(R.id.btnCheckInvent);
            ico_phone = itemView.findViewById(R.id.ic_phone);

            ico_server = itemView.findViewById(R.id.ic_server);

            ll_inventory = itemView.findViewById(R.id.ll_inventory);
            item = itemView.findViewById(R.id.item_id);
            txItem = itemView.findViewById(R.id.tx_item);
            txNumber = itemView.findViewById(R.id.tx_number);
            txLocation = itemView.findViewById(R.id.tx_location);
            txOwner = itemView.findViewById(R.id.tx_owner);
        }

        /** HELPER METHODS
         * @param device*/
        public void checkInventStatus(Device device) {
            if(device.getStatusInvent().equals(Values.STATUS_FINED)){
                btnCheckInvent.setEnabled(false);
            }else {
                btnCheckInvent.setEnabled(true);
            }
            if(device.getStatusInvent().equals(Values.STATUS_FINED)){
                ico_phone.setColorFilter(Color.GREEN);
            }
            if(device.getStatusSync() == Values.STATUS_SYNC_ONLINE && device.getStatusInvent().equals(Values.STATUS_FINED) ){
                ico_server.setColorFilter(Color.GREEN);
            }
            if(device.getStatusSync() == Values.STATUS_SYNC_OFFLINE && device.getStatusInvent().equals(Values.STATUS_FINED) ){
                ico_server.setColorFilter(Color.RED);
            }

        }

    }
}
