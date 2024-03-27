package com.example.schedule.ui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.schedule.R;
import com.example.schedule.data.database.entity.Admin;
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.data.database.entity.Group;
import com.example.schedule.data.database.entity.MainAdmin;
import com.example.schedule.data.database.entity.Student;
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.data.enums.UserType;
import com.example.schedule.databinding.FragmentLoginBinding;
import com.example.schedule.presentation.main.LoginViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private String login;
    private String password;
    private String fullName;
    private String name;

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;
    private Snackbar snackbar;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.getMainAdminData().observe(getViewLifecycleOwner(), new Observer<MainAdmin>() {
            @Override
            public void onChanged(MainAdmin mainAdmin) {
                launchNextFragment(WelcomeFragment.newInstance(
                        UserType.MAIN_ADMIN, mainAdmin.toString(), mainAdmin.getName(), "Администратор", 0));
            }
        });

        viewModel.getAdminData().observe(getViewLifecycleOwner(), new Observer<Admin>() {
            @Override
            public void onChanged(Admin admin) {
                fullName = admin.toString();
                name = admin.getName();
                viewModel.getFaculty(admin.getFacultyID());
            }
        });

        viewModel.getFacultyData().observe(getViewLifecycleOwner(), new Observer<Faculty>() {
            @Override
            public void onChanged(Faculty faculty) {
                launchNextFragment(WelcomeFragment.newInstance(
                        UserType.ADMIN, fullName, name, faculty.getFacultyName(), faculty.getID()));
            }
        });

        viewModel.getStudentData().observe(getViewLifecycleOwner(), new Observer<Student>() {
            @Override
            public void onChanged(Student student) {
                fullName = student.toString();
                name = student.getName();
                viewModel.getGroup(student.getGroupID());
            }
        });

        viewModel.getGroupData().observe(getViewLifecycleOwner(), new Observer<Group>() {
            @Override
            public void onChanged(Group group) {
                launchNextFragment(WelcomeFragment.newInstance(
                        UserType.STUDENT, fullName, name, group.getGroupName(), group.getID()));
            }
        });

        viewModel.getTeacherData().observe(getViewLifecycleOwner(), new Observer<Teacher>() {
            @Override
            public void onChanged(Teacher teacher) {
                launchNextFragment(WelcomeFragment.newInstance(
                        UserType.TEACHER, teacher.toString(), teacher.getName(), "Преподаватель", teacher.getID()));
            }
        });

        viewModel.getError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isError) {
                showMessage("Неверный логин или пароль. Пожалуйста, повторите попытку!");
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataIsCorrect()) {
                    viewModel.defineUserType(login, password);
                }
            }
        });
    }

    private boolean dataIsCorrect() {
        login = binding.editTextLogin.getText().toString().trim();
        password = binding.editTextPassword.getText().toString().trim();
        if (login.isBlank() || password.isBlank()) {
            showMessage("Пожалуйста, заполните все поля");
            return false;
        }
        return true;
    }

    private void launchNextFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                .replace(R.id.fragmentContainer, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
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

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(requireContext())
                        .setMessage("Выйти из приложения?")
                        .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Objects.requireNonNull(getActivity()).finish();
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
    public void onPause() {
        super.onPause();
        if (snackbar != null)
            snackbar.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}