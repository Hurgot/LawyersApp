package com.lawyersapp.data;

import com.lawyersapp.data.LawyersContract.LawyerEntry;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LawyersDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Lawyers.db";

    public LawyersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + LawyerEntry.TABLE_NAME + " ("
                + LawyerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LawyerEntry.ID + " TEXT NOT NULL,"
                + LawyerEntry.NAME + " TEXT NOT NULL,"
                + LawyerEntry.SPECIALTY + " TEXT NOT NULL,"
                + LawyerEntry.PHONE_NUMBER + " TEXT NOT NULL,"
                + LawyerEntry.BIO + " TEXT NOT NULL,"
                + LawyerEntry.AVATAR + " TEXT,"
                + "UNIQUE (" + LawyerEntry.ID + "))");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long saveLawyer(Lawyer lawyer) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                LawyerEntry.TABLE_NAME,
                null,
                lawyer.toContentValues());

    }

    public Cursor getAllLawyers() {
        return getReadableDatabase()
                .query(
                        LawyerEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    public Cursor getLawyerById(String lawyerId) {
        Cursor c = getReadableDatabase().query(
                LawyerEntry.TABLE_NAME,
                null,
                LawyerEntry.ID + " = ?",
                new String[]{lawyerId},
                null,
                null,
                null);
        return c;
    }

    public Cursor searchLawyer(String search) {
        return getReadableDatabase().query(
                LawyerEntry.TABLE_NAME,
                null,
                LawyerEntry.NAME + " LIKE ?",
                new String[]{"%"+search+"%"},
                null,
                null,
                null
        );
    }

    public int deleteLawyer(String lawyerId) {
        return getWritableDatabase().delete(
                LawyerEntry.TABLE_NAME,
                LawyerEntry.ID + " = ?",
                new String[]{lawyerId});
    }

    public int updateLawyer(Lawyer lawyer, String lawyerId) {
        return getWritableDatabase().update(
                LawyerEntry.TABLE_NAME,
                lawyer.toContentValues2(),
                LawyerEntry.ID + " = ?",
                new String[]{lawyerId}
        );
    }
}
