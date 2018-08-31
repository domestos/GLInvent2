package com.example.varenik.glinvent2.model;

import android.app.Application;

public class Values extends Application{

    public static String host ="";
    public static String server_url = "http://"+ host +"/PHPScript/db_get_all.php";
    public static String update_status_invent_url ="http://"+ host +"/PHPScript/db_update_status.php";
    public static String update_item="http://"+ host +"/PHPScript/db_update.php";
    public static String get_all_users="http://"+ host +"/PHPScript/db_get_all_users.php";


    public static String  TAG_LOG = "TAG_LOG";
    public static final int STATUS_SYNC_ONLINE = 0;
    public static final int STATUS_SYNC_OFFLINE = 1;
    public static final String STATUS_FINED= "ok";
    public static String lastSyncDate = "none";
    public static boolean sw_inventory_off_on = false;

    public static void concatUrl(String url_host) {
        server_url = "http://"+url_host+"/PHPScript/db_get_all.php";
        update_status_invent_url ="http://"+url_host+"/PHPScript/db_update_status.php";
        update_item="http://"+url_host+"/PHPScript/db_update.php";
        get_all_users="http://"+url_host+"/PHPScript/db_get_all_users.php";
    }
}
