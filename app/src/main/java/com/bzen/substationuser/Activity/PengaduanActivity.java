package com.bzen.substationuser.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bzen.substationuser.Adapter.PengaduanAdapter;
import com.bzen.substationuser.Model.Gardu;
import com.bzen.substationuser.Model.Pengaduan;
import com.bzen.substationuser.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PengaduanActivity extends AppCompatActivity {

    RecyclerView rvPengaduan;
    ArrayList<Pengaduan> listPengaduan;
    PengaduanAdapter adapter;

    DatabaseReference rootRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaduan);

        rvPengaduan = findViewById(R.id.rvPengaduan);

        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ShowRecycleView();
    }

    private void ShowRecycleView() {
        rootRef.child("Laporan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPengaduan = new ArrayList<>();
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Pengaduan pengaduan = child.getValue(Pengaduan.class);
                    listPengaduan.add(pengaduan);
                }

                adapter = new PengaduanAdapter(listPengaduan, mAuth.getCurrentUser().getDisplayName());
                adapter.notifyDataSetChanged();
                rvPengaduan.setLayoutManager(new LinearLayoutManager(PengaduanActivity.this));
                rvPengaduan.setHasFixedSize(true);
                rvPengaduan.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
