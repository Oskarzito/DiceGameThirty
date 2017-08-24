package com.bignerdranch.android.dicegamethirty;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
* Created by Oskar Emilsson
*/

//Creates a GameFragment and puts it on screen
public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new GameFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
