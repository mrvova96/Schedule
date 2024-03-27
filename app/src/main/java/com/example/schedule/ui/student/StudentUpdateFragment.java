package com.example.schedule.ui.student;

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
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.data.database.entity.Student;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentStudentUpdateBinding;
import com.example.schedule.presentation.student.StudentUpdateViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

public class StudentUpdateFragment extends Fragment {

    private static final String STUDENT_ID = "studentID";
    private static final String SURNAME = "surname";
    private static final String NAME = "name";
    private static final String PATRONYMIC = "patronymic";
    private static final String GROUP_ID = "groupID";

    private long studentID;
    private String surname;
    private String name;
    private String patronymic;
    private long groupID;
    private String login;
    private String password;

    private FragmentStudentUpdateBinding binding;
    private FragmentMainBinding parentBinding;
    private StudentUpdateViewModel viewModel;
    private Snackbar snackbar;

    public static StudentUpdateFragment newInstance(
            long studentID, String surname, String name, String patronymic, long groupID) {
        StudentUpdateFragment fragment = new StudentUpdateFragment();
        Bundle args = new Bundle();
        args.putLong(STUDENT_ID, studentID);
        args.putString(SURNAME, surname);
        args.putString(NAME, name);
        args.putString(PATRONYMIC, patronymic);
        args.putLong(GROUP_ID, groupID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudentUpdateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(StudentUpdateViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            studentID = getArguments().getLong(STUDENT_ID);
            surname = getArguments().getString(SURNAME);
            name = getArguments().getString(NAME);
            patronymic = getArguments().getString(PATRONYMIC);
            groupID = getArguments().getLong(GROUP_ID);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.getLoginDetails(studentID);

        binding.editTextUpdateSurname.setText(surname);
        binding.editTextUpdateName.setText(name);
        binding.editTextUpdatePatronymic.setText(patronymic);

        viewModel.getLoginData().observe(getViewLifecycleOwner(), new Observer<LoginDetails>() {
            @Override
            public void onChanged(LoginDetails loginData) {
                login = loginData.getLogin();
                password = loginData.getPassword();
                binding.editTextUpdateLogin.setText(login);
                binding.editTextUpdatePassword.setText(password);
            }
        });

        viewModel.loginIsExist().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExist) {
                if (isExist)
                    showMessage("Выбранный логин уже занят");
                else
                    updateStudent();
            }
        });

        viewModel.isUpdated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdated) {
                if (isUpdated) {
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                    showMessage("Запись успешно обновлена");
                } else
                    showMessage("Студент уже содержится в базе");
            }
        });

        binding.buttonUpdateStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    if (login.equals(binding.editTextUpdateLogin.getText().toString().trim()))
                        updateStudent();
                    else
                        viewModel.isLoginExists(binding.editTextUpdateLogin.getText().toString().trim());
                }
            }
        });
    }

    private void updateStudent() {
        surname = binding.editTextUpdateSurname.getText().toString().trim();
        name = binding.editTextUpdateName.getText().toString().trim();
        patronymic = binding.editTextUpdatePatronymic.getText().toString().trim();
        login = binding.editTextUpdateLogin.getText().toString().trim();
        password = binding.editTextUpdatePassword.getText().toString().trim();
        viewModel.update(new Student(studentID, surname, name, patronymic, groupID), login, password);
    }

    private boolean dataIsCorrect() {
        if (binding.editTextUpdateSurname.getText().toString().isBlank()
                || binding.editTextUpdateName.getText().toString().isBlank()
                || binding.editTextUpdatePatronymic.getText().toString().isBlank()
                || binding.editTextUpdateLogin.getText().toString().isBlank()
                || binding.editTextUpdatePassword.getText().toString().isBlank()) {
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