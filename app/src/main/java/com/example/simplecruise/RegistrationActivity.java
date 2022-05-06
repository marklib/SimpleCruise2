package com.example.simplecruise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private static final String LOG_TAG = RegistrationActivity.class.getName();
    private static final String PREF_KEY = RegistrationActivity.class.getPackage().toString();


    EditText usernameInput;
    EditText emailInput;
    EditText passwordInput;
    EditText passwordConfirmInput;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        usernameInput = findViewById(R.id.regName);
        emailInput = findViewById(R.id.regEmail);
        passwordInput = findViewById(R.id.regPassword);
        passwordConfirmInput = findViewById(R.id.regPasswordConfirm);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        usernameInput.setText(username);
        passwordInput.setText(password);

        mAuth = FirebaseAuth.getInstance();
    }

    public void register(View view){
        String username = usernameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwordConfirm = passwordConfirmInput.getText().toString();

        if(!password.equals(passwordConfirm)){
            Log.e(LOG_TAG, "Nem egyenlő a jelszó és a megerősítés.");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    checkCruises();
                }
                else{
                    //
                    Toast.makeText(RegistrationActivity.this, "Sikertelen regisztráció: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void cancel(View view){
        finish();
    }

    private void checkCruises(){
        Intent intent = new Intent(this, CheckCruisesActivity.class);
        startActivity(intent);
    }

}