package com.example.myapplication.ui.freelancers;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentFreelancersBinding;
import com.example.myapplication.model.Freelancer;

import java.util.ArrayList;

public class FreelancersFragment extends Fragment {

    private FragmentFreelancersBinding binding;
    private FreelancerAdapter freelancerAdapter;
    private FreelancersViewModel freelancersViewModel;
    private Freelancer freelancerToDelete;

    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFreelancersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        progressBar = binding.progressBarFragmentFreelancers;
        progressBar.setVisibility(View.VISIBLE);

        freelancerAdapter = new FreelancerAdapter(getContext(), new ArrayList<>());
        binding.freelancerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.freelancerRecyclerView.setAdapter(freelancerAdapter);

        freelancersViewModel = new ViewModelProvider(this).get(FreelancersViewModel.class);
        freelancersViewModel.getFreelancerListLiveData().observe(getViewLifecycleOwner(), freelancers -> {
            freelancerAdapter.setFreelancers(freelancersViewModel.getFreelancerListLiveData().getValue());
            progressBar.setVisibility(View.GONE);
            binding.searchViewFreelancers.setVisibility(View.VISIBLE);

        });

        freelancerAdapter.setOnDeleteCLickListener(freelancer -> {
            freelancerToDelete = freelancer;
            showDeleteConfirmationDialog();
        });

        freelancerAdapter.setOnDeleteConfirmationListener(freelancer -> freelancersViewModel.deleteFreelancer(freelancer));


        binding.searchViewFreelancers.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                freelancerAdapter.getFilter().filter(""); // Atualiza a lista de freelancers filtrados
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                freelancerAdapter.getFilter().filter(newText);
                return true;
            }
        });



        return root;
    }


    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.pop_up_tittle_delete_confirmation);
        builder.setMessage(R.string.pop_up_desc_delete_freela);
        builder.setPositiveButton(R.string.pop_up_button_confirm, (dialog, which) -> {
            if (freelancersViewModel != null && freelancerToDelete != null) {
                freelancersViewModel.deleteFreelancer(freelancerToDelete);
            }
        });
        builder.setNegativeButton(R.string.pop_up_button_cancel, null);
        builder.create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
