package com.example.schedule.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.schedule.R;
import com.example.schedule.adapter.recyclerView.GroupListAdapter;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentGroupListBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.group.GroupListViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.example.schedule.ui.schedule.ScheduleMainFragment;
import com.example.schedule.ui.student.StudentFragment;

import java.util.List;

public class GroupListFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";
    private static final String FACULTY_NAME = "facultyName";
    private static final String BUTTON_TEXT = "buttonText";
    private static final String USER_TYPE = "userType";

    private int facultyID;
    private String facultyName;
    private String buttonText;
    private UserType userType;

    private FragmentGroupListBinding binding;
    private FragmentMainBinding parentBinding;
    private GroupListViewModel viewModel;
    private GroupListAdapter groupListAdapter;

    public static GroupListFragment newInstance(int facultyID, String facultyName, String buttonText, UserType userType) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        args.putString(FACULTY_NAME, facultyName);
        args.putString(BUTTON_TEXT, buttonText);
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
        binding = FragmentGroupListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(GroupListViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
            facultyName = getArguments().getString(FACULTY_NAME);
            buttonText = getArguments().getString(BUTTON_TEXT);
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
                groupListAdapter.setGroups(groups);
            }
        });

        groupListAdapter.setOnGroupClickListener(new GroupListAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClick(Group group) {
                if (buttonText.equals("Студенты")) {
                    parentBinding.textViewAdminPanel.setText(group.getGroupName().concat(" - Студенты"));
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    launchNextFragment(StudentFragment.newInstance(group.getID()));
                }
                if (buttonText.equals("Расписание студента")) {
                    parentBinding.textViewGroupName.setText(group.getGroupName());
                    parentBinding.textViewAdminPanel.setVisibility(View.INVISIBLE);
                    parentBinding.tabLayout.setVisibility(View.VISIBLE);
                    launchNextFragment(ScheduleMainFragment.newInstance(UserType.ADMIN, facultyID, group.getID(), 0));
                }
            }
        });
    }

    private void initAdapter() {
        groupListAdapter = new GroupListAdapter();
        binding.recyclerViewGroupList.setAdapter(groupListAdapter);
        binding.recyclerViewGroupList.setLayoutManager(new LinearLayoutManager(getContext()));
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
                if (buttonText.equals("Студенты") && userType != UserType.ADMIN)
                    parentBinding.textViewAdminPanel.setText("Выберите факультет");
                else
                    parentBinding.textViewAdminPanel.setText(facultyName);
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        parentBinding = null;
    }
}