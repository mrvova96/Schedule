package com.example.schedule.ui.faculty;

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
import com.example.schedule.adapter.recyclerView.FacultyListAdapter;
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.databinding.FragmentFacultyListBinding;
import com.example.schedule.databinding.FragmentMainBinding;
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

public class FacultyListFragment extends Fragment {

    private static final String BUTTON_TEXT = "buttonText";

    private String buttonText;

    private FragmentFacultyListBinding binding;
    private FragmentMainBinding parentBinding;
    private FacultyListViewModel viewModel;
    private FacultyListAdapter facultyListAdapter;

    public static FacultyListFragment newInstance(String buttonText) {
        FacultyListFragment fragment = new FacultyListFragment();
        Bundle args = new Bundle();
        args.putString(BUTTON_TEXT, buttonText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshFaculties();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFacultyListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(FacultyListViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            buttonText = getArguments().getString(BUTTON_TEXT);
        }

        initAdapter();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.getFaculties().observe(getViewLifecycleOwner(), new Observer<List<Faculty>>() {
            @Override
            public void onChanged(List<Faculty> faculties) {
                if (faculties.isEmpty())
                    binding.textViewEmptyList.setVisibility(View.VISIBLE);
                else
                    binding.textViewEmptyList.setVisibility(View.INVISIBLE);
                facultyListAdapter.setFaculties(faculties);
            }
        });

        facultyListAdapter.setOnFacultyClickListener(new FacultyListAdapter.OnFacultyClickListener() {
            @Override
            public void onFacultyClick(Faculty faculty) {
                if (buttonText.equals("Группы")) {
                    parentBinding.textViewAdminPanel.setText(faculty.getFacultyName().concat(" - Группы"));
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    launchNextFragment(GroupFragment.newInstance(faculty.getID(), null, null));
                }
                if (buttonText.equals("Студенты")) {
                    parentBinding.textViewAdminPanel.setText("Выберите группу");
                    launchNextFragment(GroupListFragment.newInstance(faculty.getID(), faculty.getFacultyName(), buttonText, null));
                }
                if (buttonText.equals("Преподаватели")) {
                    parentBinding.textViewAdminPanel.setText(faculty.getFacultyName().concat(" - Преподаватели"));
                    launchNextFragment(TeacherMenuFragment.newInstance(faculty.getID(), faculty.getFacultyName(), null));
                }
                if (buttonText.equals("Предметы")) {
                    parentBinding.textViewAdminPanel.setText(faculty.getFacultyName().concat(" - Предметы"));
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    launchNextFragment(LessonFragment.newInstance(faculty.getID(), null, null));
                }
                if (buttonText.equals("Аудитории")) {
                    parentBinding.textViewAdminPanel.setText(faculty.getFacultyName().concat(" - Аудитории"));
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    launchNextFragment(ClassroomFragment.newInstance(faculty.getID()));
                }
                if (buttonText.equals("Расписание")) {
                    parentBinding.textViewAdminPanel.setText(faculty.getFacultyName());
                    launchNextFragment(ScheduleMenuFragment.newInstance(faculty.getID(), faculty.getFacultyName(), null));
                }

                if (buttonText.equals("Администраторы")) {
                    parentBinding.textViewAdminPanel.setText(faculty.getFacultyName().concat(" - Администраторы"));
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    launchNextFragment(AdminFragment.newInstance(faculty.getID()));
                }
            }
        });
    }

    private void initAdapter() {
        facultyListAdapter = new FacultyListAdapter();
        binding.recyclerViewFacultyList.setAdapter(facultyListAdapter);
        binding.recyclerViewFacultyList.setLayoutManager(new LinearLayoutManager(getContext()));
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
                parentBinding.textViewAdminPanel.setText("Панель администратора");
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