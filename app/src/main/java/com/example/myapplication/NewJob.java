package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Freelancer;
import com.example.myapplication.model.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class NewJob extends AppCompatActivity {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();

    private EditText mDateEditText, mTimeEditText;
    private ProgressBar progressBar;
    private FreelancerSelectionAdapter adapter;
    private List<Freelancer> freelancerList;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);

        mDateEditText = findViewById(R.id.job_date_edit);
        mTimeEditText = findViewById(R.id.job_time_edit);
        progressBar = findViewById(R.id.newJobProgressBar);
        RecyclerView recyclerView = findViewById(R.id.freelancer_new_job_RecyclerView);

        TextView mTittle = findViewById(R.id.headline_info_new_job);

        Button registerNewJobButton = findViewById(R.id.registerNewJobButton);
        Button backNewJobButton = findViewById(R.id.backNewJobButton);

        mTittle.setText(R.string.headline_new_job);

        registerNewJobButton.setOnClickListener(view -> saveJob());
        backNewJobButton.setOnClickListener(v -> finish());

        mDateSetListener = (view, year, month, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
            mDateEditText.setText(selectedDate);
        };

        mTimeSetListener = (view, hourOfDay, minute) -> {
            String selectedTime = hourOfDay + ":" + minute;
            mTimeEditText.setText(selectedTime);
        };

        mDateEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(NewJob.this, mDateSetListener, year, month, dayOfMonth);
                datePickerDialog.show();
                mDateEditText.clearFocus();
            }
        });

        mTimeEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewJob.this, mTimeSetListener, hourOfDay, minute, true);
                timePickerDialog.show();

                mTimeEditText.clearFocus();

            }
        });

        // Inicialize a lista de freelancers e o adaptador
        freelancerList = new ArrayList<>();
        adapter = new FreelancerSelectionAdapter(freelancerList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Obtenha a lista de freelancers do banco de dados
        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(uid)
                .child("Freelancers")
                .orderByChild("name")

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot freelancerSnapshot : dataSnapshot.getChildren()) {
                            Freelancer freelancer = freelancerSnapshot.getValue(Freelancer.class);
                            if (freelancer != null) {
                                freelancer.setFreelancerId(freelancerSnapshot.getKey());
                                freelancerList.add(freelancer);
                            }
                        }
                        freelancerList.sort((f1, f2) -> f1.getFreelancerName().compareToIgnoreCase(f2.getFreelancerName()));

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(NewJob.this, "Erro ao obter a lista de freelancers.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveJob() {
        String date = mDateEditText.getText().toString();
        String time = mTimeEditText.getText().toString();

        int errorCount = 0;

        if (TextUtils.isEmpty(date)) {
            mDateEditText.setError("Campo obrigatório");
            errorCount++;
        }

        if (TextUtils.isEmpty(time)) {
            mTimeEditText.setError("Campo obrigatório");
            errorCount++;
        }

        if (errorCount == 0) {
            progressBar.setVisibility(View.VISIBLE);

            String jobId = FirebaseDatabase.getInstance().getReference().child("jobs").push().getKey();

            // Obtenha a lista de IDs dos freelancers selecionados
            List<String> selectedFreelancerIds = adapter.getSelectedFreelancerIds();
            adapter.clearSelectedFreelancerIds();

            if (selectedFreelancerIds.isEmpty()) {
                Toast.makeText(NewJob.this, "Selecione pelo menos um freelancer.", Toast.LENGTH_SHORT).show();
                return;
            }

            Job job = new Job(jobId, date, time, selectedFreelancerIds);

            // Salve o objeto Job no banco de dados
            assert jobId != null;
            FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(uid)
                    .child("Jobs")
                    .child(jobId)
                    .setValue(job)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE); // Esconder a barra de progresso após a conclusão
                        if (task.isSuccessful()) {
                            Toast.makeText(NewJob.this, "Trabalho salvo com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(NewJob.this, "Erro ao salvar o trabalho. Tente novamente.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}

