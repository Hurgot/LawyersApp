package com.lawyersapp.lawyers;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.lawyersapp.R;

public class LawyersActivity extends AppCompatActivity {

    public static final String EXTRA_LAWYER_ID = "extra_lawyer_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LawyersFragment fragment = (LawyersFragment)
                getSupportFragmentManager().findFragmentById(R.id.lawyers_container);

        if (fragment == null) {
            fragment = LawyersFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.lawyers_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lawyers, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
