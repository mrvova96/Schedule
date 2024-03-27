package com.example.schedule.ui.admin;

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
import com.example.schedule.data.database.entity.Admin;
import com.example.schedule.data.database.entity.LoginDetails;
import com.example.schedule.databinding.FragmentAdminAddBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.admin.AdminAddViewModel;
import com.example.schedule.ui.classroom.ClassroomAddFragment;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

public class AdminAddFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";

    private int facultyID;

    private FragmentAdminAddBinding binding;
    private FragmentMainBinding parentBinding;
    private AdminAddViewModel viewModel;
    private Snackbar snackbar;

    public static AdminAddFragment newInstance(int facultyID) {
        AdminAddFragment fragment = new AdminAddFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminAddBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(AdminAddViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.isAdminExists().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isExist) {
                if (isExist)
                    showMessage("Администратор уже содержится в базе");
                else
                    saveAdmin();
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

        binding.buttonAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    String surname = binding.editTextAddSurname.getText().toString().trim();
                    String name = binding.editTextAddName.getText().toString().trim();
                    String patronymic = binding.editTextAddPatronymic.getText().toString().trim();
                    viewModel.checkAdmin(surname, name, patronymic);
                }
            }
        });
    }

    private void saveAdmin() {
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