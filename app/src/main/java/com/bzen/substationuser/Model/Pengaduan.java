package com.bzen.substationuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pengaduan implements Parcelable {
    private String gardu_id;
    private String id;
    private String laporan;
    private String status_baru;
    private String status_lama;
    private String tanggal_baru;
    private String tanggal_lama;
    private String user_id;

    public Pengaduan(String gardu_id, String id, String laporan, String status_baru, String status_lama, String tanggal_baru, String tanggal_lama, String user_id) {
        this.gardu_id = gardu_id;
        this.id = id;
        this.laporan = laporan;
        this.status_baru = status_baru;
        this.status_lama = status_lama;
        this.tanggal_baru = tanggal_baru;
        this.tanggal_lama = tanggal_lama;
        this.user_id = user_id;
    }

    public Pengaduan() {
    }

    protected Pengaduan(Parcel in) {
        gardu_id = in.readString();
        id = in.readString();
        laporan = in.readString();
        status_baru = in.readString();
        status_lama = in.readString();
        tanggal_baru = in.readString();
        tanggal_lama = in.readString();
        user_id = in.readString();
    }

    public static final Creator<Pengaduan> CREATOR = new Creator<Pengaduan>() {
        @Override
        public Pengaduan createFromParcel(Parcel in) {
            return new Pengaduan(in);
        }

        @Override
        public Pengaduan[] newArray(int size) {
            return new Pengaduan[size];
        }
    };

    public String getGardu_id() {
        return gardu_id;
    }

    public void setGardu_id(String gardu_id) {
        this.gardu_id = gardu_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLaporan() {
        return laporan;
    }

    public void setLaporan(String laporan) {
        this.laporan = laporan;
    }

    public String getStatus_baru() {
        return status_baru;
    }

    public void setStatus_baru(String status_baru) {
        this.status_baru = status_baru;
    }

    public String getStatus_lama() {
        return status_lama;
    }

    public void setStatus_lama(String status_lama) {
        this.status_lama = status_lama;
    }

    public String getTanggal_baru() {
        return tanggal_baru;
    }

    public void setTanggal_baru(String tanggal_baru) {
        this.tanggal_baru = tanggal_baru;
    }

    public String getTanggal_lama() {
        return tanggal_lama;
    }

    public void setTanggal_lama(String tanggal_lama) {
        this.tanggal_lama = tanggal_lama;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(gardu_id);
        parcel.writeString(id);
        parcel.writeString(laporan);
        parcel.writeString(status_baru);
        parcel.writeString(status_lama);
        parcel.writeString(tanggal_baru);
        parcel.writeString(tanggal_lama);
        parcel.writeString(user_id);
    }
}
