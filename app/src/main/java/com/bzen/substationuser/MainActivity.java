package com.bzen.substationuser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView ivTambah, ivGardu, ivPengaduan, ivBantuan;
    TextView tvTambah, tvGardu, tvPengaduan, tvBantuan;
    SaveSharedPreferences saveSharedPreference;

    ConstraintLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveSharedPreference = new SaveSharedPreferences();

        background = findViewById(R.id.background);
        try {
            Drawable b = Glide.with(this).asDrawable().load(R.drawable.bgc).into(100,100).get();
            background.setBackground(b);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(saveSharedPreference.getEmail(MainActivity.this).length() == 0){

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }else{

            setContentView(R.layout.activity_main);

            ivTambah = findViewById(R.id.ivTambah);
            ivTambah.setOnClickListener(this);

            ivGardu = findViewById(R.id.ivGardu);
            ivGardu.setOnClickListener(this);

            ivPengaduan = findViewById(R.id.ivPengaduan);
            ivPengaduan.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivTambah :
                Toast.makeText(this, "Tambah", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.ivGardu :
                Toast.makeText(this, "Gardu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ivPengaduan :
                Toast.makeText(this, "Pengaduan", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
