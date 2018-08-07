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
import com.example.varenik.glinvent2.model.Values;

public class MainActivity extends AppCompatActivity implements
                        ManageFragment.OnFragmentInteractionListener,
                        ScanFragment.OnFragmentInteractionListener {

    private TextView mTextMessage;
    /**
     * fragments
     */
   private ManageFragment  manageFragment;
   private ScanFragment scanFragment;

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
        initFtagment();
    }


    private void initFtagment() {
        manageFragment = new ManageFragment();
        scanFragment = new ScanFragment();



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
                    mTextMessage.setText(R.string.title_sync);
                    return true;
                case R.id.navigation_user:

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

    @Override
    public void saveURLHost(String url) {
        Log.d(Values.TAG_LOG, "Main Activity: run saveUrlHost ||  " + url);
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sharedPreferences.edit();
        spEdit.putString("URL", url);
        spEdit.commit();
    }

    @Override
    public void loadURLHost() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        Values.host = sharedPreferences.getString("URL", "");
        Values.concatUrl(Values.host);
    }
}
