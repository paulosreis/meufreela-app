package com.example.myapplication.ui.freelancers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Freelancer;

import java.util.List;

public class FreelancerAdapter extends RecyclerView.Adapter<FreelancerAdapter.MyViewHolder> {
    private final Context context;
    private List<Freelancer> freelancerList;
    private OnDeleteCLickListener onDeleteCLickListener;
    private OnDeleteConfirmationListener onDeleteConfirmationListener;

    public FreelancerAdapter(Context context, List<Freelancer> freelancerList) {
        this.context = context;
        this.freelancerList = freelancerList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFreelancers(List<Freelancer> freelancers) {
        freelancerList = freelancers;
        notifyDataSetChanged();
    }

    public interface OnDeleteCLickListener {
        @SuppressWarnings("unused")
        void onDeleteClick(Freelancer freelancer);
    }

    public void setOnDeleteCLickListener(OnDeleteCLickListener listener){
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
        builder.setTitle("Confirmação de exclusão");
        builder.setMessage("Deseja excluir este freelancer?");
        builder.setPositiveButton("Sim", (dialog, which) -> {
            if (onDeleteConfirmationListener != null) {
                onDeleteConfirmationListener.onDeleteConfirmation(freelancer);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
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
        Freelancer freelancer = freelancerList.get(position);
        holder.nameFreelancerCardViewText.setText(freelancer.getFreelancerName());
        holder.funcFreelancerCardTextView.setText(freelancer.getFreelancerFunc());
        holder.phoneFreelancerCardTextView.setText(freelancer.getFreelancerPhone());
        holder.buttonDeleteFreelancerCard.setOnClickListener(v -> {
            if (onDeleteCLickListener != null){
                showDeleteConfirmationDialog(freelancer);
            }
        });

        // Restante do código do ViewHolder...
    }

    @Override
    public int getItemCount() {
        return freelancerList.size();
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