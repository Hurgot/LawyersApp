package com.lawyersapp.addeditlawyer;


import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.lawyersapp.R;
import com.lawyersapp.lawyers.LawyersActivity;

public class AddEditLawyerActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_LAWYER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_lawyer);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String lawyerId = getIntent().getStringExtra(LawyersActivity.EXTRA_LAWYER_ID);

        setTitle(lawyerId == null ? "Añadir abogado" : "Editar abogado");

        AddEditLawyerFragment addEditLawyerFragment = (AddEditLawyerFragment)
                getSupportFragmentManager().findFragmentById(R.id.add_edit_lawyer_container);
        if (addEditLawyerFragment == null) {
            addEditLawyerFragment = AddEditLawyerFragment.newInstance(lawyerId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.add_edit_lawyer_container, addEditLawyerFragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_lawyer, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
