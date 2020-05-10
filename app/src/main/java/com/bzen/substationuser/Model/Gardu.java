package com.bzen.substationuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Gardu implements Parcelable {
    private String id;
    private String longitude;
    private String latitude;
    private String status;
    private String alamat;
    private String tanggal;
    private String wilayah;
    private String jenisGardu;

    public Gardu() {
    }

    public Gardu(String id, String longitude, String latitude, String status, String alamat, String tanggal, String wilayah, String jenisGardu) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.alamat = alamat;
        this.tanggal = tanggal;
        this.wilayah = wilayah;
        this.jenisGardu = jenisGardu;
    }

    protected Gardu(Parcel in) {
        id = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        status = in.readString();
        alamat = in.readString();
        tanggal = in.readString();
        wilayah = in.readString();
        jenisGardu = in.readString();
    }

    public static final Creator<Gardu> CREATOR = new Creator<Gardu>() {
        @Override
        public Gardu createFromParcel(Parcel in) {
            return new Gardu(in);
        }

        @Override
        public Gardu[] newArray(int size) {
            return new Gardu[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWilayah() {
        return wilayah;
    }

    public void setWilayah(String wilayah) {
        this.wilayah = wilayah;
    }

    public String getJenisGardu() {
        return jenisGardu;
    }

    public void setJenisGardu(String jenisGardu) {
        this.jenisGardu = jenisGardu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(longitude);
        parcel.writeString(latitude);
        parcel.writeString(status);
        parcel.writeString(alamat);
        parcel.writeString(tanggal);
        parcel.writeString(wilayah);
        parcel.writeString(jenisGardu);
    }
}
