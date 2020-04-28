package com.lawyersapp.lawyerdetail;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lawyersapp.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.lawyersapp.addeditlawyer.AddEditLawyerActivity;
import com.lawyersapp.data.Lawyer;
import com.lawyersapp.data.LawyersContract;
import com.lawyersapp.data.LawyersDbHelper;
import com.lawyersapp.lawyers.LawyersActivity;
import com.lawyersapp.lawyers.LawyersFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LawyerDetailFragment extends Fragment {

    private static final String ARG_LAWYER_ID = "arg_lawyer_id";

    private String mLawyerId;

    private CollapsingToolbarLayout mCollapsingView;
    private ImageView mAvatar;
    private TextView mPhoneNumber;
    private TextView mSpecialty;
    private TextView mBio;

    private LawyersDbHelper mLawyerDbHelper;

    public LawyerDetailFragment() {
        // Required empty public constructor
    }

    public static LawyerDetailFragment newInstance(String lawyerId) {
        LawyerDetailFragment fragment = new LawyerDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LAWYER_ID, lawyerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mLawyerId = getArguments().getString(ARG_LAWYER_ID);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                showEditScreen();
                break;
            case R.id.action_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Alerta!");
                builder.setMessage("¿Está seguro que desea eliminar este contacto?");
                builder.setCancelable(true);
                builder.setNeutralButton("ELIMINAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteLawyerTask().execute();
                    }
                });
                builder.setNegativeButton("CANCELAR", null);
                builder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEditScreen() {
        Intent intent = new Intent(getActivity(), AddEditLawyerActivity.class);
        intent.putExtra(LawyersActivity.EXTRA_LAWYER_ID, mLawyerId);
        startActivityForResult(intent, LawyersFragment.REQUEST_UPDATE_DELETE_LAWYER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_lawyer_detail, container, false);
        mCollapsingView = getActivity().findViewById(R.id.toolbar_layout);
        mAvatar = getActivity().findViewById(R.id.iv_avatar);
        mPhoneNumber = root.findViewById(R.id.tv_phone_number);
        mSpecialty = root.findViewById(R.id.tv_specialty);
        mBio = root.findViewById(R.id.tv_bio);

        mLawyerDbHelper = new LawyersDbHelper(getActivity());

        loadLawyer();

        return root;
    }

    private void loadLawyer() {
        new GetLawyerByIdTask().execute();
    }

    private void showLawyer(Lawyer lawyer) {
        mCollapsingView.setTitle(lawyer.getName());
        Glide.with(this)
                .load(Uri.parse(lawyer.getAvatar()))
                .error(R.drawable.ic_account_circle)
                .centerCrop()
                .into(mAvatar);

        mPhoneNumber.setText(lawyer.getPhoneNumber());
        mSpecialty.setText(lawyer.getSpecialty());
        mBio.setText(lawyer.getBio());
    }

    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error al cargar información", Toast.LENGTH_SHORT).show();
    }

    private void showLawyerScreen(boolean requery) {
        if(!requery){
            showDeleteError();
        }

        getActivity().setResult(requery ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    private void showDeleteError() {
        Toast.makeText(getActivity(),
                "Error al eliminar.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Activity.RESULT_OK == resultCode){
            loadLawyer();
        }
    }

    private class GetLawyerByIdTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mLawyerDbHelper.getLawyerById(mLawyerId);
        }

        @Override
        protected void onPostExecute(Cursor cursor){
            if(cursor != null && cursor.moveToLast()){
                showLawyer(new Lawyer(cursor));
            }else{
                showLoadError();
            }
        }
    }

    private class DeleteLawyerTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return mLawyerDbHelper.deleteLawyer(mLawyerId);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            showLawyerScreen(integer > 0);
        }


    }

}
