package com.bzen.substationuser.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bzen.substationuser.R;
import com.bzen.substationuser.SaveSharedPreferences;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    ImageView ivTambah, ivGardu, ivPengaduan, ivBantuan;
    Button btnTambah, btnGardu, btnPengaduan;
    TextView tvTambah, tvGardu, tvPengaduan, tvBantuan;
    SaveSharedPreferences saveSharedPreference;

//    ConstraintLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveSharedPreference = new SaveSharedPreferences();

        if(saveSharedPreference.getEmail(MainActivity.this).length() == 0){

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }else{

            setContentView(R.layout.activity_main);

            btnGardu = findViewById(R.id.btnGardu);
            btnGardu.setOnClickListener(this);

            btnTambah = findViewById(R.id.btnTambah);
            btnTambah.setOnClickListener(this);

            btnPengaduan = findViewById(R.id.btnPengaduan);
            btnPengaduan.setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTambah :
                Intent intent = new Intent(this, TambahGarduActivity.class);
                startActivity(intent);
                break;
            case R.id.btnGardu :
                Intent intent1 = new Intent(this, LihatGarduActivity.class);
                startActivity(intent1);
                break;
            case R.id.btnPengaduan :
                Intent intent2 = new Intent(this, PengaduanActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
