package com.example.schedule.ui.teacher;

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
import com.example.schedule.adapter.recyclerView.TeacherListAdapter;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentTeacherListBinding;
import com.example.schedule.presentation.teacher.TeacherListViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.example.schedule.ui.schedule.ScheduleMainFragment;

import java.util.List;

public class TeacherListFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";
    private static final String FACULTY_NAME = "facultyName";
    private static final String BUTTON_TEXT = "buttonText";

    private int facultyID;
    private String facultyName;
    private String buttonText;

    private FragmentTeacherListBinding binding;
    private FragmentMainBinding parentBinding;
    private TeacherListViewModel viewModel;
    private TeacherListAdapter teacherListAdapter;

    public static TeacherListFragment newInstance(int facultyID, String facultyName, String buttonText) {
        TeacherListFragment fragment = new TeacherListFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        args.putString(FACULTY_NAME, facultyName);
        args.putString(BUTTON_TEXT, buttonText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshTeachers(facultyID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTeacherListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(TeacherListViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
            facultyName = getArguments().getString(FACULTY_NAME);
            buttonText = getArguments().getString(BUTTON_TEXT);
        }

        initAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.getTeachers().observe(getViewLifecycleOwner(), new Observer<List<Teacher>>() {
            @Override
            public void onChanged(List<Teacher> teachers) {
                if (teachers.isEmpty())
                    binding.textViewEmptyList.setVisibility(View.VISIBLE);
                else
                    binding.textViewEmptyList.setVisibility(View.INVISIBLE);
                teacherListAdapter.setTeachers(teachers);
            }
        });

        teacherListAdapter.setOnTeacherClickListener(new TeacherListAdapter.OnTeacherClickListener() {
            @Override
            public void onTeacherClick(Teacher teacher) {
                if (buttonText.equals("Изменить предметы")) {
                    parentBinding.textViewAdminPanel.setText(teacher.getSurname().concat(" ")
                            .concat(String.valueOf(teacher.getName().charAt(0))).concat(". ")
                            .concat(String.valueOf(teacher.getPatronymic().charAt(0))).concat(".")
                            .concat(" - Предметы"));
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    launchNextFragment(TeacherLessonsFragment.newInstance(facultyID, teacher.getID()));
                }
                if (buttonText.equals("Расписание преподавателя")) {
                    parentBinding.textViewGroupName.setText(teacher.getSurname().concat(" ")
                            .concat(String.valueOf(teacher.getName().charAt(0))).concat(". ")
                            .concat(String.valueOf(teacher.getPatronymic().charAt(0))).concat("."));
                    parentBinding.textViewAdminPanel.setVisibility(View.INVISIBLE);
                    parentBinding.tabLayout.setVisibility(View.VISIBLE);
                    launchNextFragment(ScheduleMainFragment.newInstance(UserType.ADMIN, facultyID, 0, teacher.getID()));
                }
            }
        });
    }

    private void initAdapter() {
        teacherListAdapter = new TeacherListAdapter();
        binding.recyclerViewTeacherList.setAdapter(teacherListAdapter);
        binding.recyclerViewTeacherList.setLayoutManager(new LinearLayoutManager(getContext()));
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
                if (buttonText.equals("Изменить предметы")) {
                    parentBinding.textViewAdminPanel.setText(facultyName.concat(" - ").concat("Преподаватели"));
                }
                if (buttonText.equals("Расписание преподавателя")) {
                    parentBinding.textViewAdminPanel.setText(facultyName);
                }
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