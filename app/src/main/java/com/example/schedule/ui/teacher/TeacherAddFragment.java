package com.example.schedule.ui.teacher;

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
import com.example.schedule.data.database.entity.Teacher;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.databinding.FragmentTeacherAddBinding;
import com.example.schedule.presentation.teacher.TeacherAddViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

public class TeacherAddFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";

    private int facultyID;

    private FragmentTeacherAddBinding binding;
    private FragmentMainBinding parentBinding;
    private TeacherAddViewModel viewModel;
    private Snackbar snackbar;

    public static TeacherAddFragment newInstance(int facultyID) {
        TeacherAddFragment fragment = new TeacherAddFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTeacherAddBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        viewModel = new ViewModelProvider(this).get(TeacherAddViewModel.class);

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.isTeacherExists().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExist) {
                if (isExist)
                    showMessage("Преподаватель уже содержится в базе");
                else
                    saveTeacher();
            }
        });

        viewModel.isAdded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdded) {
                if (isAdded) {
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                    showMessage("Запись успешно добавлена");
                } else
                    showMessage("Выбранный логин уже занят");
            }
        });

        binding.buttonAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    String surname = binding.editTextAddSurname.getText().toString().trim();
                    String name = binding.editTextAddName.getText().toString().trim();
                    String patronymic = binding.editTextAddPatronymic.getText().toString().trim();
                    viewModel.checkTeacher(surname, name, patronymic);
                }
            }
        });
    }

    private void saveTeacher() {
        String surname = binding.editTextAddSurname.getText().toString().trim();
        String name = binding.editTextAddName.getText().toString().trim();
        String patronymic = binding.editTextAddPatronymic.getText().toString().trim();
        String login = binding.editTextAddLogin.getText().toString().trim();
        String password = binding.editTextAddPassword.getText().toString().trim();
        viewModel.addLoginDetails(new LoginDetails(login, password), surname, name, patronymic, facultyID);
    }

    private boolean dataIsCorrect() {
        if (binding.editTextAddSurname.getText().toString().isBlank()
                || binding.editTextAddName.getText().toString().isBlank()
                || binding.editTextAddPatronymic.getText().toString().isBlank()
                || binding.editTextAddLogin.getText().toString().isBlank()
                || binding.editTextAddPassword.getText().toString().isBlank()) {
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