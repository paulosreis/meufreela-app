package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Freelancer;

import java.util.ArrayList;
import java.util.List;

public class FreelancerSelectionAdapter extends RecyclerView.Adapter<FreelancerSelectionAdapter.ViewHolder> {
    private final List<Freelancer> freelancerList;
    private final List<String> selectedFreelancerIds;

    public FreelancerSelectionAdapter(List<Freelancer> freelancerList) {
        this.freelancerList = freelancerList;
        this.selectedFreelancerIds = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_freelancer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Freelancer freelancer = freelancerList.get(position);
        holder.bind(freelancer);
    }

    @Override
    public int getItemCount() {
        return freelancerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView freelancerNameTextView;
        private final TextView freelancerFuncTextView;
        private final CheckBox freelancerCheckBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            freelancerNameTextView = itemView.findViewById(R.id.name_new_job_FreelancerTextView);
            freelancerFuncTextView = itemView.findViewById(R.id.func_new_job_FreelancerTextView);

            freelancerCheckBox = itemView.findViewById(R.id.checkboxFreelancer);

            freelancerCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Freelancer freelancer = freelancerList.get(adapterPosition);
                    String freelancerId = freelancer.getFreelancerId();
                    if (isChecked) {
                        selectedFreelancerIds.add(freelancerId);
                    } else {
                        selectedFreelancerIds.remove(freelancerId);
                    }
                }
            });
        }


        public void bind(Freelancer freelancer) {
            freelancerFuncTextView.setText(freelancer.getFreelancerFunc());
            freelancerNameTextView.setText(freelancer.getFreelancerName());
            freelancerCheckBox.setChecked(selectedFreelancerIds.contains(freelancer.getFreelancerId()));
        }
    }

    // Método para obter a lista de IDs dos freelancers selecionados
    public List<String> getSelectedFreelancerIds() {
        return new ArrayList<>(selectedFreelancerIds);
    }


    @SuppressLint("NotifyDataSetChanged")
    public void clearSelectedFreelancerIds() {
        selectedFreelancerIds.clear();
        notifyDataSetChanged();
    }

}

