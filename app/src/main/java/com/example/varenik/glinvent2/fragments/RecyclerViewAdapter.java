package com.example.varenik.glinvent2.fragments;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.model.Device;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mContext;
    List<Device> mData;
   Dialog myDialog;

    public RecyclerViewAdapter(Context mContext, List<Device> mData){
        this.mContext = mContext;
        this.mData = mData;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false);
        final MyViewHolder viewHolder = new MyViewHolder(v);

        //Dialog ini
        myDialog =  new Dialog(mContext);
        myDialog.setContentView(R.layout.dialog_item);


         //tx_dialog_number.setText(mData.get(viewHolder.getAdapterPosition()).toString());
       // ed_dialog_locatio.setText(mData.get(viewHolder.getAdapterPosition()).getLocation());
        // ed_dialog_owner.setText(mData.get(viewHolder.getAdapterPosition()).getOwner());
        //  ed_dialog_decription.setText(mData.get(viewHolder.getAdapterPosition()).getDescription());


        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                TextView tx_dialog_number = (TextView) myDialog.findViewById(R.id.tx_dialog_number);
                EditText ed_dialog_locatio = (EditText) myDialog.findViewById(R.id.ed_dialog_location);
                EditText ed_dialog_owner = (EditText) myDialog.findViewById(R.id.ed_dialog_owner);
                EditText ed_dialog_decription = (EditText) myDialog.findViewById(R.id.ed_dialog_description);

                tx_dialog_number.setText(mData.get(viewHolder.getAdapterPosition()).getNumber());
                ed_dialog_locatio.setText(mData.get(viewHolder.getAdapterPosition()).getLocation());
                ed_dialog_owner.setText(mData.get(viewHolder.getAdapterPosition()).getOwner());
                ed_dialog_decription.setText(mData.get(viewHolder.getAdapterPosition()).getDescription());

                Toast.makeText(mContext, "test clike "+String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_LONG).show();


                myDialog.show();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
           myViewHolder.txNumber.setText(mData.get(i).getNumber());
           myViewHolder.txLocation.setText(mData.get(i).getLocation());
           myViewHolder.txOwner.setText(mData.get(i).getOwner());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
       private TextView txNumber;
       private TextView txLocation;
       private TextView txOwner;
       private LinearLayout item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        item = itemView.findViewById(R.id.item_id);
        txNumber = itemView.findViewById(R.id.tx_number);
        txLocation = itemView.findViewById(R.id.tx_location);
        txOwner = itemView.findViewById(R.id.tx_owner);
        }
    }
}
