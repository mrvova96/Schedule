package com.example.schedule.ui.classroom;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedule.R;
import com.example.schedule.adapter.recyclerView.ClassroomListAdapter;
import com.example.schedule.adapter.recyclerView.FacultyListAdapter;
import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentClassroomListBinding;
import com.example.schedule.databinding.FragmentFacultyListBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.classroom.ClassroomListViewModel;
import com.example.schedule.presentation.faculty.FacultyListViewModel;
import com.example.schedule.ui.admin.AdminFragment;
import com.example.schedule.ui.main.MainFragment;
import com.example.schedule.ui.classroom.ClassroomFragment;
import com.example.schedule.ui.group.GroupFragment;
import com.example.schedule.ui.group.GroupListFragment;
import com.example.schedule.ui.lesson.LessonFragment;
import com.example.schedule.ui.schedule.ScheduleMenuFragment;
import com.example.schedule.ui.teacher.TeacherMenuFragment;

import java.util.List;

public class ClassroomListFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";
    private static final String FACULTY_NAME = "facultyName";

    private int facultyID;
    private String facultyName;

    private FragmentClassroomListBinding binding;
    private FragmentMainBinding parentBinding;
    private ClassroomListViewModel viewModel;
    private ClassroomListAdapter classroomListAdapter;

    public static ClassroomListFragment newInstance(int facultyID, String facultyName) {
        ClassroomListFragment fragment = new ClassroomListFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        args.putString(FACULTY_NAME, facultyName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshClassrooms(facultyID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClassroomListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ClassroomListViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
            facultyName = getArguments().getString(FACULTY_NAME);
        }

        initAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.getClassrooms().observe(getViewLifecycleOwner(), new Observer<List<Classroom>>() {
            @Override
            public void onChanged(List<Classroom> classrooms) {
                if (classrooms.isEmpty())
                    binding.textViewEmptyList.setVisibility(View.VISIBLE);
                else
                    binding.textViewEmptyList.setVisibility(View.INVISIBLE);
                classroomListAdapter.setClassrooms(classrooms);
            }
        });
    }

    private void initAdapter() {
        classroomListAdapter = new ClassroomListAdapter();
        binding.recyclerViewClassroomList.setAdapter(classroomListAdapter);
        binding.recyclerViewClassroomList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
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