package com.lawyersapp.addeditlawyer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.lawyersapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lawyersapp.data.Lawyer;
import com.lawyersapp.data.LawyersDbHelper;

/**
 * Vista para creación/edición de un abogado
 */
public class AddEditLawyerFragment extends Fragment {
    private static final String ARG_LAWYER_ID = "arg_lawyer_id";

    private static final int RESULT_LOAD_IMAGE = 4;

    private String mLawyerId;

    private LawyersDbHelper mLawyersDbHelper;

    private FloatingActionButton mSaveButton;

    private ImageView avatar_view;
    private TextInputEditText mNameField;
    private TextInputEditText mPhoneNumberField;
    private TextInputEditText mSpecialtyField;
    private TextInputEditText mBioField;
    private TextInputLayout mNameLabel;
    private TextInputLayout mPhoneNumberLabel;
    private TextInputLayout mSpecialtyLabel;
    private TextInputLayout mBioLabel;

    private String avatarUri = "";

    public AddEditLawyerFragment() {
        // Required empty public constructor
    }

    public static AddEditLawyerFragment newInstance(String lawyerId) {
        AddEditLawyerFragment fragment = new AddEditLawyerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LAWYER_ID, lawyerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mLawyerId = getArguments().getString(ARG_LAWYER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_lawyer, container, false);

        mSaveButton =  getActivity().findViewById(R.id.fab);
        avatar_view = getActivity().findViewById(R.id.iv_avatar);
        mNameField =  root.findViewById(R.id.et_name);
        mPhoneNumberField =  root.findViewById(R.id.et_phone_number);
        mSpecialtyField =  root.findViewById(R.id.et_specialty);
        mBioField =  root.findViewById(R.id.et_bio);
        mNameLabel =  root.findViewById(R.id.til_name);
        mPhoneNumberLabel =  root.findViewById(R.id.til_phone_number);
        mSpecialtyLabel =  root.findViewById(R.id.til_specialty);
        mBioLabel =  root.findViewById(R.id.til_bio);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditLawyer();
            }
        });

        mLawyersDbHelper = new LawyersDbHelper(getActivity());

        if (mLawyerId != null) {
            loadLawyer();
        }else {
            Glide.with(this)
                    .load(R.drawable.ic_account_circle)
                    .into(avatar_view);
        }

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_select_image){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, RESULT_LOAD_IMAGE);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == RESULT_LOAD_IMAGE){
                final Uri imageUri = data.getData();
                Glide.with(this)
                        .load(imageUri)
                        .error(R.drawable.ic_account_circle)
                        .centerCrop()
                        .into(avatar_view);
                 avatarUri = imageUri.toString();
            }
        }
    }

    private void loadLawyer() {
        new GetLawyerByIdTask().execute();
    }

    private void addEditLawyer() {
        boolean error = false;

        String name = mNameField.getText().toString();
        String phoneNumber = mPhoneNumberField.getText().toString();
        String specialty = mSpecialtyField.getText().toString();
        String bio = mBioField.getText().toString();

        if (TextUtils.isEmpty(name)) {
            mNameLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (TextUtils.isEmpty(specialty)) {
            mSpecialtyLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (TextUtils.isEmpty(bio)) {
            mBioLabel.setError(getString(R.string.field_error));
            error = true;
        }

        if (error) {
            return;
        }

        Lawyer lawyer = new Lawyer(name, specialty, phoneNumber, bio, avatarUri);

        new AddEditLawyerTask().execute(lawyer);

    }

    private void showLawyersScreen(Boolean requery) {
        if (!requery) {
            showAddEditError();
            getActivity().setResult(Activity.RESULT_CANCELED);
        } else {
            getActivity().setResult(Activity.RESULT_OK);
        }
        getActivity().finish();
    }

    private void showAddEditError() {
        Toast.makeText(getActivity(),
                "Error al agregar nueva información", Toast.LENGTH_SHORT).show();
    }

    private void showLawyer(Lawyer lawyer) {

        Glide.with(this)
                .load(Uri.parse(lawyer.getAvatar()))
                .error(R.drawable.ic_account_circle)
                .centerCrop()
                .into(avatar_view);
        avatarUri = lawyer.getAvatar();

        mNameField.setText(lawyer.getName());
        mPhoneNumberField.setText(lawyer.getPhoneNumber());
        mSpecialtyField.setText(lawyer.getSpecialty());
        mBioField.setText(lawyer.getBio());
    }

    private void showLoadError() {
        Toast.makeText(getActivity(),
                "Error al editar abogado", Toast.LENGTH_SHORT).show();
    }

    private class GetLawyerByIdTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mLawyersDbHelper.getLawyerById(mLawyerId);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null && cursor.moveToLast()) {
                showLawyer(new Lawyer(cursor));
            } else {
                showLoadError();
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        }

    }

    private class AddEditLawyerTask extends AsyncTask<Lawyer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Lawyer... lawyers) {
            if (mLawyerId != null) {
                return mLawyersDbHelper.updateLawyer(lawyers[0], mLawyerId) > 0;

            } else {
                return mLawyersDbHelper.saveLawyer(lawyers[0]) > 0;
            }

        }

        @Override
        protected void onPostExecute(Boolean result) {
            showLawyersScreen(result);
        }

    }

}
