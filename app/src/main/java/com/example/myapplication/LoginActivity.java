package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;

    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailLoginEditText);
        passwordEditText = findViewById(R.id.passwordLoginEditText);
        progressBar = findViewById(R.id.registerProgressBar);
        Button loginButton = findViewById(R.id.loginButton);
        Button forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        Button createAccountButton = findViewById(R.id.createAccountButton);



        // ...

        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            int errorCount = 0;

            // Verifica campo "email"
            if (TextUtils.isEmpty(email)) {
                emailEditText.setError(getString(R.string.EmailErrorTextField));
                errorCount++;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError(getString(R.string.InvalidEmailErrorTextField));
                errorCount++;
            }

            // Verifica campo "password"
            if (TextUtils.isEmpty(password)) {
                passwordEditText.setError(getString(R.string.PasswordErrorTextField));
                errorCount++;
            }

            if (errorCount > 0) {
                if (TextUtils.isEmpty(email)) {
                    emailEditText.requestFocus();
                }  else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    passwordEditText.requestFocus();
                }
                return;
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInWithEmail:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
                                Toast.makeText(LoginActivity.this, "Autenticação realizada com sucesso.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
//                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Email ou senha incorretos.",
                                        Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                            }
                        });

            }

            // TODO: implemente a lógica de login aqui

        });



        forgotPasswordButton.setOnClickListener(view -> {
            // TODO: implement forgot password logic here
        });

        createAccountButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // ...
    }

}

