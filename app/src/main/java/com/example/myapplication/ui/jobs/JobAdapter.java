package com.example.myapplication.ui.jobs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Freelancer;
import com.example.myapplication.model.Job;

import java.util.List;
import java.util.Map;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<Job> jobList;
    private Map<String, List<Freelancer>> jobFreelancersMap;

    public JobAdapter(List<Job> jobList, Map<String, List<Freelancer>> jobFreelancersMap) {
        this.jobList = jobList;
        this.jobFreelancersMap = jobFreelancersMap;
    }

    public void setJobs(List<Job> jobs) {
        jobList.clear();
        jobList.addAll(jobs);
        notifyDataSetChanged();
    }

    public void setJobFreelancersMap(Map<String, List<Freelancer>> jobFreelancersMap) {
        this.jobFreelancersMap.clear();
        this.jobFreelancersMap.putAll(jobFreelancersMap);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.dateTextView.setText(job.getJobDate());
        holder.timeTextView.setText(job.getJobTime());

        List<Freelancer> freelancers = jobFreelancersMap.get(job.getJobId());
        if (freelancers != null && !freelancers.isEmpty()) {
            StringBuilder freelancersText = new StringBuilder();
            for (Freelancer freelancer : freelancers) {
                freelancersText.append(freelancer.getFreelancerName()).append(", ");
            }
            freelancersText.deleteCharAt(freelancersText.length() - 2);  // Remover a vírgula extra no final
            holder.freelancersTextView.setText(freelancersText.toString());
            holder.freelancersTextView.setVisibility(View.VISIBLE);
        } else {
            holder.freelancersTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView, freelancersTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.jobDateTextView);
            timeTextView = itemView.findViewById(R.id.jobTimeTextView);
            freelancersTextView = itemView.findViewById(R.id.jobFreelancersTextView);
        }
    }
}
