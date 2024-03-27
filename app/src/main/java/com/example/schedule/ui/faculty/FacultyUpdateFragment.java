package com.example.schedule.ui.faculty;

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
import com.example.schedule.data.database.entity.Faculty;
import com.example.schedule.databinding.FragmentFacultyUpdateBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.faculty.FacultyUpdateViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

public class FacultyUpdateFragment extends Fragment {

    private static final String FACULTY_ID = "facultyID";
    private static final String FACULTY_NAME = "facultyName";

    private int facultyID;
    private String facultyName;

    private FragmentFacultyUpdateBinding binding;
    private FragmentMainBinding parentBinding;
    private FacultyUpdateViewModel viewModel;
    private Snackbar snackbar;

    public static FacultyUpdateFragment newInstance(int facultyID, String facultyName) {
        FacultyUpdateFragment fragment = new FacultyUpdateFragment();
        Bundle args = new Bundle();
        args.putInt(FACULTY_ID, facultyID);
        args.putString(FACULTY_NAME, facultyName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFacultyUpdateBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(FacultyUpdateViewModel.class);

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        if (getArguments() != null) {
            facultyID = getArguments().getInt(FACULTY_ID);
            facultyName = getArguments().getString(FACULTY_NAME);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        binding.editTextUpdateFaculty.setText(facultyName);

        viewModel.isUpdated().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUpdated) {
                if (isUpdated) {
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                    showMessage("Запись успешно обновлена");
                } else
                    showMessage("Факультет уже содержится в базе");
            }
        });

        binding.buttonUpdateFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    updateFaculty();
                }
            }
        });
    }

    private void updateFaculty() {
        facultyName = binding.editTextUpdateFaculty.getText().toString().trim();
        viewModel.update(new Faculty(facultyID, facultyName));
    }

    private boolean dataIsCorrect() {
        if (binding.editTextUpdateFaculty.getText().toString().isBlank()) {
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