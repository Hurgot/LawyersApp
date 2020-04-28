package com.lawyersapp.lawyers;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.lawyersapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lawyersapp.addeditlawyer.AddEditLawyerActivity;
import com.lawyersapp.data.LawyersDbHelper;
import com.lawyersapp.data.LawyersContract.LawyerEntry;
import com.lawyersapp.lawyerdetail.LawyerDetailActivity;
import com.lawyersapp.lawyersearch.LawyerSearchActivity;

/**
 * Vista para la lista de abogados del gabinete
 */
public class LawyersFragment extends Fragment {
    public static final int REQUEST_UPDATE_DELETE_LAWYER = 2;

    private LawyersDbHelper mLawyersDbHelper;

    private ListView mLawyersList;
    private LawyersCursorAdapter mLawyersAdapter;
    private FloatingActionButton mAddButton;


    public LawyersFragment() {
        // Required empty public constructor
    }

    public static LawyersFragment newInstance() {
        return new LawyersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lawyers, container, false);

        // Referencias UI
        mLawyersList = (ListView) root.findViewById(R.id.lawyers_list);
        mLawyersAdapter = new LawyersCursorAdapter(getActivity(), null);
        mAddButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        // Setup
        mLawyersList.setEmptyView(root.findViewById(R.id.empty));
        mLawyersList.setAdapter(mLawyersAdapter);

        // Eventos
        mLawyersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor currentItem = (Cursor) mLawyersAdapter.getItem(position);
                String currentLawyerId = currentItem.getString(
                        currentItem.getColumnIndex(LawyerEntry.ID));

                showDetailScreen(currentLawyerId);
            }
        });
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddScreen();
            }
        });


        // Instancia de helper
        mLawyersDbHelper = new LawyersDbHelper(getActivity());

        // Carga de datos
        loadLawyers();

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Activity.RESULT_OK == resultCode){
            if(requestCode == AddEditLawyerActivity.REQUEST_ADD_LAWYER){
                showSuccessfullSavedMessage();
            }
        }

        loadLawyers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_search){
            showSearchScreen();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSearchScreen(){
        Intent intent = new Intent(getActivity(), LawyerSearchActivity.class);
        startActivity(intent);
    }

    private void loadLawyers() {
        new LawyersLoadTask().execute();
    }

    private void showSuccessfullSavedMessage() {
        Toast.makeText(getActivity(),
                "Abogado guardado correctamente", Toast.LENGTH_SHORT).show();
    }

    private void showAddScreen() {
        Intent intent = new Intent(getActivity(), AddEditLawyerActivity.class);
        startActivityForResult(intent, AddEditLawyerActivity.REQUEST_ADD_LAWYER);
    }

    private void showDetailScreen(String lawyerId) {
        Intent intent = new Intent(getActivity(), LawyerDetailActivity.class);
        intent.putExtra(LawyersActivity.EXTRA_LAWYER_ID, lawyerId);
        startActivityForResult(intent, REQUEST_UPDATE_DELETE_LAWYER);
    }

    private class LawyersLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mLawyersDbHelper.getAllLawyers();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            mLawyersAdapter.swapCursor(cursor);
        }
    }

}
