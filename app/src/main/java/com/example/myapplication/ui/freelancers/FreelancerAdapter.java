package com.example.myapplication.ui.freelancers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.NewFreelancer;
import com.example.myapplication.R;
import com.example.myapplication.model.Freelancer;

import java.util.ArrayList;
import java.util.List;

public class FreelancerAdapter extends RecyclerView.Adapter<FreelancerAdapter.MyViewHolder> implements Filterable {
    private final Context context;
    private List<Freelancer> freelancerList;
    private final List<Freelancer> filteredFreelancerList = new ArrayList<>();

    private OnDeleteCLickListener onDeleteCLickListener;
    private OnDeleteConfirmationListener onDeleteConfirmationListener;

    public FreelancerAdapter(Context context, List<Freelancer> freelancerList) {
        this.context = context;
        this.freelancerList = freelancerList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFreelancers(List<Freelancer> freelancers) {
        freelancerList = freelancers;
        filteredFreelancerList.clear();
        filteredFreelancerList.addAll(freelancers);
        notifyDataSetChanged();
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterPattern = constraint.toString().toLowerCase().trim();
                List<Freelancer> filteredList = new ArrayList<>();
                if (filterPattern.isEmpty()) {
                    filteredList.addAll(freelancerList);
                } else {
                    for (Freelancer freelancer : freelancerList) {
                        if (freelancer.getFreelancerName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(freelancer);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredFreelancerList.clear();
                filteredFreelancerList.addAll((List<Freelancer>) results.values);
                notifyDataSetChanged();
            }
        };
    }


    public interface OnDeleteCLickListener {
        @SuppressWarnings("unused")
        void onDeleteClick(Freelancer freelancer);
    }

    public void setOnDeleteCLickListener(OnDeleteCLickListener listener) {
        onDeleteCLickListener = listener;
    }

    public interface OnDeleteConfirmationListener {
        void onDeleteConfirmation(Freelancer freelancer);
    }

    public void setOnDeleteConfirmationListener(OnDeleteConfirmationListener listener) {
        onDeleteConfirmationListener = listener;
    }

    private void showDeleteConfirmationDialog(final Freelancer freelancer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.pop_up_tittle_delete_confirmation);
        builder.setMessage(R.string.pop_up_desc_delete_freela);
        builder.setPositiveButton(R.string.pop_up_button_confirm, (dialog, which) -> {
            if (onDeleteConfirmationListener != null) {
                onDeleteConfirmationListener.onDeleteConfirmation(freelancer);
            }
        });
        builder.setNegativeButton(R.string.pop_up_button_cancel, null);
        builder.create().show();
    }

    private void editFreelancer(Freelancer freelancer) {
        Intent intent = new Intent(context, NewFreelancer.class);
        intent.putExtra("freelancerId", freelancer.getFreelancerId());
        intent.putExtra("freelancerName", freelancer.getFreelancerName());
        intent.putExtra("freelancerFunc", freelancer.getFreelancerFunc());
        intent.putExtra("freelancerPhone", freelancer.getFreelancerPhone());
        context.startActivity(intent);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_freelancer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Freelancer freelancer = filteredFreelancerList.get(position);
        holder.nameFreelancerCardViewText.setText(freelancer.getFreelancerName());
        holder.funcFreelancerCardTextView.setText(freelancer.getFreelancerFunc());
        holder.phoneFreelancerCardTextView.setText(String.format("%s %s", context.getString(R.string.phone_label), freelancer.getFreelancerPhone()));
        holder.buttonDeleteFreelancerCard.setOnClickListener(v -> {
            if (onDeleteCLickListener != null) {
                showDeleteConfirmationDialog(freelancer);
            }
        });
        holder.buttonEditFreelancerCard.setOnClickListener(v -> editFreelancer(freelancer));

    }

    @Override
    public int getItemCount() {
        return filteredFreelancerList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recImage;
        TextView nameFreelancerCardViewText;
        TextView funcFreelancerCardTextView;
        TextView phoneFreelancerCardTextView;
        CardView cardFreelancer;
        ImageButton buttonDeleteFreelancerCard, buttonEditFreelancerCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recImage = itemView.findViewById(R.id.recImage);
            nameFreelancerCardViewText = itemView.findViewById(R.id.nameFreelancerCardTextView);
            funcFreelancerCardTextView = itemView.findViewById(R.id.funcFreelancerCardTextView);
            phoneFreelancerCardTextView = itemView.findViewById(R.id.phoneFreelancerCardTextView);
            cardFreelancer = itemView.findViewById(R.id.cardFreelancer);
            buttonDeleteFreelancerCard = itemView.findViewById(R.id.buttonDeleteFreelancerCard);
            buttonEditFreelancerCard = itemView.findViewById(R.id.buttonEditFreelancerCard);
        }
    }
}
