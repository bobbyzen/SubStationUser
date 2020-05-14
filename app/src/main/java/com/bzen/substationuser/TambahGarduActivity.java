package com.bzen.substationuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bzen.substationuser.Model.Gardu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TambahGarduActivity extends AppCompatActivity implements LocationListener {

    DatabaseReference rootRef;
    FirebaseAuth mAuth;

    LocationManager locationManager;
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    ImageButton btnSearchLocation;
    EditText etAddress;
    HashMap<String, Gardu> listGardu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_gardu);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        btnSearchLocation = findViewById(R.id.btnSearch);
        etAddress = findViewById(R.id.etAddress);
        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        grantPermission();
        checkLocationIsEnableOrNot();
        getLocation();
    }


    private void getLocation() {
        try{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500,5, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void checkLocationIsEnableOrNot() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try{
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!gpsEnabled && !networkEnabled){
            new AlertDialog.Builder(TambahGarduActivity.this)
                    .setTitle("Enable GPS Service")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void grantPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            final List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            System.out.println("BETUL" + addresses.get(0).getAddressLine(0));
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    map = googleMap;
                    LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                    MarkerOptions options = new MarkerOptions().position(latLng).title("Iam here").icon(BitmapDescriptorFactory.fromResource(R.drawable.signs));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    map.addMarker(options);

                    rootRef.child("PengajuanGardu").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            listGardu = new HashMap<>();
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                Gardu gardu = child.getValue(Gardu.class);
                                listGardu.put(gardu.getId(), gardu);
                            }

                            for (Map.Entry<String, Gardu> entry : listGardu.entrySet()) {
                                String key = entry.getKey();
                                Gardu gardu = entry.getValue();
                                double longitude = Double.parseDouble(gardu.getLongitude());
                                double latitude = Double.parseDouble(gardu.getLatitude());
//                                LatLng set = ;
//                                Toast.makeText(TambahGarduActivity.this, set.longitude +"j", Toast.LENGTH_SHORT).show();
                                if(gardu.getStatus().equals("0")){
                                    map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(gardu.getId()).icon(BitmapDescriptorFactory.fromResource(R.drawable.gardu_aman)));
                                }
                                else if(gardu.getStatus().equals("1")){
                                    map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(gardu.getId()).icon(BitmapDescriptorFactory.fromResource(R.drawable.gardu_maintenance)));
                                }
                                else if(gardu.getStatus().equals("2")){
                                    map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(gardu.getId()).icon(BitmapDescriptorFactory.fromResource(R.drawable.gardu_mati)));
                                }
                                //map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(gardu.getId()).icon(BitmapDescriptorFactory.fromResource(R.drawable.gardu_mati)));

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(ChangeLongLatToAddress(latLng));
                            map.addMarker(markerOptions);
                        }
                    });

                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(final Marker marker) {
                            rootRef.child("PengajuanGardu").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    listGardu = new HashMap<>();
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        Gardu gardu = child.getValue(Gardu.class);
                                        listGardu.put(gardu.getId(), gardu);
                                    }

                                    if(!listGardu.containsKey(marker.getTitle())){
                                        //Tanya Mau tambah atau hapus
                                        final AlertDialog alertDialog = new AlertDialog.Builder(TambahGarduActivity.this).create();
                                        alertDialog.setTitle("Tambah gardu sesuai lokasi ?");
                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                LayoutInflater inflater = getLayoutInflater();
                                                View dialogView = inflater.inflate(R.layout.alert_dialog_tambah_gardu, null);
                                                final Spinner spinner = dialogView.findViewById(R.id.spinnerJenisGardu);
                                                final Spinner spinner1 = dialogView.findViewById(R.id.spinnerJenisWilayah);
                                                final Spinner spinner2 = dialogView.findViewById(R.id.spinnerJenisStatus);
                                                final TextView tvTanggal = dialogView.findViewById(R.id.tvTanggal);

                                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TambahGarduActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jenis));
                                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinner.setAdapter(arrayAdapter);

                                                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(TambahGarduActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jenis_wilayah));
                                                arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinner1.setAdapter(arrayAdapter1);

                                                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(TambahGarduActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jenis_maintenance));
                                                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinner2.setAdapter(arrayAdapter2);

                                                //tanggal otomatis
                                                final DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                                                final Date date = new Date();
                                                tvTanggal.setText(dateFormat.format(date));


                                                final AlertDialog dialog = new AlertDialog.Builder(TambahGarduActivity.this).create();
                                                dialog.setView(dialogView);
                                                dialog.setCancelable(true);
                                                dialog.setIcon(R.drawable.gardu_fix);
                                                dialog.setTitle("Form Tambah Gardu");
                                                dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
                                                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ya", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        rootRef.child("PengajuanGardu").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                int counter = 1;
                                                                String jenisGardu = spinner.getSelectedItem().toString().substring(spinner.getSelectedItem().toString().length()-3, spinner.getSelectedItem().toString().length()-1);
                                                                String jenisWilayah = spinner1.getSelectedItem().toString().substring(spinner1.getSelectedItem().toString().length()-4, spinner1.getSelectedItem().toString().length()-1);
                                                                for(DataSnapshot child : dataSnapshot.getChildren()){
                                                                    Gardu gardu = child.getValue(Gardu.class);
                                                                    if(gardu.getId().substring(0,5).equals(jenisGardu+jenisWilayah)){
                                                                        counter++;
                                                                    }
                                                                }

                                                                String [] addlonglat = marker.getTitle().split(",");

                                                                Gardu gardu = new Gardu();
                                                                String convertCounter = ConvertCounter(counter);
                                                                gardu.setId(jenisGardu+jenisWilayah+convertCounter);
                                                                gardu.setLongitude(String.valueOf(marker.getPosition().longitude));
                                                                gardu.setLatitude(String.valueOf(marker.getPosition().latitude));
                                                                gardu.setAlamat(marker.getTitle().substring(14));
                                                                gardu.setJenisGardu(String.valueOf(spinner.getSelectedItemId()));
                                                                gardu.setStatus(String.valueOf(spinner2.getSelectedItemId()));
                                                                gardu.setTanggal(dateFormat.format(date));
                                                                gardu.setWilayah(String.valueOf(spinner1.getSelectedItemId()));

                                                                //TambahGardu
                                                                HashMap<String,String> data = new HashMap<>();
                                                                data.put("id", gardu.getId());
                                                                data.put("longitude", String.valueOf(gardu.getLongitude()));
                                                                data.put("latitude", String.valueOf(gardu.getLatitude()));
                                                                data.put("alamat", gardu.getAlamat());
                                                                data.put("jenis_gardu", gardu.getJenisGardu());
                                                                data.put("status", gardu.getStatus());
                                                                data.put("tanggal", gardu.getTanggal());
                                                                data.put("wilayah",gardu.getWilayah());
                                                                rootRef.child("PengajuanGardu").child(gardu.getId()).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        marker.remove();
                                                                        Toast.makeText(TambahGarduActivity.this, "Data disimpan", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                });
                                                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tidak", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialog.cancel();
                                                    }
                                                });
                                                dialog.show();

                                            }
                                        });
                                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Hapus", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                marker.remove();
                                            }
                                        });
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                        alertDialog.setCancelable(false);
                                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                            @Override
                                            public void onShow(DialogInterface dialog) {
//                                                Button positiveButton = ((AlertDialog) dialog)
//                                                        .getButton(AlertDialog.BUTTON_POSITIVE);
//                                                positiveButton.setBackground(getResources().getDrawable(R.drawable.dialog_bg_positive));
//
//                                                Button negativeButton = ((AlertDialog) dialog)
//                                                        .getButton(AlertDialog.BUTTON_NEGATIVE);
//                                                negativeButton.setBackground(getResources().getDrawable(R.drawable.dialog_bg_negative));
//
//                                                Button neutralButton = ((AlertDialog) dialog)
//                                                        .getButton(AlertDialog.BUTTON_NEUTRAL);
//                                                neutralButton.setBackground(getResources().getDrawable(R.drawable.dialog_bg_cancel));
                                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.red));
                                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.green));
                                                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.grey));
                                            }
                                        });
                                        alertDialog.show();
                                    }
                                    else{
                                        Toast.makeText(TambahGarduActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            return true;
                        }
                    });

                    btnSearchLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String location = etAddress.getText().toString();
                            List<Address> addressList = null;

                            if (!TextUtils.isEmpty(location)) {
                                Geocoder geocoder = new Geocoder(TambahGarduActivity.this);
                                try {
                                    addressList = geocoder.getFromLocationName(location, 1);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Address address = addressList.get(0);
                                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                                googleMap.addMarker(new MarkerOptions().position(latLng).title(location));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(TambahGarduActivity.this, "Lokasi belum diisi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String ConvertCounter(int counter) {
        String hasil = "";
        if(counter < 9){
            hasil = "0000" + counter;
        }
        else if(counter < 99){
            hasil = "000" + counter;
        }
        else if(counter < 999){
            hasil = "00" + counter;
        }
        else if(counter < 9999){
            hasil = "0" + counter;
        }
        else{
            hasil = String.valueOf(counter);
        }
        return hasil;
    }

    private String ChangeLongLatToAddress(LatLng latLng){
        List<Address> addressList = null;

        if (latLng != null) {
            Geocoder geocoder = new Geocoder(TambahGarduActivity.this);
            try {
                addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList == null){
                return ChangeLongLatToAddress(latLng);
            }
            else{
                Address address = addressList.get(0);
                String add = "";
                for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    add += address.getAddressLine(i);
                }
                return add;
            }
        }
        else{
            return null;
        }
    }

    private LatLng ChangeAddressToLongLang(String location) {
        List<Address> addressList = null;

        if (!TextUtils.isEmpty(location)) {
            Geocoder geocoder = new Geocoder(TambahGarduActivity.this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList == null){
                return ChangeAddressToLongLang(location);
            }
            else{
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                return latLng;
            }
        }
        else{
            return null;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}