package com.example.schedule.ui.group;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.schedule.R;
import com.example.schedule.adapter.recyclerView.GroupEditAdapter;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentGroupBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.group.GroupViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class GroupFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";
    private static final String FACULTY_NAME = "facultyName";
    private static final String USER_TYPE = "userType";

    private int facultyID;
    private String facultyName;
    private UserType userType;

    private FragmentGroupBinding binding;
    private FragmentMainBinding parentBinding;
    private GroupViewModel viewModel;
    private GroupEditAdapter groupEditAdapter;
    private Snackbar snackbar;

    public static GroupFragment newInstance(int facultyID, String facultyName, UserType userType) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        args.putString(FACULTY_NAME, facultyName);
        args.putSerializable(USER_TYPE, userType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshGroups(facultyID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGroupBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(GroupViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
            facultyName = getArguments().getString(FACULTY_NAME);
            userType = (UserType) getArguments().getSerializable(USER_TYPE);
        }

        initAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.getGroups().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                if (groups.isEmpty())
                    binding.textViewEmptyList.setVisibility(View.VISIBLE);
                else
                    binding.textViewEmptyList.setVisibility(View.INVISIBLE);
                groupEditAdapter.setGroups(groups);
            }
        });

        parentBinding.imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                launchNextFragment(GroupAddFragment.newInstance(facultyID));
            }
        });

        groupEditAdapter.setOnUpdateClickListener(new GroupEditAdapter.OnUpdateClickListener() {
            @Override
            public void onUpdateClick(Group group) {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                launchNextFragment(GroupUpdateFragment.newInstance(
                        group.getID(), group.getGroupName(), group.getFacultyID()));
            }
        });

        groupEditAdapter.setOnDeleteClickListener(new GroupEditAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(Group group) {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Также будут удалены все связанные записи, продолжить?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.remove(group, facultyID);
                                showMessage("Запись успешно удалена");
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
            }
        });
    }

    private void initAdapter() {
        groupEditAdapter = new GroupEditAdapter();
        binding.recyclerViewGroup.setAdapter(groupEditAdapter);
        binding.recyclerViewGroup.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void launchNextFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.fragmentContainerAdmin, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (snackbar != null)
                    snackbar.dismiss();
                parentBinding.imageButtonAdd.setVisibility(View.INVISIBLE);
                if (userType == UserType.ADMIN)
                    parentBinding.textViewAdminPanel.setText(facultyName);
                else
                    parentBinding.textViewAdminPanel.setText("Выберите факультет");
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