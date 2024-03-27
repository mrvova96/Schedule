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
import com.example.schedule.adapter.diffUtil.FacultyDiffUtilCallback;
import com.example.schedule.data.database.entity.Faculty;

import java.util.ArrayList;
import java.util.List;

public class FacultyEditAdapter extends RecyclerView.Adapter<FacultyEditAdapter.FacultyEditViewHolder> {

    private List<Faculty> faculties = new ArrayList<>();

    public void setFaculties(List<Faculty> faculties) {
        FacultyDiffUtilCallback productDiffUtilCallback = new FacultyDiffUtilCallback(this.faculties, faculties);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.faculties = faculties;
        productDiffResult.dispatchUpdatesTo(this);
    }

    private OnUpdateClickListener onUpdateClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnUpdateClickListener(FacultyEditAdapter.OnUpdateClickListener onUpdateClickListener) {
        this.onUpdateClickListener = onUpdateClickListener;
    }

    public void setOnDeleteClickListener(FacultyEditAdapter.OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public FacultyEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit, parent, false);
        return new FacultyEditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyEditViewHolder holder, int position) {
        Faculty faculty = faculties.get(position);
        holder.textViewItem.setText(faculty.getFacultyName());
        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdateClickListener.onUpdateClick(faculty);
            }
        });
        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickListener.onDeleteClick(faculty);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faculties.size();
    }

    class FacultyEditViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewItem;
        private ImageButton imageViewEdit;
        private ImageButton imageViewDelete;

        public FacultyEditViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
            imageViewEdit = itemView.findViewById(R.id.imageViewEdit);
            imageViewDelete = itemView.findViewById(R.id.imageViewDelete);
        }
    }

    public interface OnUpdateClickListener {

        void onUpdateClick(Faculty faculty);
    }

    public interface OnDeleteClickListener {

        void onDeleteClick(Faculty faculty);
    }
}
