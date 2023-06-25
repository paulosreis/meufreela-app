package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mConfirmEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;

    private FirebaseAuth mAuth;

    ProgressBar progressBar;

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
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mEmailEditText = findViewById(R.id.emailCreateAccountEdit);
        mConfirmEmailEditText = findViewById(R.id.emailConfirmCreateAccountEdit);
        mPasswordEditText = findViewById(R.id.passwordCreateAccountEdit);
        mConfirmPasswordEditText = findViewById(R.id.passwordConfirmCreateAccountEdit);
        progressBar = findViewById(R.id.registerProgressBar);

        Button createAccountButton = findViewById(R.id.createAccountButton);
        createAccountButton.setOnClickListener(v -> {
            String email = mEmailEditText.getText().toString().trim();
            String confirmEmail = mConfirmEmailEditText.getText().toString().trim();
            String password = mPasswordEditText.getText().toString().trim();
            String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

            int errorCount = 0;

            // Verifica campo "email"
            if (TextUtils.isEmpty(email)) {
                mEmailEditText.setError(getString(R.string.EmailErrorTextField));
                errorCount++;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmailEditText.setError(getString(R.string.InvalidEmailErrorTextField));
                errorCount++;
            }

            // Verifica campo "confirmEmail"
            if (TextUtils.isEmpty(confirmEmail)) {
                mConfirmEmailEditText.setError(getString(R.string.EmailConfirmationErrorTextField));
                errorCount++;
            } else if (!email.equals(confirmEmail)) {
                mConfirmEmailEditText.setError(getString(R.string.DifferentEmailErrorTextField));
                errorCount++;
            }

            // Verifica campo "password"
            if (TextUtils.isEmpty(password)) {
                mPasswordEditText.setError(getString(R.string.PasswordErrorTextField));
                errorCount++;
            } else if (password.length() < 8) {
                mPasswordEditText.setError(getString(R.string.PasswordLengthErrorTextField));
                errorCount++;
            }

            // Verifica campo "confirmPassword"
            if (TextUtils.isEmpty(confirmPassword)) {
                mConfirmPasswordEditText.setError(getString(R.string.PasswordConfirmationErrorTextField));
                errorCount++;
            } else if (!password.equals(confirmPassword)) {
                mConfirmPasswordEditText.setError(getString(R.string.DifferentPasswordErrorTextField));
                errorCount++;
            }

            // Exibe as mensagens de erro em todos os campos se houver erros
            if (errorCount > 0) {
                if (TextUtils.isEmpty(email)) {
                    mEmailEditText.requestFocus();
                } else if (TextUtils.isEmpty(confirmEmail) || !email.equals(confirmEmail)) {
                    mConfirmEmailEditText.requestFocus();
                } else if (TextUtils.isEmpty(password) || password.length() < 8) {
                    mPasswordEditText.requestFocus();
                } else {
                    mConfirmPasswordEditText.requestFocus();
                }
                return;
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "createUserWithEmail:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
                                    Toast.makeText(RegisterActivity.this, "Conta criada com sucesso",
                                            Toast.LENGTH_SHORT).show();

                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
//                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Não foi possível criar a conta.",
                                            Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                                }
                            }
                        });

            }

            // Realiza a criação da conta se todos os campos estiverem preenchidos corretamente
            // TODO: realizar a criação da conta






        });

        Button backToLoginButton = findViewById(R.id.backToLoginButton);
        backToLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}