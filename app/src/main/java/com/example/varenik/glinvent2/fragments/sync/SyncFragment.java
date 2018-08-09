package com.example.varenik.glinvent2.fragments.sync;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.varenik.glinvent2.R;
import com.example.varenik.glinvent2.fragments.RecyclerViewAdapter;
import com.example.varenik.glinvent2.model.Device;

import java.util.ArrayList;
import java.util.List;

public class SyncFragment extends Fragment {

    View v;
    private RecyclerView myrecyclerview;
    private List<Device> listDevice;

    public SyncFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_sync, container,false);

        myrecyclerview = v.findViewById(R.id.container_recyclerview);
        RecyclerViewAdapter recyclerViewAdapter  =  new RecyclerViewAdapter(getContext(), listDevice);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listDevice = new ArrayList<>();
        listDevice.add(new Device(1,"121212/05/10", "item 1 monitor", "lviwks0001","Valera Pelenskyi", "Administration","ok", 1, "my new workstation" ));
        listDevice.add(new Device(2,"121212/05/11", "item 1 computer", "lviwks0002","Valera Pelenskyi", "Administration","ok", 1, "my new workstation2" ));
        listDevice.add(new Device(2,"121212/05/11", "item 1 computer", "lviwks0002","Valera Pelenskyi", "Administration","ok", 1, "my new workstation2" ));
        listDevice.add(new Device(2,"121212/05/11", "item 1 computer", "lviwks0002","Valera Pelenskyi", "Administration","ok", 1, "my new workstation2" ));
        listDevice.add(new Device(2,"121212/05/11", "item 1 computer", "lviwks0002","Valera Pelenskyi", "Administration","ok", 1, "my new workstation2" ));

    }
}
