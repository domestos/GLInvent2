package com.example.varenik.glinvent2.database.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;


import com.example.varenik.glinvent2.model.Device;
import com.example.varenik.glinvent2.model.User;
import com.example.varenik.glinvent2.model.Values;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by valera.pelenskyi on 26.10.17.
 */

public class SQLiteConnect {

    private static SQLiteConnect sqLiteConnect;
    private static Context mCtx;
    private static DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    // ================= Singelton Methods =====================================================
    private SQLiteConnect(Context context) {
        mCtx = context;
        dbHelper = getDbHelper();
        sqLiteDatabase = getSqLiteDatabase();
    }

    public static synchronized SQLiteConnect getInstance(Context context) {
        if (sqLiteConnect == null) {
            sqLiteConnect = new SQLiteConnect(context);
        }
        return sqLiteConnect;
    }

    private DBHelper getDbHelper() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(mCtx.getApplicationContext());
        }
        return dbHelper;
    }

    public SQLiteDatabase getSqLiteDatabase() {
        if (dbHelper != null) {
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    // ================= insertAllUsersToSQList =====================================================
    public void insertAllUsersToSQList(List<User> users) {
        Log.d(Values.TAG_LOG, "run isertAllUsersToSQList");
        String sql = "INSERT INTO "+ DBHelper.TABLE_USERS + " VALUES(?);";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        if(users !=null){
            sqLiteDatabase.beginTransaction();
            Log.d(Values.TAG_LOG, "beginTransaction ");
            try {
                for (int i =0; i< users.size();i++){
                    sqLiteStatement.clearBindings();
                    sqLiteStatement.bindString(1, users.get(i).getName());
                    sqLiteStatement.execute();
                }
                sqLiteDatabase.setTransactionSuccessful();
            }finally {
                sqLiteDatabase.endTransaction();
            }
        }else {
            Log.d(Values.TAG_LOG, "Can't insert. Users is Empty");
            //  return false;
        }//end IF


    }

        // ================= insertAllItemToSQList =====================================================
    public void insertAllItemToSQList(List<Device> devices) {
        Log.d(Values.TAG_LOG, "run insertAllItemToSQList");
        //
        String sql = "INSERT INTO " + DBHelper.TABLE_INVENTORY + " VALUES(?,?,?,?,?,?,?,?,?);";
        SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
        if (devices != null) {
            sqLiteDatabase.beginTransaction();
            Log.d(Values.TAG_LOG, "beginTransaction ");
            try {
                for (int i = 0; i < devices.size(); i++) {
                    Log.e(Values.TAG_LOG, "insert  " + devices.get(i).getId() + " || " + devices.get(i).getNumber() + " ||  " + devices.get(i).getOwner());
                    sqLiteStatement.clearBindings();

                    sqLiteStatement.bindLong(1, devices.get(i).getId());
                    sqLiteStatement.bindString(2, devices.get(i).getNumber().trim());
                    sqLiteStatement.bindString(3, devices.get(i).getItem());
                    sqLiteStatement.bindString(4, devices.get(i).getName_wks().trim());
                    sqLiteStatement.bindString(5, devices.get(i).getOwner().trim());
                    sqLiteStatement.bindString(6, devices.get(i).getLocation().trim());
                    sqLiteStatement.bindString(7, devices.get(i).getStatusInvent());
                    sqLiteStatement.bindLong(8, Values.STATUS_SYNC_ONLINE);
                    sqLiteStatement.bindString(9, devices.get(i).getDescription());
                    sqLiteStatement.execute();

                    /*
                    *
                    1 KEY_ID+" integer primary key, "+
                    2 KEY_NUMBER+" text, "+
                    3 KEY_ITEM+" text, "+
                    4 KEY_NAME_WKS+" text, "+
                    5 KEY_OWNER+" text, "+
                    6 KEY_LOCATION+" text, "+
                    7 KEY_STATUS_INVENT+" text, "+
                    8 KEY_STATUS_SYNC+" integer, "+
                    9 KEY_DESCRIPTION +" text "+*/
                }
                sqLiteDatabase.setTransactionSuccessful();

            } finally {
                sqLiteDatabase.endTransaction();
                Log.d(Values.TAG_LOG, "endTransaction ");
            }
            //return true;
        } else {
            Log.d(Values.TAG_LOG, "Can't insert. Devices is Empty");
            //  return false;
        }//end IF


    }


    // ================= getAllUsersFromSQLite =====================================================
    public List<User> getAllUsersFromSQLite() {
        List<User> users = new ArrayList<User>();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
        users = wrapperUsers(cursor);
        cursor.close();
        if (users != null) {
          //  Log.d(Values.TAG_LOG, "result: Users into SQLite =" + users.size());
        } else {
            Log.d(Values.TAG_LOG, "result:   Users into SQLite is NULL");
        }
        return users;
    }

    // ================= getAllItemsFromSQLite =====================================================
    public List<Device> getAllItemsFromSQLite() {
        List<Device> devices = new ArrayList<Device>();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_INVENTORY, null, null, null, null, null, null);
        devices = wrapperDevices(cursor);
        cursor.close();
        if (devices != null) {
            Log.d(Values.TAG_LOG, "result:  From SQLite =" + devices.size());
        } else {
            Log.d(Values.TAG_LOG, "result:  From SQLite NULL");
        }
        return devices;
    }

    // ================= getItemFromSQLite =========================================================
    public Device getItemFromSQLite(String number) {
        Log.d(Values.TAG_LOG, "SQLiteConnect getItemFromSQLite");
        Device device = null;
        String[] selectArgs = new String[]{number};
        String select = DBHelper.KEY_NUMBER + " = ? ";
        //Device devices = new Device();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_INVENTORY, null, select, selectArgs, null, null, null);
        try {
            device = wrapperDevices(cursor).get(0);
        } catch (Exception e) {
            return null;
        }
        cursor.close();
        return device;
    }

    // ================= getAllLocationFromSQLite =========================================================
    public String[] getAllLocationFromSQLite() {
        Log.d(Values.TAG_LOG, "SQLiteConnect getAllLocationFromSQLite");
        String[] columns = new String[] { DBHelper.KEY_LOCATION };
        String groupBy = DBHelper.KEY_LOCATION ;
        String orderBy = DBHelper.KEY_LOCATION ;
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_INVENTORY, columns, null, null, groupBy, null, orderBy);
        String[] locations = wrapperLocation(cursor);
        cursor.close();
        return locations;
    }


    // ================= getDevicesFromSQLite =========================================================
    public List<Device> getDevicesFromSQLite(String number) {
        Log.d(Values.TAG_LOG, "SQLiteConnect getDevicesFromSQLite");
        List<Device> devices = new ArrayList<Device>();
        Device device = null;
        String[] selectArgs = new String[]{number};
        String select = DBHelper.KEY_NUMBER + " = ? ";
        //Device devices = new Device();
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_INVENTORY, null, select, selectArgs, null, null, null);
        devices = wrapperDevices(cursor);
        cursor.close();
        return devices;
    }

    // ================= getNoSyncItemsFromSQLite ==================================================
    public List<Device> getNoSyncItemsFromSQLite() {
        Log.d(Values.TAG_LOG, "SQLiteConnect getNoSyncItemsFromSQLite");
        String[] selectArgs = new String[]{String.valueOf(Values.STATUS_SYNC_OFFLINE)};
        String select = DBHelper.KEY_STATUS_SYNC + " = ? ";
        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_INVENTORY, null, select, selectArgs, null, null, null);
        return wrapperDevices(cursor);
    }

    public void updateStatusInvent(int id, int statusSync) {
        //UPDATE wp_inventor SET status_sync = "statusSyncOnline", status_invent="ok" where id = id
        String[] whereArgs = new String[]{String.valueOf(id)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_STATUS_SYNC, statusSync);
        contentValues.put(DBHelper.KEY_STATUS_INVENT, Values.STATUS_FINED);
        sqLiteDatabase.update(DBHelper.TABLE_INVENTORY, contentValues, DBHelper.KEY_ID + " = ? ", whereArgs);

    }

    public void updateItem(Device device, int statusSync) {
        //UPDATE wp_inventor SET status_sync = "statusSyncOnline", status_invent="ok" where id = id
        String[] whereArgs = new String[]{String.valueOf(device.getId())};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_STATUS_SYNC, statusSync);
        contentValues.put(DBHelper.KEY_OWNER, device.getOwner());
        contentValues.put(DBHelper.KEY_LOCATION, device.getLocation());
        contentValues.put(DBHelper.KEY_DESCRIPTION, device.getDescription());
        sqLiteDatabase.update(DBHelper.TABLE_INVENTORY, contentValues, DBHelper.KEY_ID + " = ? ", whereArgs);
    }

    private List<Device> wrapperDevices(Cursor cursor) {
        List<Device> devices = new ArrayList<Device>();
        if (cursor.moveToFirst()) {
            for (cursor.isFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                devices.add(
                        new Device(
                                cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NUMBER)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ITEM)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME_WKS)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.KEY_OWNER)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.KEY_LOCATION)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.KEY_STATUS_INVENT)),
                                cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_STATUS_SYNC)),
                                cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION))
                        )
                );
            }
        } else {
            Log.d(Values.TAG_LOG, "row = " + String.valueOf(cursor.getCount()));
        }
        return devices;
    }

    private String[] wrapperLocation(Cursor cursor) {
        String[] locations = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
               Integer i=0;
            for (cursor.isFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                locations[i] = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_LOCATION));
                Log.d(Values.TAG_LOG, "wrapperLocation:  " + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_LOCATION)));
                i++;
            }
        } else {
            Log.d(Values.TAG_LOG, "Location into SQLite= " + String.valueOf(cursor.getCount()));
        }


        return locations;
    }

    private List<User> wrapperUsers(Cursor cursor) {
        List<User> users= new ArrayList<User>();
        if (cursor.moveToFirst()) {
            for (cursor.isFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                users.add(
                        new User(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_USER_NAME)))
                );
            }
        } else {
            Log.d(Values.TAG_LOG, "Users into SQLite= " + String.valueOf(cursor.getCount()));
        }
        return users;
    }

    public int deleteALL() {
               sqLiteDatabase.delete(DBHelper.TABLE_USERS,null, null);
        return sqLiteDatabase.delete(DBHelper.TABLE_INVENTORY, null, null);
    }

}
