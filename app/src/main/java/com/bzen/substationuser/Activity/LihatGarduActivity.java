package com.bzen.substationuser.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bzen.substationuser.Helper.StringHelper;
import com.bzen.substationuser.Model.Gardu;
import com.bzen.substationuser.Model.User;
import com.bzen.substationuser.R;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class LihatGarduActivity extends AppCompatActivity implements LocationListener {

    ProgressDialog progressDialog;
    SupportMapFragment supportMapFragment;
    LocationManager locationManager;
    GoogleMap map;
    CheckBox cbVerify;

    DatabaseReference rootRef;
    FirebaseAuth mAuth;
    HashMap<String, Gardu> listGardu;
    User user;

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "SUBSTATION";
    private final static String DATA_KEY = "DATA_KEY";
    private final static String DATA_KEY1 = "DATA_KEY1";

    StringHelper helper = new StringHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_gardu);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        cbVerify = findViewById(R.id.cbVerify);
        cbVerify.setChecked(false);
        grantPermission();
        checkLocationIsEnableOrNot();
        getLocation();

        rootRef.child("Gardu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listGardu = new HashMap<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Gardu gardu = child.getValue(Gardu.class);
                    listGardu.put(gardu.getId(), gardu);
                }

                rootRef.child("User").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        cbVerify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            UpdateGardu(helper.StringToHashmap(mPreferences.getString(DATA_KEY, "")), helper.StringToHashmap(mPreferences.getString(DATA_KEY1, "")));
                            cbVerify.setChecked(false);
                        }
                    }, 5000);
                }
            }
        });
    }

    private void UpdateGardu(HashMap<String, String> data, HashMap<String, String> dataLaporan) {
        rootRef.child("Gardu").child(data.get("id")).setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        DatabaseReference laporRef = rootRef.child("Laporan").push();
        String tmp = laporRef.getKey();
        dataLaporan.remove("id");
        dataLaporan.put("id", tmp);
        laporRef.setValue(dataLaporan);
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
            new AlertDialog.Builder(LihatGarduActivity.this)
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
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        final List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    map = googleMap;
                    LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                    MarkerOptions options = new MarkerOptions().position(latLng).title("I am here").icon(BitmapDescriptorFactory.fromResource(R.drawable.signs));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    map.addMarker(options);
                    rootRef.child("Gardu").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            googleMap.clear();
                            map = googleMap;
                            LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                            MarkerOptions options = new MarkerOptions().position(latLng).title("I am here").icon(BitmapDescriptorFactory.fromResource(R.drawable.signs));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            map.addMarker(options);
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
                                if(gardu.getDiacc().equals("1")){
                                    if(gardu.getStatus().equals("0")){
                                        map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(gardu.getId()).icon(BitmapDescriptorFactory.fromResource(R.drawable.gardu_aman)));
                                    }
                                    else if(gardu.getStatus().equals("1")){
                                        map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(gardu.getId()).icon(BitmapDescriptorFactory.fromResource(R.drawable.gardu_maintenance)));
                                    }
                                    else if(gardu.getStatus().equals("2")){
                                        map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(gardu.getId()).icon(BitmapDescriptorFactory.fromResource(R.drawable.gardu_mati)));
                                    }
                                }
                                else if(gardu.getDiacc().equals("0")){
                                    map.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(gardu.getId()));
                                }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(final Marker marker) {

                            if(!marker.getTitle().equals("I am here")){
                                if(listGardu.get(marker.getTitle()).getDiacc().equals("1")){
                                    if(listGardu.get(marker.getTitle()).getWilayah().equals(user.getWilayah())){
                                        final Gardu garduLama = listGardu.get(marker.getTitle());
                                        final Gardu currentGardu = listGardu.get(marker.getTitle());
                                        final String tanggalLama = currentGardu.getTanggal();
                                        final String statusLama = currentGardu.getStatus();
                                        final AlertDialog alertDialog = new AlertDialog.Builder(LihatGarduActivity.this).create();
                                        LayoutInflater inflater = getLayoutInflater();
                                        View dialogView = inflater.inflate(R.layout.alert_dialog_maintenance_gardu, null);
                                        alertDialog.setView(dialogView);
                                        alertDialog.setCancelable(true);
                                        alertDialog.setIcon(R.drawable.gardu_fix);
                                        alertDialog.setTitle("Form Maintenance Gardu");
                                        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);

                                        TextView tvGardu = dialogView.findViewById(R.id.tvJenisGardu);
                                        final Spinner spinner = dialogView.findViewById(R.id.spinnerJenisStatus);
                                        final Button btnYa = dialogView.findViewById(R.id.btnYa);
                                        final Button btnTidak = dialogView.findViewById(R.id.btnTidak);
                                        final TextView tvTanggal = dialogView.findViewById(R.id.tvTanggal);
                                        final EditText etLapor = dialogView.findViewById(R.id.etLaporan);
                                        TextView tvStatus = dialogView.findViewById(R.id.tvStatus);

                                        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(LihatGarduActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.jenis_maintenance));
                                        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinner.setAdapter(arrayAdapter2);

                                        //tanggal otomatis
                                        final DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                                        final Date date = new Date();
                                        tvTanggal.setText(dateFormat.format(date));

                                        tvGardu.setText("Gardu " + currentGardu.getId());
                                        tvStatus.setText("Status(" + (spinner.getItemAtPosition(Integer.valueOf(currentGardu.getStatus())))+")");

                                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                if(i == 0){
                                                    etLapor.setText("-");
                                                    etLapor.setEnabled(false);
                                                }
                                                else{
                                                    etLapor.setText("");
                                                    etLapor.setEnabled(true);
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> adapterView) {

                                            }
                                        });


                                        btnYa.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                if(etLapor.getText().toString().trim().length() > 0){
                                                    currentGardu.setStatus(String.valueOf(spinner.getSelectedItemId()));
                                                    currentGardu.setMaintenance_terakhir(dateFormat.format(date));

                                                    HashMap<String,String> data = new HashMap<>();
                                                    data.put("id", currentGardu.getId());
                                                    data.put("longitude", String.valueOf(currentGardu.getLongitude()));
                                                    data.put("latitude", String.valueOf(currentGardu.getLatitude()));
                                                    data.put("alamat", currentGardu.getAlamat());
                                                    data.put("jenis_gardu", currentGardu.getJenisGardu());
                                                    data.put("status", currentGardu.getStatus());
                                                    data.put("tanggal", currentGardu.getTanggal());
                                                    data.put("wilayah",currentGardu.getWilayah());
                                                    data.put("diajukan_oleh", currentGardu.getDiajukan_oleh());
                                                    data.put("diacc", currentGardu.getDiacc());
                                                    data.put("maintenance_terakhir", currentGardu.getMaintenance_terakhir());

                                                    HashMap<String,String> dataLaporan = new HashMap<>();
                                                    dataLaporan.put("id", currentGardu.getId());
                                                    dataLaporan.put("gardu_id", currentGardu.getId());
                                                    dataLaporan.put("status_lama", statusLama);
                                                    dataLaporan.put("status_baru", String.valueOf(currentGardu.getStatus()));
                                                    dataLaporan.put("laporan", etLapor.getText().toString().trim());
                                                    dataLaporan.put("tanggal_lama", tanggalLama);
                                                    dataLaporan.put("tanggal_baru", currentGardu.getTanggal());
                                                    dataLaporan.put("user_id",mAuth.getCurrentUser().getUid());

                                                    cbVerify.setChecked(true);
                                                    SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                                                    preferencesEditor.putString(DATA_KEY, helper.HashmapToString(data));
                                                    preferencesEditor.apply();

                                                    preferencesEditor.putString(DATA_KEY1, helper.HashmapToString(dataLaporan));
                                                    preferencesEditor.apply();
                                                    progressDialog = new ProgressDialog(LihatGarduActivity.this);
                                                    progressDialog.setMessage("Menyimpan ...");
                                                    progressDialog.show();

                                                    alertDialog.cancel();
                                                }
                                                else{
                                                    Toast.makeText(LihatGarduActivity.this, "Isi Deskripsi Laporan", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        btnTidak.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                alertDialog.cancel();
                                            }
                                        });

                                        alertDialog.show();
                                    }
                                    else{
                                        Toast.makeText(LihatGarduActivity.this, "Lokasi ini bukan wilayah anda", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Toast.makeText(LihatGarduActivity.this, "Lokasi belum diacc admin", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(LihatGarduActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                            }

                            return true;
                        }
                    });

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String ChangeLongLatToAddress(LatLng latLng){
        List<Address> addressList = null;

        if (latLng != null) {
            Geocoder geocoder = new Geocoder(LihatGarduActivity.this);
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
