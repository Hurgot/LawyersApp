package com.lawyersapp.lawyersearch;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lawyersapp.R;
import com.lawyersapp.addeditlawyer.AddEditLawyerActivity;
import com.lawyersapp.data.LawyersContract;
import com.lawyersapp.data.LawyersDbHelper;
import com.lawyersapp.lawyerdetail.LawyerDetailActivity;
import com.lawyersapp.lawyers.LawyersActivity;
import com.lawyersapp.lawyers.LawyersCursorAdapter;
import com.lawyersapp.lawyers.LawyersFragment;

public class LawyerSearchFragment extends Fragment {


    private LawyersDbHelper mLawyersDbHelper;

    private ListView mLawyersList;
    private LawyersCursorAdapter mLawyersAdapter;
    private EditText mFieldSearch;

    private String search = "";

    public LawyerSearchFragment() {
        // Required empty public constructor
    }

    public static LawyerSearchFragment newInstance() {
        LawyerSearchFragment fragment = new LawyerSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lawyer_search, container, false);
        mLawyersList = (ListView) root.findViewById(R.id.lawyers_search_list);
        mLawyersAdapter = new LawyersCursorAdapter(getActivity(), null);

        mFieldSearch = getActivity().findViewById(R.id.field_search);

        mLawyersList.setEmptyView(root.findViewById(R.id.empty));
        mLawyersList.setAdapter(mLawyersAdapter);

        mFieldSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search = mFieldSearch.getText().toString();
                loadLawyers();
            }
        });

        mLawyersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor currentItem = (Cursor) mLawyersAdapter.getItem(position);
                String currentLawyerId = currentItem.getString(
                        currentItem.getColumnIndex(LawyersContract.LawyerEntry.ID));

                showDetailScreen(currentLawyerId);
            }
        });

        mLawyersDbHelper = new LawyersDbHelper(getActivity());

        loadLawyers();

        return root;
    }

    private void showDetailScreen(String lawyerId) {
        Intent intent = new Intent(getActivity(), LawyerDetailActivity.class);
        intent.putExtra(LawyersActivity.EXTRA_LAWYER_ID, lawyerId);
        startActivityForResult(intent, LawyersFragment.REQUEST_UPDATE_DELETE_LAWYER);
    }



    private void loadLawyers() {
        new LawyersLoadTask().execute();
    }


    private class LawyersLoadTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mLawyersDbHelper.searchLawyer(search);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {

            } else {
                // Mostrar empty state
            }
            mLawyersAdapter.swapCursor(cursor);
        }
    }

}
