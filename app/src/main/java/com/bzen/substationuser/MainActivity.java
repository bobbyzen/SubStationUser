package com.bzen.substationuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivTambah, ivGardu, ivPengaduan, ivBantuan;
    TextView tvTambah, tvGardu, tvPengaduan, tvBantuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        ivTambah = findViewById(R.id.ivTambah);
        ivTambah.setOnClickListener(this);

        tvTambah = findViewById(R.id.tvTambah);
        tvTambah.setOnClickListener(this);

        ivGardu = findViewById(R.id.ivGardu);
        ivGardu.setOnClickListener(this);

        tvGardu = findViewById(R.id.tvGardu);
        tvGardu.setOnClickListener(this);

        ivPengaduan = findViewById(R.id.ivPengaduan);
        ivPengaduan.setOnClickListener(this);

        tvPengaduan = findViewById(R.id.tvPengaduan);
        tvPengaduan.setOnClickListener(this);

        ivBantuan = findViewById(R.id.ivBantuan);
        ivBantuan.setOnClickListener(this);

        tvBantuan = findViewById(R.id.tvBantuan);
        tvBantuan.setOnClickListener(this);

        Toast.makeText(this, "AAA", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTambah :
                Toast.makeText(this, "Tambah", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.tvTambah :
                Toast.makeText(this, "Tambah", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ivGardu :
                Toast.makeText(this, "Gardu", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.tvGardu :
                Toast.makeText(this, "Gardu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ivPengaduan :
                Toast.makeText(this, "Pengaduan", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvPengaduan :
                Toast.makeText(this, "Pengaduan", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ivBantuan :
                Toast.makeText(this, "Bantuan", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvBantuan :
                Toast.makeText(this, "Bantuan", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
