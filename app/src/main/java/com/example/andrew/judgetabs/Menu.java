package com.example.andrew.judgetabs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Andrew on 7/2/2015.
 */
public class Menu extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        textView.setText("Menu");
        setContentView(R.layout.fragment_menu);
    }
}
