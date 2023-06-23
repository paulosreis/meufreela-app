package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Freelancer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class NewFreelancer extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_freelancer);

        EditText mNameEditText = findViewById(R.id.freelancer_name_edit);
        EditText mFunctionEditText = findViewById(R.id.freelancer_funcao_edit);
        EditText mPhoneEditText = findViewById(R.id.freelancer_phone_edit);
        ProgressBar progressBar = findViewById(R.id.newFreelancerProgressBar);

        Button registerNewFreelancerButton = findViewById(R.id.registerNewFreelancerButton);
        registerNewFreelancerButton.setOnClickListener(view -> {
            String name = mNameEditText.getText().toString();
            String function = mFunctionEditText.getText().toString();
            String phone = mPhoneEditText.getText().toString();


            int errorCount = 0;

            // Verifica campo "email"
            if (TextUtils.isEmpty(name)) {
                mNameEditText.setError("Campo obrigatório!");
                errorCount++;
            }
            if (TextUtils.isEmpty(function)) {
                mFunctionEditText.setError("Campo obrigatório!");
                errorCount++;
            }
            if (TextUtils.isEmpty(phone)) {
                mPhoneEditText.setError("Campo obrigatório!");
                errorCount++;
            } else if (phone.length() < 9) {
                mPhoneEditText.setError("Insira um número válido");
                errorCount++;
            }


            // Exibe as mensagens de erro em todos os campos se houver erros
            if (errorCount > 0) {
                if (TextUtils.isEmpty(name)) {
                    mNameEditText.requestFocus();
                } else if (TextUtils.isEmpty(function)) {
                    mFunctionEditText.requestFocus();
                } else if (TextUtils.isEmpty(phone) || phone.length() < 9) {
                    mPhoneEditText.requestFocus();
                }
                return;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                Freelancer freelancer = new Freelancer(name, function, phone);
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(uid)
                        .child("Freelancers")
                        .push()
                        .setValue(freelancer).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {

                                Toast.makeText(getApplicationContext(), "Salvo",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show());
            }


            finish();
        });

        Button backNewFreelancerButton = findViewById(R.id.backNewFreelancerButton);
        backNewFreelancerButton.setOnClickListener(v -> finish());


    }
}