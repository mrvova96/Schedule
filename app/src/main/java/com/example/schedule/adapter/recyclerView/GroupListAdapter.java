package com.example.schedule.adapter.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedule.R;
import com.example.schedule.adapter.diffUtil.GroupDiffUtilCallback;
import com.example.schedule.data.database.entity.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupListViewHolder> {

    private List<Group> groups = new ArrayList<>();

    public void setGroups(List<Group> groups) {
        GroupDiffUtilCallback productDiffUtilCallback = new GroupDiffUtilCallback(this.groups, groups);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback);
        this.groups = groups;
        productDiffResult.dispatchUpdatesTo(this);
    }

    private OnGroupClickListener onGroupClickListener;

    public void setOnGroupClickListener(GroupListAdapter.OnGroupClickListener onGroupClickListener) {
        this.onGroupClickListener = onGroupClickListener;
    }

    @NonNull
    @Override
    public GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button, parent, false);
        return new GroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListViewHolder holder, int position) {
        Group group = groups.get(position);
        holder.buttonGroup.setText(group.getGroupName());
        holder.buttonGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGroupClickListener.onGroupClick(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class GroupListViewHolder extends RecyclerView.ViewHolder {

        private Button buttonGroup;

        public GroupListViewHolder(@NonNull View itemView) {
            super(itemView);
            buttonGroup = itemView.findViewById(R.id.buttonItem);
        }
    }

    public interface OnGroupClickListener {

        void onGroupClick(Group group);
    }
}
