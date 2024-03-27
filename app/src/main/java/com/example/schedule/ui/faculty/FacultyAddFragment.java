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
import com.example.schedule.databinding.FragmentFacultyAddBinding;
import com.example.schedule.databinding.FragmentMainBinding;
import com.example.schedule.presentation.faculty.FacultyAddViewModel;
import com.example.schedule.ui.main.MainFragment;
import com.google.android.material.snackbar.Snackbar;

public class FacultyAddFragment extends Fragment {

    private FragmentFacultyAddBinding binding;
    private FragmentMainBinding parentBinding;
    private FacultyAddViewModel viewModel;
    private Snackbar snackbar;

    public static FacultyAddFragment newInstance() {
        return new FacultyAddFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFacultyAddBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getParentFragment() != null)
            parentBinding = ((MainFragment) getParentFragment()).getBinding();

        viewModel = new ViewModelProvider(this).get(FacultyAddViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnBackPressed();

        viewModel.isAdded().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isAdded) {
                if (isAdded) {
                    parentBinding.imageButtonAdd.setVisibility(View.VISIBLE);
                    getParentFragmentManager().popBackStack();
                    showMessage("Запись успешно добавлена");
                } else
                    showMessage("Факультет уже содержится в базе");

            }
        });

        binding.buttonAddFaculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataIsCorrect()) {
                    saveFaculty();
                }
            }
        });
    }

    private void saveFaculty() {
        String facultyName = binding.editTextAddFaculty.getText().toString().trim();
        viewModel.add(new Faculty(facultyName));
    }

    private boolean dataIsCorrect() {
        if (binding.editTextAddFaculty.getText().toString().isBlank()) {
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