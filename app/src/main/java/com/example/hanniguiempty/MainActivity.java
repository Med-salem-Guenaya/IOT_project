package com.example.hanniguiempty;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find buttons by their IDs
        Button buttonFragment1 = findViewById(R.id.buttonFragment1);
        Button buttonFragment2 = findViewById(R.id.buttonFragment2);
        Button buttonFragment3 = findViewById(R.id.buttonFragment3);
        Button buttonFragment4 = findViewById(R.id.buttonFragment4);
        Button buttonFragment5 = findViewById(R.id.buttonFragment5);
        Button buttonFragment6 = findViewById(R.id.buttonFragment6);



        // Set click listeners for each button
        buttonFragment1.setOnClickListener(v -> openFragment(new Fragment1()));
        buttonFragment2.setOnClickListener(v -> openFragment(new Fragment2()));
        buttonFragment3.setOnClickListener(v -> openFragment(new Fragment3()));
        buttonFragment4.setOnClickListener(v -> openFragment(new Fragment4()));
        buttonFragment5.setOnClickListener(v -> openFragment(new Fragment5()));
        buttonFragment6.setOnClickListener(v -> openFragment(new Fragment6()));




    }

    private void openFragment(Fragment fragment) {
        // Replace the existing fragment with the new one
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


}
