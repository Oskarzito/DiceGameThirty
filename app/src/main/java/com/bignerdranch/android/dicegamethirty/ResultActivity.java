package com.bignerdranch.android.dicegamethirty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Oskar Emilsson
 */

public class ResultActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_result_container);

        if (fragment == null) {
            fragment = new ResultFragment();
            fm.beginTransaction().add(R.id.fragment_result_container, fragment).commit();
        }
    }
}
