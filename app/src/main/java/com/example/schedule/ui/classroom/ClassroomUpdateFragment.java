package com.example.schedule.ui.classroom;

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
import com.example.schedule.data.database.entity.Classroom;
import com.example.schedule.databinding.FragmentClassroomUpdateBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.classroom.ClassroomUpdateViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

public class ClassroomUpdateFragment extends Fragment {

    private static final String CLASSROOM_ID = "classroomID";
    private static final String CLASSROOM_NUMBER = "classroomNumber";
    private static final String FACULTY_ID = "facultyID";

    private int classroomID;
    private int classroomNumber;
    private int facultyID;

    private FragmentClassroomUpdateBinding binding;
    private FragmentMainBinding parentBinding;
    private ClassroomUpdateViewModel viewModel;
    private Snackbar snackbar;

    public static ClassroomUpdateFragment newInstance(int classroomID, int classroomNumber, int facultyID) {
        ClassroomUpdateFragment fragment = new ClassroomUpdateFragment();
        Bundle args = new Bundle();
        args.putInt(CLASSROOM_ID, classroomID);
        args.putInt(CLASSROOM_NUMBER, classroomNumber);
        args.putInt(FACULTY_ID, facultyID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClassroomUpdateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(ClassroomUpdateViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            classroomID = getArguments().getInt(CLASSROOM_ID);
            classroomNumber = getArguments().getInt(CLASSROOM_NUMBER);
            facultyID = getArguments().getInt(FACULTY_ID);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        binding.editTextUpdateClassroom.setText(String.valueOf(classroomNumber));

        viewModel.isUpdated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdated) {
                if (isUpdated) {
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                    showMessage("Запись успешно обновлена");
                } else
                    showMessage("Аудитория уже содержится для данного факультета");
            }
        });

        binding.buttonUpdateClassroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    updateClassroom();
                }
            }
        });
    }

    private void updateClassroom() {
        classroomNumber = Integer.parseInt(binding.editTextUpdateClassroom.getText().toString().trim());
        viewModel.update(new Classroom(classroomID, classroomNumber, facultyID));
    }

    private boolean dataIsCorrect() {
        try {
            if (binding.editTextUpdateClassroom.getText().toString().isBlank()) {
                showMessage("Пожалуйста, заполните все поля");
                return false;
            }
            if (Integer.parseInt(binding.editTextUpdateClassroom.getText().toString().trim()) <= 0) {
                showMessage("Номер аудитории должен быть натуральным числом");
                return false;
            }
        } catch (Exception e) {
            showMessage("Номер аудитории должен быть натуральным числом");
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