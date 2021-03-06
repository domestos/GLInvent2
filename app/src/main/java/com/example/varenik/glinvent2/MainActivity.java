package com.example.varenik.glinvent2;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.varenik.glinvent2.fragments.manage.ManageFragment;
import com.example.varenik.glinvent2.fragments.scan.ScanFragment;
import com.example.varenik.glinvent2.fragments.sync.SyncFragment;
import com.example.varenik.glinvent2.fragments.user.UserFragment;
import com.example.varenik.glinvent2.model.Values;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements
                        ScanFragment.OnFragmentInteractionListener,
                        UserFragment.OnScanFragmentInteractionListener{

    private TextView mTextMessage;
    /**
     * fragments
     */
   private ManageFragment  manageFragment;
   private ScanFragment scanFragment;
   private SyncFragment syncFragment;
   private UserFragment userFragment;
   private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        /**
         * init all Fragment
         */
        loadURLHost();
        initFtagment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, scanFragment).commit();
    }

    private void initFtagment() {
        manageFragment = new ManageFragment();
        scanFragment = new ScanFragment();
        syncFragment = new SyncFragment();
        userFragment = new UserFragment();

    }

    public void loadURLHost() {
        sharedPreferences = this.getPreferences(MODE_PRIVATE);
        Values.host = sharedPreferences.getString("URL", "");
        Values.concatUrl(Values.host);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_scan:
                    fragmentTransaction.replace(R.id.container, scanFragment).commit();
                    return true;
                case R.id.navigation_sync:
                    fragmentTransaction.replace(R.id.container, syncFragment).commit();
                    return true;
                case R.id.navigation_user:
                    fragmentTransaction.replace(R.id.container, userFragment).commit();
                    return true;
                case R.id.navigation_manage:
                    fragmentTransaction.replace(R.id.container, manageFragment).commit();
                    return true;
            }
            return false;
        }
    };

    /**
     * Init Methods of Manager Fragment
     *
     */

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
