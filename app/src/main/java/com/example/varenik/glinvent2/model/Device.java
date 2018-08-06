package com.example.varenik.glinvent2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class Device implements Parcelable {

    private int id ;
    private String number;
    private String item;
    private String name_wks;
    private String owner;
    private String location;
    private String statusInvent;
    private Integer statusSync;
    private String description;

    public Device(int id, String number, String item, String name_wks, String owner, String location, String statusInvent, Integer statusSync, String description) {
        this.id = id;
        this.number = number;
        this.item = item;
        this.owner = owner;
        this.location = location;
        this.description = description;
        this.statusInvent = statusInvent;
        this.statusSync = statusSync;
        this.name_wks = name_wks;
    }

    protected Device(Parcel in) {
        id = in.readInt();
        number = in.readString();
        item = in.readString();
        name_wks = in.readString();
        owner = in.readString();
        location = in.readString();
        statusInvent = in.readString();
        statusSync = in.readInt();
        description = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    public String getStatusInvent() {
        return statusInvent;
    }

    public void setStatusInvent(String statusInvent) {
        this.statusInvent = statusInvent;
    }

    public Integer getStatusSync() {
        return statusSync;
    }

    public void setStatusSync(Integer statusSync) {
        this.statusSync = statusSync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName_wks() {
        return name_wks;
    }

    public void setName_wks(String name_wks) {
        this.name_wks = name_wks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(number);
        parcel.writeString(item);
        parcel.writeString(name_wks);
        parcel.writeString(owner);
        parcel.writeString(location);
        parcel.writeString(statusInvent);
        parcel.writeInt(statusSync);
        parcel.writeString(description);
    }
}
