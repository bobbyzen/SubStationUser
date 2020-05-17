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
    private String diajukan_oleh;
    private String diacc;
    private String maintenance_terakhir;

    public Gardu(String id, String longitude, String latitude, String status, String alamat, String tanggal, String wilayah, String jenisGardu, String diajukan_oleh, String diacc, String maintenance_terakhir) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.status = status;
        this.alamat = alamat;
        this.tanggal = tanggal;
        this.wilayah = wilayah;
        this.jenisGardu = jenisGardu;
        this.diajukan_oleh = diajukan_oleh;
        this.diacc = diacc;
        this.maintenance_terakhir = maintenance_terakhir;
    }

    public Gardu() {
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
        diajukan_oleh = in.readString();
        diacc = in.readString();
        maintenance_terakhir = in.readString();
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

    public String getDiajukan_oleh() {
        return diajukan_oleh;
    }

    public void setDiajukan_oleh(String diajukan_oleh) {
        this.diajukan_oleh = diajukan_oleh;
    }

    public String getDiacc() {
        return diacc;
    }

    public void setDiacc(String diacc) {
        this.diacc = diacc;
    }

    public String getMaintenance_terakhir() {
        return maintenance_terakhir;
    }

    public void setMaintenance_terakhir(String maintenance_terakhir) {
        this.maintenance_terakhir = maintenance_terakhir;
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
        parcel.writeString(diajukan_oleh);
        parcel.writeString(diacc);
        parcel.writeString(maintenance_terakhir);
    }
}
