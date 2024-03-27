package com.example.schedule.ui.admin;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedule.R;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentAdminMenuBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.ui.classroom.ClassroomFragment;
import com.example.schedule.ui.classroom.ClassroomListFragment;
import com.example.schedule.ui.faculty.FacultyFragment;
import com.example.schedule.ui.faculty.FacultyListFragment;
import com.example.schedule.ui.group.GroupFragment;
import com.example.schedule.ui.group.GroupListFragment;
import com.example.schedule.ui.lesson.LessonFragment;
import com.example.schedule.ui.main.MainFragment;
import com.example.schedule.ui.schedule.ScheduleMenuFragment;
import com.example.schedule.ui.teacher.TeacherMenuFragment;

public class AdminMenuFragment extends Fragment {

    private static final String USER_TYPE = "userType";
    private static final String FACULTY_ID = "facultyID";
    private static final String FACULTY_NAME = "facultyName";

    private UserType userType;
    private int facultyID;
    private String facultyName;

    private FragmentAdminMenuBinding binding;
    private FragmentMainBinding parentBinding;

    public static AdminMenuFragment newInstance(UserType userType, int facultyID, String facultyName) {
        AdminMenuFragment fragment = new AdminMenuFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_TYPE, userType);
        args.putInt(FACULTY_ID, facultyID);
        args.putString(FACULTY_NAME, facultyName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminMenuBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            userType = (UserType) getArguments().getSerializable(USER_TYPE);
            facultyID = getArguments().getInt(FACULTY_ID);
            facultyName = getArguments().getString(FACULTY_NAME);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        if (userType == UserType.ADMIN) {
            binding.buttonFacultiesList.setVisibility(View.GONE);
            binding.buttonAdminsList.setVisibility(View.GONE);
        }

        binding.buttonScheduleList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userType) {
                    case MAIN_ADMIN:
                        parentBinding.textViewAdminPanel.setText("Выберите факультет");
                        launchNextFragment(FacultyListFragment.newInstance(String.valueOf(binding.buttonScheduleList.getText())));
                        break;
                    case ADMIN:
                        launchNextFragment(ScheduleMenuFragment.newInstance(facultyID, facultyName, userType));
                        break;
                }

            }
        });

        binding.buttonFacultiesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentBinding.textViewAdminPanel.setText("Факультеты");
                parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                launchNextFragment(FacultyFragment.newInstance());
            }
        });

        binding.buttonGroupsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userType) {
                    case MAIN_ADMIN:
                        parentBinding.textViewAdminPanel.setText("Выберите факультет");
                        launchNextFragment(FacultyListFragment.newInstance(String.valueOf(binding.buttonGroupsList.getText())));
                        break;
                    case ADMIN:
                        parentBinding.textViewAdminPanel.setText(facultyName.concat(" - Группы"));
                        launchNextFragment(GroupListFragment.newInstance(facultyID, facultyName, "", userType));
                        break;
                }
            }
        });

        binding.buttonLessonsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userType) {
                    case MAIN_ADMIN:
                        parentBinding.textViewAdminPanel.setText("Выберите факультет");
                        launchNextFragment(FacultyListFragment.newInstance(String.valueOf(binding.buttonLessonsList.getText())));
                        break;
                    case ADMIN:
                        parentBinding.textViewAdminPanel.setText(facultyName.concat(" - Предметы"));
                        parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                        launchNextFragment(LessonFragment.newInstance(facultyID, facultyName, userType));
                        break;
                }

            }
        });

        binding.buttonClassroomsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userType) {
                    case MAIN_ADMIN:
                        parentBinding.textViewAdminPanel.setText("Выберите факультет");
                        launchNextFragment(FacultyListFragment.newInstance(String.valueOf(binding.buttonClassroomsList.getText())));
                        break;
                    case ADMIN:
                        parentBinding.textViewAdminPanel.setText(facultyName.concat(" - Аудитории"));
                        launchNextFragment(ClassroomListFragment.newInstance(facultyID, facultyName));
                        break;
                }
            }
        });

        binding.buttonStudentsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userType) {
                    case MAIN_ADMIN:
                        parentBinding.textViewAdminPanel.setText("Выберите факультет");
                        launchNextFragment(FacultyListFragment.newInstance(String.valueOf(binding.buttonStudentsList.getText())));
                        break;
                    case ADMIN:
                        parentBinding.textViewAdminPanel.setText("Выберите группу");
                        launchNextFragment(GroupListFragment.newInstance(facultyID, facultyName, "Студенты", userType));
                        break;
                }
            }
        });

        binding.buttonTeachersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (userType) {
                    case MAIN_ADMIN:
                        parentBinding.textViewAdminPanel.setText("Выберите факультет");
                        launchNextFragment(FacultyListFragment.newInstance(String.valueOf(binding.buttonTeachersList.getText())));
                        break;
                    case ADMIN:
                        parentBinding.textViewAdminPanel.setText(facultyName.concat(" - Преподаватели"));
                        launchNextFragment(TeacherMenuFragment.newInstance(facultyID, facultyName, userType));
                }
            }
        });

        binding.buttonAdminsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentBinding.textViewAdminPanel.setText("Выберите факультет");
                launchNextFragment(FacultyListFragment.newInstance(String.valueOf(binding.buttonAdminsList.getText())));
            }
        });
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
                new AlertDialog.Builder(requireContext())
                        .setMessage("Выйти из приложения?")
                        .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getActivity().finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("Назад", null)
                        .show();
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