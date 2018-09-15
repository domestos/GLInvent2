package com.example.varenik.glinvent2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class Device implements Parcelable {

    private int id ;
    private String number;
    private String type;
    private String url_info;
    private String item;
    private String name_wks;
    private String owner;
    private String location;
    private String statusInvent;
    private Integer statusSync;
    private String description;

    public Device(int id, String number, String type, String item, String name_wks, String owner, String location, String statusInvent, Integer statusSync, String description, String url_info) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.item = item;
        this.owner = owner;
        this.location = location;
        this.description = description;
        this.statusInvent = statusInvent;
        this.statusSync = statusSync;
        this.name_wks = name_wks;
        this.url_info = url_info;
    }

    protected Device(Parcel in) {
        id = in.readInt();
        number = in.readString();

        type = in.readString();
        item = in.readString();
        name_wks = in.readString();
        owner = in.readString();
        location = in.readString();
        statusInvent = in.readString();
        statusSync = in.readInt();
        description = in.readString();
        url_info = in.readString();
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

    public String getNameWks() {
        return name_wks;
    }

    public void setName_wks(String name_wks) {
        this.name_wks = name_wks;
    }

    public String getType() {
        return type;
    }

    public String getUrlInfo() {
        return url_info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(number);
        parcel.writeString(type);
        parcel.writeString(item);
        parcel.writeString(name_wks);
        parcel.writeString(owner);
        parcel.writeString(location);
        parcel.writeString(statusInvent);
        parcel.writeInt(statusSync);
        parcel.writeString(description);
        parcel.writeString(url_info);
    }
}
