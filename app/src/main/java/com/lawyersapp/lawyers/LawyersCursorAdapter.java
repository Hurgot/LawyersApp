package com.lawyersapp.lawyers;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lawyersapp.R;
import com.lawyersapp.data.LawyersContract.LawyerEntry;

public class LawyersCursorAdapter extends CursorAdapter {
    public LawyersCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_lawyer, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView nameText = view.findViewById(R.id.tv_name);
        final ImageView avatarImage = view.findViewById(R.id.iv_avatar);

        String name = cursor.getString(cursor.getColumnIndex(LawyerEntry.NAME));
        String avatarUri = cursor.getString(cursor.getColumnIndex(LawyerEntry.AVATAR));

        nameText.setText(name);
        Glide.with(context)
                .load(Uri.parse(avatarUri))
                .error(R.drawable.ic_account_circle)
                .centerCrop()
                .into(avatarImage);

    }

}
