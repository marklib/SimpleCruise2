package com.example.simplecruise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    EditText usernameInput;
    EditText passwordInput;

    private SharedPreferences preferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInput = findViewById(R.id.loginUsername);
        passwordInput = findViewById(R.id.loginPassword);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
    }

    public void login(View view){

        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    checkCruises();
                }
                else{
                    Toast.makeText(MainActivity.this, "Sikertelen bejelentkezés: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkCruises(){
        Intent intent = new Intent(this, CheckCruisesActivity.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onPause(){
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",usernameInput.getText().toString());
        editor.putString("password",passwordInput.getText().toString());
        editor.apply();
    }


    public void guestLogin(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    checkCruises();
                }
                else{
                    Toast.makeText(MainActivity.this, "Sikertelen belépés: "+task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}