package com.lawyersapp.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.lawyersapp.data.LawyersContract.LawyerEntry;

import java.util.UUID;

public class Lawyer {
    private String id;
    private String name;
    private String specialty;
    private String phoneNumber;
    private String bio;
    private String avatar;

    public Lawyer(String name,
                  String specialty, String phoneNumber,
                  String bio, String avatar) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.avatar = avatar;
    }

    public Lawyer(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(LawyersContract.LawyerEntry.ID));
        name = cursor.getString(cursor.getColumnIndex(LawyerEntry.NAME));
        specialty = cursor.getString(cursor.getColumnIndex(LawyerEntry.SPECIALTY));
        phoneNumber = cursor.getString(cursor.getColumnIndex(LawyerEntry.PHONE_NUMBER));
        bio = cursor.getString(cursor.getColumnIndex(LawyerEntry.BIO));
        avatar = cursor.getString(cursor.getColumnIndex(LawyerEntry.AVATAR));
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(LawyerEntry.ID, id);
        values.put(LawyerEntry.NAME, name);
        values.put(LawyerEntry.SPECIALTY, specialty);
        values.put(LawyerEntry.PHONE_NUMBER, phoneNumber);
        values.put(LawyerEntry.BIO, bio);
        values.put(LawyerEntry.AVATAR, avatar);
        return values;
    }
    public ContentValues toContentValues2() {
        ContentValues values = new ContentValues();
        values.put(LawyerEntry.NAME, name);
        values.put(LawyerEntry.SPECIALTY, specialty);
        values.put(LawyerEntry.PHONE_NUMBER, phoneNumber);
        values.put(LawyerEntry.BIO, bio);
        values.put(LawyerEntry.AVATAR, avatar);
        return values;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatar(){ return avatar; }

}
