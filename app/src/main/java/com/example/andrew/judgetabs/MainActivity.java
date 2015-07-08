package com.example.andrew.judgetabs;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {

    private TabHost mTabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        mTabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;


        //Catagories tab
        intent = new Intent(this, CatsFragment.class);

        spec = mTabHost.newTabSpec("catagories")
                .setIndicator("Catagories")
                .setContent(intent);
        mTabHost.addTab(spec);

        //Teams tab
        intent = new Intent(this, TeamsFragment.class);
        spec = mTabHost.newTabSpec("teams")
                .setIndicator("Teams")
                .setContent(intent);
        mTabHost.addTab(spec);

        //Map Tab
        intent = new Intent(this, Map.class);
        spec = mTabHost.newTabSpec("map")
                .setIndicator("Map")
                .setContent(intent);
        mTabHost.addTab(spec);

        //Menu Tab
        intent = new Intent(this, Menu.class);
        spec = mTabHost.newTabSpec("menu")
                .setIndicator("Menu")
                .setContent(intent);
        mTabHost.addTab(spec);
    }

}