
package com.example.myapplication.ui.jobs;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Freelancer;
import com.example.myapplication.model.Job;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobsViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final String uid = auth.getCurrentUser().getUid();

    private MutableLiveData<List<Job>> jobListLiveData;
    private MutableLiveData<List<Freelancer>> selectedJobFreelancersLiveData;
    private Map<String, List<Freelancer>> jobFreelancersMap;
    private String selectedJobId;

    public LiveData<List<Job>> getJobListLiveData() {
        if (jobListLiveData == null) {
            jobListLiveData = new MutableLiveData<>();
            loadJobsFromFirebase();
        }
        return jobListLiveData;
    }


    private void loadJobsFromFirebase() {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(uid)
                .child("Jobs")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Job> jobs = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Job job = snapshot.getValue(Job.class);
                            jobs.add(job);
                        }
                        jobListLiveData.setValue(jobs);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Tratar erros, se necessário
                    }
                });
    }
}
