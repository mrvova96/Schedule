package com.example.schedule.ui.group;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.databinding.FragmentGroupUpdateBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.group.GroupUpdateViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

public class GroupUpdateFragment extends Fragment {

    private static final String GROUP_ID = "groupID";
    private static final String GROUP_NAME = "facultyName";
    private static final String FACULTY_ID = "facultyID";

    private long groupID;
    private String facultyName;
    private int facultyID;

    private FragmentGroupUpdateBinding binding;
    private FragmentMainBinding parentBinding;
    private GroupUpdateViewModel viewModel;
    private Snackbar snackbar;

    public static GroupUpdateFragment newInstance(long groupID, String groupName, int facultyID) {
        GroupUpdateFragment fragment = new GroupUpdateFragment();
        Bundle args = new Bundle();
        args.putLong(GROUP_ID, groupID);
        args.putString(GROUP_NAME, groupName);
        args.putInt(FACULTY_ID, facultyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGroupUpdateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(GroupUpdateViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            groupID = getArguments().getLong(GROUP_ID);
            facultyName = getArguments().getString(GROUP_NAME);
            facultyID = getArguments().getInt(FACULTY_ID);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        binding.editTextUpdateGroup.setText(facultyName);

        viewModel.isUpdated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdated) {
                if (isUpdated) {
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                    showMessage("Запись успешно обновлена");
                } else
                    showMessage("Группа уже содержится в базе");
            }
        });

        binding.buttonUpdateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    updateGroup();
                }
            }
        });
    }

    private void updateGroup() {
        facultyName = binding.editTextUpdateGroup.getText().toString().trim();
        viewModel.update(new Group(groupID, facultyName, facultyID));
    }

    private boolean dataIsCorrect() {
        if (binding.editTextUpdateGroup.getText().toString().isBlank()) {
            showMessage("Пожалуйста, заполните все поля");
            return false;
        }
        return true;
    }

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void showMessage(String message) {
        snackbar = Snackbar.make(requireView(), message, 3000);
        snackbar.setBackgroundTint(getResources().getColor(R.color.pink, null));
        snackbar.setActionTextColor(getResources().getColor(R.color.black, null));
        TextView snackbarText = snackbar.getView()
                .findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarText.setTextSize(16);
        snackbarText.setTextColor(getResources().getColor(R.color.black, null));
        snackbar.setAction(R.string.snackbar_action, v -> snackbar.dismiss());
        snackbar.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        parentBinding = null;
    }
}