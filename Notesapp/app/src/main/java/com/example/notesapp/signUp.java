package com.example.notesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notesapp.mainpage.firstPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signUp extends AppCompatActivity {

    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    MaterialButton signin, guest;
    TextInputEditText em, ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signin = findViewById(R.id.signup);
        guest = findViewById(R.id.guest);
        em = findViewById(R.id.signUpEmail);
        ps = findViewById(R.id.signUpPassword);

        // Check if user is already logged in
        if (isUserLoggedIn()) {
            navigateToMainPage();
        }

        guest.setOnClickListener(v -> {
            Intent intent = new Intent(signUp.this, firstPage.class);
            startActivity(intent);
        });

        signin.setOnClickListener(v -> FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(em.getText().toString(), ps.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserCredentials(em.getText().toString(), ps.getText().toString());
                            navigateToMainPage();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sign-up failed, try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(e ->
                        Toast.makeText(getApplicationContext(), "Sign-up failed, try again", Toast.LENGTH_SHORT).show()
                )
        );
    }

    // Method to check if user credentials are already stored
    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(KEY_EMAIL) && sharedPreferences.contains(KEY_PASSWORD);
    }

    // Method to save email and password in SharedPreferences
    private void saveUserCredentials(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    // Method to navigate to the main page after successful login
    private void navigateToMainPage() {
        startActivity(new Intent(signUp.this, firstPage.class));
        finish();
    }
}
