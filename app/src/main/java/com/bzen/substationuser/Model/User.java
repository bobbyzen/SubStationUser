package com.bzen.substationuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String uid;
    private String id;
    private String jabatan;
    private String wilayah;
    private String email;

    public User(String uid, String id, String jabatan, String wilayah, String email) {
        this.uid = uid;
        this.id = id;
        this.jabatan = jabatan;
        this.wilayah = wilayah;
        this.email = email;
    }

    public User() {
    }

    protected User(Parcel in) {
        uid = in.readString();
        id = in.readString();
        jabatan = in.readString();
        wilayah = in.readString();
        email = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getWilayah() {
        return wilayah;
    }

    public void setWilayah(String wilayah) {
        this.wilayah = wilayah;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(id);
        parcel.writeString(jabatan);
        parcel.writeString(wilayah);
        parcel.writeString(email);
    }
}
