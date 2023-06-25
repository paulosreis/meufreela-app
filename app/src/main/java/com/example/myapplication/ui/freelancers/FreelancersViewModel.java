package com.example.myapplication.ui.freelancers;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Freelancer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FreelancersViewModel extends ViewModel {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
    private MutableLiveData<List<Freelancer>> freelancerListLiveData;

    public LiveData<List<Freelancer>> getFreelancerListLiveData() {
        if (freelancerListLiveData == null) {
            freelancerListLiveData = new MutableLiveData<>();
            loadFreelancersFromFirebase();
        }
        return freelancerListLiveData;
    }

    private void loadFreelancersFromFirebase() {
        FirebaseDatabase.getInstance().getReference("Users")
                .child(uid)
                .child("Freelancers")
                .orderByChild("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Freelancer> freelancerList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Freelancer freelancer = snapshot.getValue(Freelancer.class);
                            assert freelancer != null;
                            freelancer.setFreelancerId(snapshot.getKey());
                            freelancerList.add(freelancer);
                        }

                        freelancerList.sort((f1, f2) -> f1.getFreelancerName().compareToIgnoreCase(f2.getFreelancerName()));
                        freelancerListLiveData.setValue(freelancerList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Tratar erros, se necessário
                    }
                });
    }

    public void deleteFreelancer(Freelancer freelancer) {
        String freelancerId = freelancer.getFreelancerId();
        FirebaseDatabase.getInstance().getReference("Users")
                .child(uid)
                .child("Freelancers")
                .child(freelancerId)
                .removeValue()
                .addOnSuccessListener(aVoid -> {
//                        Toast.makeText(
//                                YourApplicationContextHere,
//                                "Freelancer excluído com sucesso.",
//                                Toast.LENGTH_SHORT
//                        ).show();
                    loadFreelancersFromFirebase();
                })
                .addOnFailureListener(e -> {
//                        Toast.makeText(
//                                YourApplicationContextHere,
//                                "Erro ao excluir freelancer.",
//                                Toast.LENGTH_SHORT
//                        ).show();
                });
    }
}
