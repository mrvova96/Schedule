package com.example.schedule.adapter.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.R;
import com.example.schedule.adapter.diffUtil.FacultyDiffUtilCallback;
import com.example.schedule.data.database.entity.Faculty;

import java.util.ArrayList;
import java.util.List;

public class FacultyListAdapter extends RecyclerView.Adapter<FacultyListAdapter.FacultyListViewHolder> {

    private List<Faculty> faculties = new ArrayList<>();

    public void setFaculties(List<Faculty> faculties) {
        FacultyDiffUtilCallback productDiffUtilCallback = new FacultyDiffUtilCallback(this.faculties, faculties);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.faculties = faculties;
        productDiffResult.dispatchUpdatesTo(this);
    }

    private OnFacultyClickListener onFacultyClickListener;

    public void setOnFacultyClickListener(FacultyListAdapter.OnFacultyClickListener onFacultyClickListener) {
        this.onFacultyClickListener = onFacultyClickListener;
    }

    @NonNull
    @Override
    public FacultyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
        return new FacultyListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyListViewHolder holder, int position) {
        Faculty faculty = faculties.get(position);
        holder.buttonFaculty.setText(faculty.getFacultyName());
        holder.buttonFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFacultyClickListener.onFacultyClick(faculty);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faculties.size();
    }

    class FacultyListViewHolder extends RecyclerView.ViewHolder {

        private Button buttonFaculty;

        public FacultyListViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonFaculty = itemView.findViewById(R.id.buttonItem);
        }
    }

    public interface OnFacultyClickListener {

        void onFacultyClick(Faculty faculty);
    }
}
