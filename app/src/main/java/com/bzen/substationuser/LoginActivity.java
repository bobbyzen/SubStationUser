package com.bzen.substationuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextInputEditText tiEmail, tiPass;
    private String email, pass;
    private Button btnLogin;
    SaveSharedPreferences saveSharedPreference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveSharedPreference = new SaveSharedPreferences();

        setContentView(R.layout.activity_login);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        tiEmail = findViewById(R.id.tiEmail);
        tiPass = findViewById(R.id.tiPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In ...");
        progressDialog.show();
    }

    private void dismissProgressDIalog(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void signIn(){
        showProgressDialog();

        Log.d(TAG, "SignIn");
        if( !validateForm()){
            dismissProgressDIalog();
            return;
        }

        email = tiEmail.getText().toString();
        pass = tiPass.getText().toString();


        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                if(task.isSuccessful()){
                    onAuthSuccess(task.getResult().getUser());
                }else{
                    Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                    dismissProgressDIalog();
                }
            }

            private void onAuthSuccess(FirebaseUser user) {

                saveSharedPreference.setEmail(LoginActivity.this, email);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();

            }

            private String usernameFromEmail(String email) {
                if (email.contains("@")) {
                    return email.split("@")[0];
                } else {
                    return email;
                }
            }
        });
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(tiEmail.getText().toString())) {
            tiEmail.setError("Required");
            result = false;
        } else {
            tiEmail.setError(null);
        }

        if (TextUtils.isEmpty(tiPass.getText().toString())) {
            tiPass.setError("Required");
            result = false;
        } else {
            tiPass.setError(null);
        }

        return result;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnLogin) {
            signIn();
        }
    }
}
