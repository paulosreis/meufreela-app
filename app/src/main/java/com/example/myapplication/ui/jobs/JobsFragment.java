package com.example.myapplication.ui.jobs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myapplication.databinding.FragmentJobsBinding;
import com.example.myapplication.model.Freelancer;
import com.example.myapplication.model.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JobsFragment extends Fragment {

    private FragmentJobsBinding binding;
    private JobsViewModel jobsViewModel;
    private JobAdapter jobAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentJobsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        jobsViewModel = new ViewModelProvider(this).get(JobsViewModel.class);

        jobAdapter = new JobAdapter(new ArrayList<>(), new HashMap<>());
        binding.jobsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.jobsRecyclerView.setAdapter(jobAdapter);

        jobsViewModel.getJobListLiveData().observe(getViewLifecycleOwner(), jobs -> {
            jobAdapter.setJobs(jobs);
            if (jobs.isEmpty()) {
                binding.textEmptyJobs.setVisibility(View.VISIBLE);
            } else {
                binding.textEmptyJobs.setVisibility(View.GONE);
            }
        });




        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}