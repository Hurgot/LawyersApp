package com.lawyersapp.lawyersearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.lawyersapp.R;

public class LawyerSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lawyer);
        Toolbar toolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LawyerSearchFragment fragment = (LawyerSearchFragment)
                getSupportFragmentManager().findFragmentById(R.id.lawyer_search_container);

        if(fragment == null){
            fragment = LawyerSearchFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.lawyer_search_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
