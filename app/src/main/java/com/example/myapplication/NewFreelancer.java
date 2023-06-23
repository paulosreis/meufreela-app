package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.model.Freelancer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NewFreelancer extends AppCompatActivity {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();

    private EditText mNameEditText, mFunctionEditText, mPhoneEditText;
    private ProgressBar progressBar;

    private String freelancerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_freelancer);

        TextView mTittle = findViewById(R.id.headline_info_new_freelancer);
        mNameEditText = findViewById(R.id.freelancer_name_edit);
        mFunctionEditText = findViewById(R.id.freelancer_funcao_edit);
        mPhoneEditText = findViewById(R.id.freelancer_phone_edit);
        progressBar = findViewById(R.id.newFreelancerProgressBar);

        Button registerNewFreelancerButton = findViewById(R.id.registerNewFreelancerButton);
        Button backNewFreelancerButton = findViewById(R.id.backNewFreelancerButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Obtém o ID do freelancer da intent de edição
            freelancerId = extras.getString("freelancerId");
            mTittle.setText(R.string.headline_edit_freelancer);
            if (freelancerId != null) {
                // Modo de edição - preenche os campos com os dados do freelancer existente
                mNameEditText.setText(extras.getString("freelancerName"));
                mFunctionEditText.setText(extras.getString("freelancerFunc"));
                mPhoneEditText.setText(extras.getString("freelancerPhone"));
            }
        } else {
            mTittle.setText(R.string.headline_new_freelancer);

        }

        registerNewFreelancerButton.setOnClickListener(view -> saveFreelancer());
        backNewFreelancerButton.setOnClickListener(v -> finish());
    }

    private void saveFreelancer() {
        String name = mNameEditText.getText().toString();
        String function = mFunctionEditText.getText().toString();
        String phone = mPhoneEditText.getText().toString();

        int errorCount = 0;

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

        if (errorCount > 0) {
            if (TextUtils.isEmpty(name)) {
                mNameEditText.requestFocus();
            } else if (TextUtils.isEmpty(function)) {
                mFunctionEditText.requestFocus();
            } else if (TextUtils.isEmpty(phone) || phone.length() < 9) {
                mPhoneEditText.requestFocus();
            }
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Freelancer freelancer = new Freelancer(name, function, phone);
            if (freelancerId != null) {
                // Modo de edição - atualiza os dados do freelancer existente
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(uid)
                        .child("Freelancers")
                        .child(freelancerId)
                        .setValue(freelancer)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                // Modo de criação - salva um novo freelancer
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(uid)
                        .child("Freelancers")
                        .push()
                        .setValue(freelancer)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Salvo com successo", Toast.LENGTH_SHORT).show();

                                finish();
                            }
                        })
                        .addOnFailureListener(e -> Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }
    }
}
