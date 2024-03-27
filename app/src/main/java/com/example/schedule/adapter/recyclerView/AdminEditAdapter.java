package com.example.schedule.adapter.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.R;
import com.example.schedule.adapter.diffUtil.AdminDiffUtilCallback;
import com.example.schedule.data.database.entity.Admin;

import java.util.ArrayList;
import java.util.List;

public class AdminEditAdapter extends RecyclerView.Adapter<AdminEditAdapter.AdminEditViewHolder> {

    private List<Admin> admins = new ArrayList<>();

    public void setAdmins(List<Admin> admins) {
        AdminDiffUtilCallback productDiffUtilCallback = new AdminDiffUtilCallback(this.admins, admins);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.admins = admins;
        productDiffResult.dispatchUpdatesTo(this);
    }

    private OnUpdateClickListener onUpdateClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnUpdateClickListener(AdminEditAdapter.OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public void setOnDeleteClickListener(AdminEditAdapter.OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public AdminEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit, parent, false);
        return new AdminEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminEditViewHolder holder, int position) {
        Admin admin = admins.get(position);
        holder.textViewItem.setText(admin.getSurname().concat(" ").concat(admin.getName())
                .concat(" ").concat(admin.getPatronymic()));
        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClickListener.onUpdateClick(admin);
            }
        });
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(admin);
            }
        });
    }

    @Override
    public int getItemCount() {
        return admins.size();
    }

    class AdminEditViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItem;
        private ImageButton imageViewEdit;
        private ImageButton imageViewDelete;

        public AdminEditViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }

    public interface OnUpdateClickListener {

        void onUpdateClick(Admin admin);
    }

    public interface OnDeleteClickListener {

        void onDeleteClick(Admin admin);
    }
}
